package lob.physics.events;

import java.util.ArrayList;
import java.util.List;

// A classe usa o Genérico T, que tem de ser um tipo de PhysicsEvent
public class PhysicsSubject<T extends PhysicsEvent> {

    // A lista onde guardamos todos os "cuscos" (observadores) que querem saber dos eventos
    private final List<PhysicsObserver<T>> observers;

    public PhysicsSubject() {
        this.observers = new ArrayList<>();
    }

    // Método para um observador se registar
    public void addObserver(PhysicsObserver<T> observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    // Método para um observador cancelar a subscrição
    public void removeObserver(PhysicsObserver<T> observer) {
        observers.remove(observer);
    }

    // Método que avisa todos os observadores quando um evento acontece!
    public void notifyObservers(T event) {
        for (PhysicsObserver<T> observer : observers) {
            observer.notified(event); // Chama o método "notified" que criámos antes
        }
    }
}