package lob.physics.engine;

import lob.physics.forces.ForceStrategy;
import lob.physics.shapes.Shape;
import lob.physics.Vector2D;

/**
 * A mock implementation of the ForceStrategy interface for testing purposes.
 * This strategy returns a predefined acceleration vector for all inputs.
 * <p>
 * The primary purpose of this class is to serve as a dummy implementation
 * during testing, allowing verification of force strategy behavior without
 * relying on specific force calculation logic.
 * <p>
 * This particular force strategy implements a constant force field with
 * the vector given in the instantiation.
 * Methods:
 * - {@link #getAcceleration(Shape)}: Returns a constant acceleration
 * vector regardless of the inputs.
 */
class MockForceStrategy implements ForceStrategy {
    Vector2D force;

    MockForceStrategy(Vector2D force) {
        this.force = force;
    }

    @Override
    public Vector2D getAcceleration(Shape shape) {
        return force;
    }
}