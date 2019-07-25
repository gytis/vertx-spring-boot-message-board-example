package dev.snowdrop.example;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * A makeshift messages broker.
 * In reality this would be replaced by publishing and subscribing to an AMQ multi-cast address or a Kafka topic.
 */
@Component
public class MessagesBroker {

    private final Flux<Message> messagesFlux;

    private AtomicReference<FluxSink<Message>> messagesSink = new AtomicReference<>();

    public MessagesBroker() {
        messagesFlux = Flux.create(messagesSink::set)
                .share();
    }

    /**
     * Publish message to a broker.
     *
     * @param message
     */
    public void publish(Message message) {
        System.out.println(message);
        if (messagesSink.get() != null) {
            messagesSink.get().next(message);
        }
    }

    /**
     * Subscribe to a broker in order to receive newly published messages.
     *
     * @return Flux of messages
     */
    public Flux<Message> subscribe() {
        return messagesFlux;
    }
}
