package lob.physics.events;

public interface PhysicsObserver<T extends PhysicsEvent> {

    // Método chamado quando o evento acontece
    void notified(T event);

}