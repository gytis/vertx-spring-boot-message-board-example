package dev.snowdrop.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
public class MessagesController {

    private static final String MESSAGES_SERVICE_URL = "http://localhost:8081";

    private final WebClient webClient;

    public MessagesController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(MESSAGES_SERVICE_URL)
                .build();
    }

    @GetMapping(path = "/messages", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> getMessages() {
        return webClient.get()
                .accept(APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(Message.class);
    }
}
