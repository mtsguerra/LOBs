package lob.physics.events;

/**
 * A mock implementation of the {@link PhysicsEvent} interface used for testing purposes
 * within event-driven systems.
 * <p>
 * The {@code MockPhysicsEvent} record represents a simulation or dummy event in the
 * physics domain. It is specifically designed to serve as a minimalistic placeholder in
 * tests to validate the functionality of event notification and observer behaviors.
 * <p>
 * Instances of this class are utilized to trigger and verify interactions between
 * subjects and observers without introducing complex event-specific logic. This ensures
 * isolated and efficient testing of event-driven components.
 */
record MockPhysicsEvent() implements PhysicsEvent {
}
