package lob.physics.events;

import lob.TestData;
import lob.physics.engine.CollisionManifold;
import lob.physics.Vector2D;
import lob.physics.shapes.Circle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for verifying the behavior and implementation details of the {@code CollisionEvent} class.
 *
 * The tests in this class are designed to ensure that the {@code CollisionEvent} class adheres
 * to its intended functionality as a data encapsulation mechanism and correctly models collision events.
 * It includes tests for record implementation, null handling, and specific data access methods.
 */
class CollisionEventTest extends TestData {

    /**
     * Tests if the {@code CollisionEvent} class is a record.
     *
     * The test verifies that the {@link CollisionEvent} class is implemented as a
     * Java record, ensuring compliance with expected design and data encapsulation
     * patterns.
     *
     * The test will pass if {@code CollisionEvent.class.isRecord()} returns {@code true},
     * asserting that the class is declared as a record.
     */
    @Test
    void isRecord() {
        assertTrue(CollisionEvent.class.isRecord(), "should be a record");
    }

    /**
     * Tests that a {@code CollisionEvent} instance correctly handles and returns
     * null values when initialized with null parameters.
     *
     * This test verifies the following behaviors:
     * 1. Ensures that {@link CollisionEvent#colliding()} returns {@code null}
     *    when the {@code colliding} parameter is initialized to {@code null}.
     * 2. Ensures that {@link CollisionEvent#collided()} returns {@code null}
     *    when the {@code collided} parameter is initialized to {@code null}.
     * 3. Ensures that {@link CollisionEvent#collision()} returns {@code null}
     *    when the {@code collision} parameter is initialized to {@code null}.
     *
     * The test passes if all the above conditions are satisfied.
     */
    @Test
    void checkNulls() {
        var event = new CollisionEvent(null,null,null);

        assertAll(
                () -> assertNull(event.colliding()),
                () -> assertNull(event.collided()),
                () -> assertNull(event.collision())
        );
    }

    /**
     * Tests that the {@code colliding} method correctly identifies the colliding
     * circle within a {@link CollisionEvent}.
     *
     * The method verifies if the {@code colliding()} method of a {@link CollisionEvent}
     * instance returns the expected {@link Circle} object passed during initialization.
     *
     * @param x the x-coordinate of the circle's center
     * @param y the y-coordinate of the circle's center
     * @param radius the radius of the circle
     */
    @ParameterizedTest
    @MethodSource("doubles3")
    void colliding(double x, double y, double radius) {
        var colliding = new Circle(new Vector2D(x, y),Vector2D.NULL_VECTOR,radius,null);
        var event = new CollisionEvent(colliding,null,null);

        assertEquals(colliding, event.colliding());
    }

    /**
     * Tests that the {@code collided} method correctly identifies the collided circle
     * within a {@link CollisionEvent}.
     *
     * The method verifies if the {@code collided()} method of a {@link CollisionEvent}
     * instance returns the expected {@link Circle} object passed during initialization.
     *
     * @param x the x-coordinate of the circle's center
     * @param y the y-coordinate of the circle's center
     * @param radius the radius of the circle
     */
    @ParameterizedTest
    @MethodSource("doubles3")
    void collided(double x, double y, double radius) {
        var collided = new Circle(new Vector2D(x, y),Vector2D.NULL_VECTOR,radius,null);
        var event = new CollisionEvent(null,collided,null);

        assertEquals(collided, event.collided());
    }

    /**
     * Tests the {@code CollisionEvent} class by initializing it with a {@code CollisionManifold}
     * and verifying correctness of the collision data encapsulation.
     *
     * @param random a double value used to randomly determine if a collision occurs
     * @param x the x-component of the collision normal vector
     * @param y the y-component of the collision normal vector
     * @param penetration the penetration depth of the collision
     */
    @ParameterizedTest
    @MethodSource("doubles4")
    void collision(double random, double x, double y, double penetration) {
        boolean isCollision = random < 0.5;
        var collision = new CollisionManifold(isCollision,penetration,new Vector2D(x,y));
        var event = new CollisionEvent(null,null,collision);

        assertEquals(collision, event.collision());
    }
}