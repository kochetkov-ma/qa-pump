package ru.iopump.qa.spring.scope;

import com.google.common.collect.Queues;
import java.util.Queue;
import javax.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;

public class PostponeApplicationEventPublisher implements ApplicationEventPublisher {
    private static final Object LOCK = new Object();
    private static final Queue<Object> eventQueue = Queues.newConcurrentLinkedQueue();
    private ApplicationEventPublisher publisher = eventQueue::add;

    @Override
    public void publishEvent(@Nonnull Object event) {
        synchronized (LOCK) {
            publisher.publishEvent(event);
        }
    }

    public void redirectTo(@NonNull ApplicationEventPublisher publisher) {
        synchronized (LOCK) {
            this.publisher = publisher;
            tryPublishEvents();
        }
    }

    private void tryPublishEvents() {
        for (var item = eventQueue.poll(); item != null; item = eventQueue.poll()) {
            this.publisher.publishEvent(item);
        }
    }
}
