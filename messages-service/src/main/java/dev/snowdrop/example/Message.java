package dev.snowdrop.example;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Message {

    private static final AtomicLong idCounter = new AtomicLong();

    private final long id;

    private final Instant timestamp;

    private String sender;

    private String text;

    public Message() {
        this.id = idCounter.incrementAndGet();
        this.timestamp = Instant.now();
    }

    Message(long id, Instant timestamp, String sender, String text) {
        this.id = id;
        this.timestamp = timestamp;
        this.sender = sender;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("Message (id=%s, sender=%s, text=%s, timestamp=%s)", id, sender, text, timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Message message = (Message) o;
        return id == message.id &&
                Objects.equals(timestamp, message.timestamp) &&
                Objects.equals(sender, message.sender) &&
                Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, sender, text);
    }
}
