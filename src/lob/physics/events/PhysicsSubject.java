package lob.physics.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe base que gere a lista de observadores.
 * Agora é uma classe concreta para permitir que o PhysicsSubjectTest a instancie.
 */
public class PhysicsSubject<T extends PhysicsEvent> {

    private final List<PhysicsObserver<T>> observers = new ArrayList<>();

    public void addObserver(PhysicsObserver<T> observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(PhysicsObserver<T> observer) {
        observers.remove(observer);
    }

    /**
     * Resolve o erro de "cannot find symbol" no teste ao disparar notificações.
     */
    public void notifyObservers(T event) {
        if (event == null) return;
        // Cópia para evitar ConcurrentModificationException durante o loop
        for (PhysicsObserver<T> observer : new ArrayList<>(observers)) {
            observer.notified(event);
        }
    }
}