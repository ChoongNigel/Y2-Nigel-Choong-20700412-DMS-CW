package com.comp2042.GameLogic;

public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Declare event type, then help to decide what action to take
     * @param eventType
     * @param eventSource
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventSource getEventSource() {
        return eventSource;
    }
}
