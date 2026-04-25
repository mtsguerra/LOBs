package lob.physics.engine;

import lob.physics.Vector2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CollisionManifold class.
 *
 * This class contains test cases to validate the behavior of the CollisionManifold
 * class, including its construction, equality, and predefined constants.
 *
 * The tests ensure that the collision manifold handles cases correctly
 * where collisions occur and where there is no collision, including the validity
 * of its fields such as `isColliding`, `penetration`, `normal`, and `contactPoint`.
 */
class CollisionManifoldTest {

    /**
     * Verifies the correctness of the CollisionManifold object construction.
     *
     * This test checks whether a CollisionManifold instance is created correctly
     * with the specified properties, ensuring that:
     * - The collision status matches the expected value.
     * - The penetration depth is set to the expected value.
     * - The collision normal vector is properly assigned.
     */
    @Test
    void testCollisionManifoldConstruction() {
        var penetration = 2.5;
        Vector2D normal = new Vector2D(1, 0);
        CollisionManifold manifold = new CollisionManifold(true, penetration, normal);

        assertAll(
                () -> assertTrue(manifold.isColliding(), "collision expected"),
                () -> assertEquals(penetration, manifold.penetration() , "expected different penetration"),
                () -> assertEquals(normal, manifold.normal(),  "expected different normal")
        );
    }

    @Test
    void testNoCollisionConstant() {
        assertAll(
                () -> assertFalse(CollisionManifold.NO_COLLISION.isColliding(), "no collision expected "),
                () -> assertEquals(0, CollisionManifold.NO_COLLISION.penetration() ,"zero  penetration expected"),
                () -> assertNull(CollisionManifold.NO_COLLISION.normal(), "no normal expected")
        );
    }

    /**
     * Tests the equality behavior of the CollisionManifold class.
     *
     * This test case verifies that the CollisionManifold instances properly
     * implement equality by asserting the expected results of comparisons
     * between different instances:
     */
    @Test
    void testCollisionManifoldEquality() {
        var penetration = 2.5;
        var normal = new Vector2D(1, 0);

        CollisionManifold manifold1 = new CollisionManifold(true, penetration, normal);
        CollisionManifold manifold2 = new CollisionManifold(true, penetration, normal);
        CollisionManifold manifold3 = new CollisionManifold(false, 0, null);

        assertAll(
                () -> assertEquals(manifold1, manifold2),
                () -> assertNotEquals(manifold1, manifold3),
                () -> assertEquals(manifold3, CollisionManifold.NO_COLLISION)
        );
    }
}