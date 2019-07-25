package dev.snowdrop.example;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessagesServiceApplicationTest {

    @LocalServerPort
    private String port;

    @Autowired
    private WebClient.Builder builder;

    private WebClient client;

    @Before
    public void setUp() {
        client = builder.baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void shouldSendAndReceiveMessages() {
        Message first = new Message(1, Instant.MIN, "user1", "message1");
        Message second = new Message(2, Instant.MAX, "user2", "message2");

        StepVerifier.create(receiveMessages())
                .then(() -> sendMessage(first))
                .then(() -> sendMessage(second))
                .expectNext(first)
                .expectNext(second)
                .thenCancel()
                .verify();
    }

    private Flux<Message> receiveMessages() {
        return client.get()
                .accept(APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(Message.class);
    }

    private void sendMessage(Message message) {
        client.post()
                .contentType(APPLICATION_JSON)
                .syncBody(message)
                .retrieve()
                .bodyToMono(Message.class)
                .block();
    }
}