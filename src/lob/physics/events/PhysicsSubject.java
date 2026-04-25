package lob.physics.events;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSubject<T extends PhysicsEvent> {

    // Inicializamos logo aqui para maior clareza
    private final List<PhysicsObserver<T>> observers = new ArrayList<>();

    public PhysicsSubject() {
        // O construtor agora pode ficar vazio
    }

    public void addObserver(PhysicsObserver<T> observer) {
        // Evitamos adicionar null e duplicados
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(PhysicsObserver<T> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(T event) {
        if (event == null) return;

        // Usamos um loop protegido para evitar erros se a lista mudar durante o envio
        for (PhysicsObserver<T> observer : new ArrayList<>(observers)) {
            observer.notified(event);
        }
    }
}