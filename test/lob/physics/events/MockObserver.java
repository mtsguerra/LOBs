package lob.physics.events;

/**
 * A mock implementation of the {@link PhysicsObserver} interface used for testing purposes.
 * <p>
 * The {@code MockObserver} class serves as a simulated observer that listens to
 * events of type {@code MockPhysicsEvent}. It maintains an internal count of the number
 * of events it has been notified about. This class is specifically designed for use in
 * testing environments to validate the behavior of event-driven interactions between
 * subjects and observers.
 * <p>
 * This implementation is minimal and only increments the {@code count} field
 * each time the {@code notified} method is invoked, ensuring that the number
 * of received notifications can be tracked and verified.
 *
 * @param <T> the type of physics event this observer listens to, constrained to instances
 *            of {@code MockPhysicsEvent}
 */
public class MockObserver<T extends PhysicsEvent> implements PhysicsObserver<T> {
    private int count = 0;

    @Override
    public void notified(T event) {
        count++;
    }

    /**
     * Returns the current count of events that this observer has been notified about.
     *
     * @return the number of notifications received by this observer
     */
    public int getCount() {
        return count;
    }
}
