package dev.snowdrop.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class MessagesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagesServiceApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> router(MessagesBroker broker) {
        return route()
                .GET("/", accept(APPLICATION_STREAM_JSON), request -> getHandler(broker))
                .POST("/", request -> postHandler(request, broker))
                .build();
    }

    private Mono<ServerResponse> getHandler(MessagesBroker broker) {
        // Subscribe to a broker and send messages to an HTTP stream whenever they arrive
        return ok()
                .contentType(APPLICATION_STREAM_JSON)
                .body(broker.subscribe(), Message.class);
    }

    private Mono<ServerResponse> postHandler(ServerRequest request, MessagesBroker broker) {
        // Publish a message to a broker and retain its instance to be returned as an HTTP response body
        Mono<Message> messageMono = request.bodyToMono(Message.class)
                .doOnNext(broker::publish);

        return accepted()
                .contentType(APPLICATION_JSON)
                .body(messageMono, Message.class);
    }
}
