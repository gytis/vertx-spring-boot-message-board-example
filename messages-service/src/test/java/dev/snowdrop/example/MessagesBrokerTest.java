package dev.snowdrop.example;

import java.time.Instant;

import org.junit.Test;
import reactor.test.StepVerifier;

public class MessagesBrokerTest {

    @Test
    public void shouldPublishAndSubscribe() {
        MessagesBroker broker = new MessagesBroker();
        Message first = new Message(1, Instant.MIN, "user1", "message1");
        Message second = new Message(2, Instant.MAX, "user2", "message2");

        StepVerifier.create(broker.subscribe())
                .then(() -> broker.publish(first))
                .expectNext(first)
                .then(() -> broker.publish(second))
                .expectNext(second)
                .thenCancel()
                .verify();
    }
}