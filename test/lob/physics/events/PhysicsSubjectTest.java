package lob.physics.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The {@code PhysicsSubjectTest} class contains unit tests for the {@link PhysicsSubject} class
 * to verify the behavior of event-driven systems in a physics simulation context.
 *
 * These tests ensure proper functionality of event notification mechanisms, including the
 * ability to add and remove observers, notify multiple observers, and validate event delivery.
 *
 * The test suite uses mock implementations of {@link PhysicsEvent} and {@link PhysicsObserver}
 * for isolated and controlled testing.
 */
class PhysicsSubjectTest {

    PhysicsSubject<MockPhysicsEvent> subject;
    MockObserver observer;

    /**
     * Sets up the environment required for each test in the {@code PhysicsSubjectTest} class.
     *
     * This method initializes the necessary test objects, including:
     * - A {@code PhysicsSubject} instance, which will serve as the subject under test.
     * - A mock observer instance, {@code MockObserver}, which will act as a simulated
     *   observer to validate the behavior of the subject.
     *
     * The method is executed before each test case to ensure a consistent and isolated
     * test environment.
     */
    @BeforeEach
    void setup() {
        subject = new PhysicsSubject<>();
        observer = new MockObserver();
    }

    /**
     * Tests the event delivery mechanism of the {@code PhysicsSubject} class by verifying
     * that an observer receives the correct number of notifications when registered.
     *
     * This method ensures that:
     * - An observer receives events in the order they are notified.
     * - The number of events received by the observer matches the number of events emitted.
     *
     * The test first adds an observer to the subject, then repeatedly triggers event
     * notifications using the {@code notifyObservers} method. After each notification,
     * assertions are made to confirm that the observer's state has been updated appropriately.
     *
     * The final assertion verifies that the observer's count matches the total number of
     * expected events.
     */
    @Test
    void testEventDelivery() {
        subject.addObserver(observer);
        int total = 10;
        for(int i = 0; i < total; i++) {
            assertEquals(i, observer.getCount(), i + " event(s) expected");
            subject.notifyObservers(new MockPhysicsEvent());
        }
        assertEquals( total , observer.getCount(), total+" events expected");
    }

    /**
     * Tests the functionality of the {@code addObserver} method in the {@code PhysicsSubject} class.
     *
     * This test ensures that an observer can be added to a subject and properly receives notifications
     * of events. Initially, the observer count is confirmed to be zero. After an event is triggered without
     * adding the observer, the observer's state is verified to remain unchanged. Once the observer is added,
     * a subsequent event is triggered, and the observer's state is verified to confirm reception of the event.
     *
     * The test verifies robustness by asserting:
     * - Initial state of the observer when no event is processed.
     * - State of the observer when an event occurs prior to being added.
     * - Correct update to the observer's state upon notification after being added.
     */
    @Test
    void addObserver() {
        assertEquals(0, observer.getCount(),"no events initially");

        subject.notifyObservers(new MockPhysicsEvent());

        assertEquals(0, observer.getCount(),"not observer added yet");

        subject.addObserver(observer);
        subject.notifyObservers(new MockPhysicsEvent());

        assertEquals(1, observer.getCount(),"an observer was added");
    }

    /**
     * Tests the functionality of the {@code removeObserver} method in the
     * {@code PhysicsSubject} class.
     *
     * This test verifies that an observer, once removed from the subject, no longer
     * receives notifications of events. The observer is first added to the subject
     * and confirmed to receive notifications. After removal, it is verified that
     * the observer no longer responds to further events.
     *
     * The test ensures correctness by checking the internal state of the observer
     * before and after its removal from the subject.
     */
    @Test
    void removeObserver() {
        subject.addObserver(observer);
        subject.notifyObservers(new MockPhysicsEvent());

        assertEquals(1, observer.getCount(),"observer was added");

        subject.removeObserver(observer);
        subject.notifyObservers(new MockPhysicsEvent());

        assertEquals(1, observer.getCount(),"observer was removed");
    }

    /**
     * Tests the notification process of multiple observers in a physics-related subject.
     * This method verifies the behavior of adding multiple observers to the subject,
     * notifying all registered observers with an event, and then removing them.
     * It ensures that observers are notified exactly once per event and that no further
     * notifications occur after removal.
     *
     * @param nObservers the number of observers to be created, added to the subject,
     *                   and tested for correct notification behavior
     */
    @ParameterizedTest
    @CsvSource({ "1", "2", "3", "10", "20"})
    void notifyMultipleObservers(int nObservers) {
        var observers = IntStream.range(0, nObservers).mapToObj((i) -> new MockObserver()).collect(Collectors.toList());

        observers.stream().forEach(observer -> assertEquals(0, observer.getCount(),
                "nothing happened yet"));

        observers.stream().forEach(observer -> subject.addObserver(observer));

        observers.stream().forEach(observer -> assertEquals(0, observer.getCount(),
                "no events sent yet"));

        subject.notifyObservers(new MockPhysicsEvent());

        observers.stream().forEach(observer -> assertEquals(1, observer.getCount(),
                "one event expected"));

        observers.stream().forEach(observer -> subject.removeObserver(observer));

        subject.notifyObservers(new MockPhysicsEvent());

        observers.stream().forEach(observer -> assertEquals(1, observer.getCount(),
                "no more events expected"));
    }

    /**
     * Tests the proper handling of observer removal during the execution of a callback within
     * the {@code notifyObservers} method in the {@code PhysicsSubject} class.
     *
     * This test verifies that:
     * - It is safe to modify the observer list while iterating over it during event notifications.
     * - Observers can correctly remove other observers or themselves without causing
     *   a {@code ConcurrentModificationException}.
     *
     * The method performs the following:
     * - Adds an initial observer to the subject.
     * - Adds another observer that removes the initial observer during its callback execution.
     * - Triggers the {@code notifyObservers} method to ensure that modifying the observer list
     *   during iteration does not disrupt the process.
     *
     * If a {@code ConcurrentModificationException} is thrown during the iteration, the test fails,
     * indicating that the implementation must iterate over a copy of the observers to avoid such issues.
     */
    @Test
    void removeObserverInCallback() {
        subject.addObserver(observer);
        subject.addObserver((event) -> {
            subject.removeObserver(observer);
                });
        try {
            subject.notifyObservers(new MockPhysicsEvent());
        } catch(ConcurrentModificationException e) {
            fail("Should be able to modify observers in an callback.\n"+
                    ">>> You need to iterate over a copy of observers."
            );
        }
    }

}