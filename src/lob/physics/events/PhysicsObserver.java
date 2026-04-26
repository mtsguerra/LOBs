package lob.physics.events;

public interface PhysicsObserver<T extends PhysicsEvent> {
    /**
     * Método chamado quando um evento ocorre.
     */
    void notified(T event);
}