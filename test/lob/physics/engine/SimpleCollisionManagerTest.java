package lob.physics.engine;

import lob.TestData;
import lob.physics.events.MockObserver;
import lob.physics.shapes.Rectangle;
import lob.physics.shapes.Circle;
import lob.physics.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static lob.physics.Vector2D.*;
import static org.junit.jupiter.api.Assertions.*;

class SimpleCollisionManagerTest extends TestData {
    SimpleCollisionManager collisionManager;

    Circle circle = new Circle(NULL_VECTOR,NULL_VECTOR,1,null);
    Rectangle rectangle = new Rectangle(2,-1,3,1,null);
    CollisionManifold collision = null;

    @BeforeEach
    void setUp() {
        collisionManager = new SimpleCollisionManager();
    }

    @Test
    void checkCollision() {

    }

    @Nested
    class CircleCircleCollisionTest extends TestData {
        final static double X = 100;
        final static double Y = 100;
        final static double RADIUS =  10;

        final Vector2D centerA = new Vector2D(X, Y);
        final Circle circleA = new Circle(centerA,NULL_VECTOR, RADIUS,null);

        /**
         * Validates that two circles with horizontally displaced centers do not collide.
         * This method ensures through assertions that:
         * - The collision manifold calculated for "circle A on circle B" shows no collision.
         * - The collision manifold calculated for "circle B on circle A" shows no collision.
         *
         * @param dx the horizontal displacement applied to the center of the second circle
         * @param dy the vertical displacement applied to the center of the second circle
         */
        @ParameterizedTest
        @MethodSource("smallPairedVariations")
        void checkHorizontalNoCollision(double dx, double dy) {
            Vector2D centerB = new Vector2D(X + 3* RADIUS +dx, Y +dy);
            Circle circleB = new Circle(centerB,NULL_VECTOR, RADIUS,null);
            CollisionManifold collisionAonB = collisionManager.checkCollision(circleA,circleB);
            CollisionManifold collisionBonA = collisionManager.checkCollision(circleB, circleA);

            assertAll( "shouldn't be colliding",
                    () -> assertFalse(collisionAonB.isColliding()),
                    () -> assertFalse(collisionBonA.isColliding())
            );
        }

        /**
         * Validates the behavior of the collision detection system when two circles
         * are tangent to each other horizontally.
         *
         * This test verifies the collision detection logic for a scenario where two
         * circles are positioned such that they are just touching at a single point on
         * their boundaries, with their centers aligned horizontally.
         *
         * Assertions:
         * - Both collision manifolds (circle A on circle B and circle B on circle A)
         *   indicate a collision.
         * - The normal vector of the collision manifold for circle A on circle B
         *   points west (left).
         * - The normal vector of the collision manifold for circle B on circle A
         *   points east (right).
         * - The penetration depth for both collision manifolds is zero, within an allowed epsilon margin.
         *
         * Small variations cannot be applied in this case since they may change collision status.
         */
        @Test
        void checkHorizontalTangentCollision() {
            Vector2D centerB = new Vector2D(X + 2* RADIUS, Y);
            Circle circleB = new Circle(centerB,NULL_VECTOR, RADIUS,null);
            CollisionManifold collisionAonB = collisionManager.checkCollision(circleA,circleB);
            CollisionManifold collisionBonA = collisionManager.checkCollision(circleB, circleA);
            // Vector2D contactPointOnA = centerA.add(EAST.multiply(radius));
            // Vector2D contactPointOnB = centerB.add(WEST.multiply(radius));

            assertAll(
                    () -> assertTrue(collisionAonB.isColliding(), "should be colliding"),
                    () -> assertTrue(collisionBonA.isColliding(), "should be colliding"),
                    () -> assertEquals(WEST,collisionAonB.normal(), "should point west (left)"),
                    () -> assertEquals(EAST,collisionBonA.normal(), "should point east (right)"),
                    // () -> assertEquals( contactPointOnB, collisionAonB.contactPoint()),
                    // () -> assertEquals( contactPointOnA, collisionBonA.contactPoint()),
                    () -> assertEquals(0, collisionAonB.penetration(),EPSILON),
                    () -> assertEquals(0, collisionBonA.penetration(),EPSILON)
            );
        }

        @ParameterizedTest
        @MethodSource("smallPairedVariations")
        void checkHorizontalHalfRadiusPenetration(double dx, double dy) {
            Vector2D centerB = new Vector2D(X +1.5* RADIUS +dx, Y +dy);
            Circle circleB = new Circle(centerB,NULL_VECTOR, RADIUS,null);
            CollisionManifold collisionAonB = collisionManager.checkCollision(circleA,circleB);
            CollisionManifold collisionBonA = collisionManager.checkCollision(circleB, circleA);
            // Vector2D contactPointOnA = centerA.add(EAST.multiply(radius));
            // Vector2D contactPointOnB = centerB.add(WEST.multiply(radius));

            assertAll(
                    () -> assertTrue(collisionAonB.isColliding(), "should be colliding"),
                    () -> assertTrue(collisionBonA.isColliding(), "should be colliding"),
                    () -> assertEquals(WEST.x(),collisionAonB.normal().x(), EPSILON, "should point west (left) - x coordinate"),
                    () -> assertEquals(WEST.y(),collisionAonB.normal().y(), EPSILON, "should point west (left) - y"),
                    () -> assertEquals(EAST.x(),collisionBonA.normal().x(), EPSILON, "should point east (right) - x"),
                    () -> assertEquals(EAST.y(),collisionBonA.normal().y(), EPSILON, "should point east (right) - y"),
                    // () -> assertEquals( contactPointOnB, collisionAonB.contactPoint()),
                    // () -> assertEquals( contactPointOnA, collisionBonA.contactPoint()),
                    () -> assertEquals(RADIUS /2, collisionAonB.penetration(),EPSILON),
                    () -> assertEquals(RADIUS /2, collisionBonA.penetration(),EPSILON)
            );
        }


        @ParameterizedTest
        @MethodSource("smallPairedVariations")
        void checkHorizontalFullRadiusPenetration(double dx, double dy) {
            Vector2D centerB = new Vector2D(X + RADIUS +dx, Y +dy);
            Circle circleB = new Circle(centerB,NULL_VECTOR, RADIUS,null);
            CollisionManifold collisionAonB = collisionManager.checkCollision(circleA,circleB);
            CollisionManifold collisionBonA = collisionManager.checkCollision(circleB, circleA);
            // Vector2D contactPointOnA = centerA.add(EAST.multiply(radius));
            // Vector2D contactPointOnB = centerB.add(WEST.multiply(radius));

            assertAll(
                    () -> assertTrue(collisionAonB.isColliding(), "should be colliding"),
                    () -> assertTrue(collisionBonA.isColliding(), "should be colliding"),
                    () -> assertEquals(WEST.x(),collisionAonB.normal().x(), EPSILON, "should point west (left) - x coordinate"),
                    () -> assertEquals(WEST.y(),collisionAonB.normal().y(), EPSILON, "should point west (left) - y"),
                    () -> assertEquals(EAST.x(),collisionBonA.normal().x(), EPSILON, "should point east (right) - x"),
                    () -> assertEquals(EAST.y(),collisionBonA.normal().y(), EPSILON, "should point east (right) - y"),
                    // () -> assertEquals( contactPointOnB, collisionAonB.contactPoint()),
                    // () -> assertEquals( contactPointOnA, collisionBonA.contactPoint()),
                    () -> assertEquals(RADIUS, collisionAonB.penetration(),EPSILON),
                    () -> assertEquals(RADIUS, collisionBonA.penetration(),EPSILON)
            );
        }

        @ParameterizedTest
        @MethodSource("smallPairedVariations")
        void checkHorizontalDeepPenetration(double dx, double dy) {
            Vector2D centerB = new Vector2D(X + RADIUS /2+dx, Y +dy);
            Circle circleB = new Circle(centerB,NULL_VECTOR, RADIUS,null);
            CollisionManifold collisionAonB = collisionManager.checkCollision(circleA,circleB);
            CollisionManifold collisionBonA = collisionManager.checkCollision(circleB, circleA);
            // Vector2D contactPointOnA = centerA.add(EAST.multiply(radius));
            // Vector2D contactPointOnB = centerB.add(WEST.multiply(radius));

            assertAll(
                    () -> assertTrue(collisionAonB.isColliding(), "should be colliding"),
                    () -> assertTrue(collisionBonA.isColliding(), "should be colliding"),
                    () -> assertEquals(WEST.x(),collisionAonB.normal().x(), EPSILON, "should point west (left) - x coordinate"),
                    () -> assertEquals(WEST.y(),collisionAonB.normal().y(), EPSILON, "should point west (left) - y"),
                    () -> assertEquals(EAST.x(),collisionBonA.normal().x(), EPSILON, "should point east (right) - x"),
                    () -> assertEquals(EAST.y(),collisionBonA.normal().y(), EPSILON, "should point east (right) - y"),
                    // () -> assertEquals( contactPointOnB, collisionAonB.contactPoint()),
                    // () -> assertEquals( contactPointOnA, collisionBonA.contactPoint()),
                    () -> assertEquals(1.5 * RADIUS, collisionAonB.penetration(),EPSILON),
                    () -> assertEquals(1.5 * RADIUS, collisionBonA.penetration(),EPSILON)
            );
        }

        /**
         * Validates the behavior of the collision detection system when two circles overlap,
         * perfectly or with a small variation.
         *
         * This method tests the scenario where two circles have centers aligned horizontally
         * and overlapping in such a way that their distance is less than the sum of their radii.
         * The test ensures that:
         * - Both collision manifolds (circle A on circle B and vice versa) indicate a collision.
         * - The normals from the two collision manifolds are in opposite directions.
         * - The penetration depth for both collisions matches the expected value, which is twice the radius of the circles.
         *
         * Assertions:
         * - The `isColliding` value of both collision manifolds is true.
         * - The normalized inner product of the normals from the two collision manifolds equals -1 (opposite directions).
         * - The penetration depth for each collision manifold equals `2 * radius`, within an allowed epsilon margin.
         */
        @ParameterizedTest
        @MethodSource("smallPairedVariations")
        void checkHorizontalOverlap(double dx, double dy) {
            Vector2D centerB = new Vector2D(X +dx, Y +dy);
            Circle circleB = new Circle(centerB,NULL_VECTOR, RADIUS,null);
            CollisionManifold collisionAonB = collisionManager.checkCollision(circleA,circleB);
            CollisionManifold collisionBonA = collisionManager.checkCollision(circleB, circleA);

            assertAll(
                    () -> assertTrue(collisionAonB.isColliding(), "should be colliding"),
                    () -> assertTrue(collisionBonA.isColliding(), "should be colliding"),
                    () -> assertEquals(-1,  collisionAonB.normal().innerProduct(collisionBonA.normal()),
                            "normals must have opposition directions:"+
                                    "A . B = ||A|| ||B|| cos(A^B) = 1 x 1 x cos(PI) = -1"),
                    () -> assertEquals(2 * RADIUS, collisionAonB.penetration(),EPSILON),
                    () -> assertEquals(2 * RADIUS, collisionBonA.penetration(),EPSILON)
                    /* TODO ignore contact point in manifold
                    ,() -> {
                        var p = collisionAonB.contactPoint();
                        assertEquals(radius * radius, Math.pow(p.x() - x0,2) + Math.pow(p.y() - y,2),EPSILON,
                                "contact point should be on the circumference of B");
                    },
                    () -> {
                        var p = collisionBonA.contactPoint();
                        assertEquals(radius * radius, Math.pow(p.x() - x0,2) + Math.pow(p.y() - y,2),EPSILON,
                                "contact point should be on the circumference of B");
                    }
                     */
            );
        }

    }


    @Nested
    class CircleRectangleCollisionTest extends TestData {
        Rectangle rectangle;
        double radius = 1;

        @BeforeEach
        void setUp() {
            rectangle = new Rectangle(-radius,-radius,radius,radius,null);
        }


        @ParameterizedTest
        @MethodSource("directions")
        void checkNoCollision(String directionName, double dx, double dy) {
            final double gap = 3;
            final Vector2D center = new Vector2D(dx * gap , dy * gap );
            final Circle circle = new Circle(center,NULL_VECTOR,radius,null);

            final CollisionManifold collision = collisionManager.checkCollision(circle,rectangle);

            assertFalse(collision.isColliding(), "shouldn't be colliding from "+directionName);
        }

        @ParameterizedTest
        @MethodSource("directions")
        void checkTangentCollision(String directionName, double dx, double dy) {
            final double gap = (dx == 0 || dy == 0) ? 2 : 1 + Math.sqrt(0.5);
            final Vector2D center = new Vector2D(dx * gap , dy * gap );
            final Circle circle = new Circle(center,NULL_VECTOR,radius,null);
            
            final CollisionManifold collision = collisionManager.checkCollision(circle,rectangle);

            
            assertAll(
                    () -> assertTrue(collision.isColliding(),
                            "should be colliding"),
                    () -> assertEquals( new Vector2D(dx,dy).normalize(), collision.normal(),
                            "expected "+directionName+" as normal"),
                    () -> assertEquals( 0, collision.penetration() , EPSILON,
                            "no penetration expected")
            );
        }


        @ParameterizedTest
        @MethodSource("directions")
        void checkPenetrationCollision(String directionName, double dx, double dy) {
            final double gap = (dx == 0 || dy == 0) ? 1 : Math.sqrt(0.5);
            final Vector2D center = new Vector2D(dx * gap , dy * gap );
            final Circle circle = new Circle(center,NULL_VECTOR,radius,null);

            final CollisionManifold collision = collisionManager.checkCollision(circle,rectangle);

            assertAll(
                    () -> assertTrue(collision.isColliding(),
                            "should be colliding"),
                    () -> assertEquals( new Vector2D(dx,dy).normalize(), collision.normal(),
                            "expected "+directionName+" as normal"),
                    () -> assertEquals( radius, collision.penetration() , EPSILON,
                            "penetration expected")
            );
        }

    }

    @Nested
    class RectangleRectangleCollisionTest extends TestData {
        final static double X = 100;
        final static double Y = 100;
        final static double WIDTH = 100;
        final static double HEIGHT = 100;

        Rectangle colliding = new Rectangle(X,Y,X+WIDTH,Y+HEIGHT,null);

        @ParameterizedTest
        @MethodSource("smallPairedVariations")
        void checkHorizontalNoCollision(double dx, double dy) {
            Rectangle collided = new Rectangle(X+2*WIDTH+dx,Y+dx,X+3*WIDTH+dx,Y+HEIGHT+dy,null);

            final CollisionManifold collision = collisionManager.checkCollision(colliding,collided);

            assertFalse(collision.isColliding(), "shouldn't be colliding from "+dx+" "+dy);
        }

        @ParameterizedTest
        @MethodSource("smallVariations")
        void checkHorizontalTangentCollision(double dy) {
            Rectangle collided = new Rectangle(X+WIDTH,Y+dy,X+2*WIDTH,Y+HEIGHT+dy,null);

            final CollisionManifold collision = collisionManager.checkCollision(colliding,collided);

            assertTrue(collision.isColliding(), "shouldn't be colliding");
        }

        @ParameterizedTest
        @MethodSource("smallVariations")
        void checkHorizontalTangentCollision2(double dy) {
            Rectangle collided = new Rectangle(X+WIDTH,Y+0.5*HEIGHT,X+2*WIDTH,Y+1.5*HEIGHT+dy,null);

            final CollisionManifold collision = collisionManager.checkCollision(colliding,collided);

            assertTrue(collision.isColliding(), "shouldn't be colliding from  "+dy);
        }

        @ParameterizedTest
        @MethodSource("smallPairedVariations")
        void checkHorizontalTangentCollision3(double dx, double dy) {
            Rectangle collided = new Rectangle(X+WIDTH,Y+0.75*HEIGHT,X+2*WIDTH,Y+1.75*HEIGHT+dy,null);

            final CollisionManifold collision = collisionManager.checkCollision(colliding,collided);

            assertTrue(collision.isColliding(), "shouldn't be colliding from "+dx+" "+dy);
        }

    }

    /**
     * A nested test class responsible for testing various collision resolution scenarios
     * between geometric shapes such as rectangles and circles. This class extends
     * {@code TestData} to provide necessary test utilities.
     */
    @Nested
    class ResolveCollisionTest extends TestData{
        Rectangle brick = new Rectangle( 1,-1,2,1,null);

        /**
         * Resolves a collision between two overlapping rectangles by adjusting their state based on the
         * provided collision manifold. This test ensures that the collision of rectangles
         * is ignored.
         *
         * @param xMin the x-coordinate of the lower-left corner of the rectangle
         * @param yMin the y-coordinate of the lower-left corner of the rectangle
         * @param width the width of the rectangle
         * @param height the height of the rectangle
         */
        @ParameterizedTest
        @MethodSource("doubles4")
        void resolveCollisionRectangle(double xMin,double yMin, double width, double height) {
            var rectangle = new Rectangle(xMin,yMin,xMin+width,yMin+height,null);
            var penetration = Math.min(width,height);
            var collision = new CollisionManifold(true, penetration, NORTH);

            assertEquals( rectangle, collisionManager.resolveCollision(rectangle,rectangle,collision));
        }

        /**
         * Resolves a collision between a circle and another shape (e.g., a rectangle) by adjusting the circle's
         * position and velocity based on the given collision manifold. The method calculates the resultant
         * circle state post-collision, ensuring it no longer overlaps with the other shape and its velocity is modified
         * to simulate the collision physics.
         *
         * @param penetration the depth of penetration of the circle into the collision shape, must be non-negative
         */
        @ParameterizedTest
        @MethodSource("doubles1")
        void resolveCircleCollisionWithPenetration(double penetration) {
            var x=penetration;
            var radius = 1;
            var ball = new Circle( new Vector2D( x, 0),new Vector2D(1,0),radius,null);

            var collision = new CollisionManifold(true,penetration,new Vector2D(-1,0));

            var expected = new Circle(new Vector2D(0,0), new Vector2D(-1,0),radius,null);
            assertEquals(expected, collisionManager/**/.resolveCircleCollision( ball, brick, collision));
        }

        /**
         * Resolves a collision between a circle and another shape by reflecting
         * the circle's velocity based on the collision normal. This method ensures
         * that the circle's position remains unchanged, and that the velocity of
         * the circle is adjusted to simulate a reflection along the normal of the
         * collision. The x-component of the velocity is inverted upon reflection,
         * while the y-component remains preserved.
         *
         * @param angle the angle of the circle's velocity vector in radians,
         *              used to determine its initial direction before collision
         */
        @ParameterizedTest
        @MethodSource("doubles1")
        void resolveCircleCollisionWithReflection(double angle) {
            var x = Math.cos(angle);
            var y = Math.sin(angle);
            var radius = 1;
            var ball = new Circle( NULL_VECTOR,new Vector2D(x,y),radius,null);

            var collision = new CollisionManifold(true,0,new Vector2D(-1,0));

            var resolved =  collisionManager.resolveCircleCollision( ball, brick, collision);

            if(resolved instanceof Circle resolvedCircle ) {
                var resolvedCircleVelocity = resolvedCircle.velocity();
                assertAll(
                        () -> assertEquals( NULL_VECTOR, resolvedCircle.position(),"should not move"),
                        () -> assertEquals( radius, resolvedCircle.radius(),"should keep radius"),
                        () -> assertEquals(-x,resolvedCircleVelocity.x(), EPSILON,
                                "should reflect x component"),
                        () -> assertEquals(y, resolvedCircleVelocity.y(), EPSILON,
                                "should preserve y component")
                );
            } else
                fail("Unexpected result"+resolved);
        }

        /**
         * Resolves a collision between a moving circle and another shape (e.g., a rectangle) by applying
         * reflection and restitution to the circle's velocity. The circle's resulting velocity is adjusted based
         * on the restitution coefficient to simulate energy loss due to the collision, while the position and
         * radius of the circle remain unchanged. The method ensures that the x-component of the velocity is
         * modified according to the restitution, while the y-component is preserved.
         *
         * @param angle the angle of the circle's initial velocity vector in radians, used to determine its
         *              direction of motion before the collision
         * @param restitution the coefficient of restitution, a value between 0 and 1 indicating the
         *                    elasticity of the collision; a value of 1 represents a perfectly elastic
         *                    collision, while a value of 0 represents a perfectly inelastic collision
         */
        @ParameterizedTest
        @MethodSource("doubles2")
        void resolveCircleCollisionWithReflectionAndRestitution(double angle,double restitution) {
            var x = Math.cos(angle);
            var y = Math.sin(angle);
            var radius = 1;
            var ball = new Circle( new Vector2D( 0, 0),new Vector2D(x,y),radius,null);
            var collision = new CollisionManifold(true,0,new Vector2D(-1,0));

            collisionManager.setRestitution(restitution);

            var resolved = collisionManager.resolveCircleCollision( ball, brick, collision);

            if(resolved instanceof Circle resolvedCircle ) {
                var resolvedCircleVelocity = resolvedCircle.velocity();
                assertAll(
                        () -> assertEquals( NULL_VECTOR, resolvedCircle.position(),"should not move"),
                        () -> assertEquals( radius, resolvedCircle.radius(),"should keep radius"),
                        () -> assertEquals(-x * restitution,resolvedCircleVelocity.x(), EPSILON,
                                "restitution should affect x component"),
                        () -> assertEquals(y, resolvedCircleVelocity.y(), EPSILON,
                                "restitution should not affect y component")
                );
            } else
                fail("Unexpected result"+resolved);
        }

    }

    /**
     * Unit tests for the CollisionListener functionality within the context of collision management.
     *
     * This test suite contains nested test cases that validate the behavior of adding, notifying,
     * and removing observers for collision events associated with specific shapes in a physics simulation.
     *
     * The tests are designed to ensure that:
     * - Observers can be added to monitor events for specific shapes.
     * - Observers are correctly notified when collision events occur for their registered shapes.
     * - Observers can be removed, and their notifications cease as expected.
     * - The system supports multiple observers for a single shape and handles their notifications correctly.
     */
    @Nested
    class CollisionListenerTest {
        MockObserver observer1;
        MockObserver observer2;


        @BeforeEach
        void setUp() {
            observer1 = new MockObserver();
            observer2 = new MockObserver();

        }

        /**
         * Tests the functionality of adding observers to monitor collision events
         * for a specific shape and verifies that they are notified correctly.

         * The test ensures that:
         * - Observers can be added to monitor specific shapes.
         * - Observers are notified in response to collision events for the registered shape.
         * - Adding multiple observers to the same shape functions correctly, and notifications are sent as expected.
         *
         * Validates*/
        @Test
        void testAddObserver() {
            assertEquals( 0, observer1.getCount());
            assertEquals( 0, observer2.getCount());

            collisionManager.addCollisionListener(rectangle,observer1);

            collisionManager.notifyCollision(circle,rectangle,collision);

            assertEquals( 1, observer1.getCount());
            assertEquals( 0, observer2.getCount());

            collisionManager.addCollisionListener(rectangle,observer2);

            collisionManager.notifyCollision(circle,rectangle,collision);

            assertEquals( 2, observer1.getCount());
            assertEquals( 1, observer2.getCount());
        }


        /**
         * Tests the functionality of removing observers from monitoring collision events
         * for a specific shape and verifies the behavior after an observer has been removed.
         *
         * This test ensures that:
         * - Observers can be correctly removed from monitoring specific shapes.
         * - Once removed, an observer no longer receives notifications of collision events.
         * - Observers that remain registered continue to be notified as expected.
         */
        @Test
        void testRemoveObserver() {
            assertEquals( 0, observer1.getCount());
            assertEquals( 0, observer2.getCount());

            collisionManager.addCollisionListener(rectangle,observer1);
            collisionManager.addCollisionListener(rectangle,observer2);

            collisionManager.notifyCollision(circle,rectangle,collision);

            assertEquals( 1, observer1.getCount());
            assertEquals( 1, observer2.getCount());

            collisionManager.removeCollisionListener(rectangle,observer1);

            collisionManager.notifyCollision(circle,rectangle,collision);

            assertEquals( 1, observer1.getCount());
            assertEquals( 2, observer2.getCount());
        }

        /**
         * Tests the behavior of notifying all registered observers of collision events and
         * validates the functionality of adding and removing collision listeners for specific shapes.
         *
         * This test ensures that:
         * - Observers can be added to monitor collision events for a specific shape.
         * - Observers are notified when a collision event occurs for their registered shape.
         * - Multiple observers can be registered for the same shape and receive notifications as expected.
         * - Observers are no longer notified after being removed through the removal of all collision listeners.
         */
        @Test
        void testAllObservers() {
            assertEquals( 0, observer1.getCount());
            assertEquals( 0, observer2.getCount());

            collisionManager.addCollisionListener(rectangle,observer1);
            collisionManager.addCollisionListener(rectangle,observer2);

            collisionManager.notifyCollision(circle,rectangle,collision);

            assertEquals( 1, observer1.getCount());
            assertEquals( 1, observer2.getCount());

            collisionManager.removeAllCollisionListeners(rectangle);

            collisionManager.notifyCollision(circle,rectangle,collision);

            assertEquals( 1, observer1.getCount());
            assertEquals( 1, observer2.getCount());
        }
    }

    /**
     * Test class for validating restitution behavior in the simple collision manager.
     *
     * The restitution coefficient is a property that determines
     * how elastic collisions are. A restitution coefficient of 1.0 represents a fully elastic
     * collision with no energy loss, while values closer to 0 indicate inelastic collisions.
     */
    @Nested
    class RestitutionTest extends TestData {

        /**
         * Validates that the default restitution coefficient of the simple collision manager
         * is set to 1.0, which indicates perfectly elastic collisions.
         *
         * The test asserts that the initial restitution value of the simple collision manager
         * retrieved via {@code getRestitution()} is equal to 1.0. A restitution value
         * of 1.0 ensures no energy loss during collisions.
         */
        @Test
        void getDefaultRestitution() {
            assertEquals(1.0 , collisionManager.getRestitution() ,
                    "default restitution should be 1.0 (totally elastic)");
        }

        /**
         * Tests that the restitution coefficient of the simple collision manager can be set and
         * retrieved correctly.
         *
         * @param restitution the restitution coefficient to set in the physics world.
         *                     This value must be between 0 and 1, inclusive.
         */
        @ParameterizedTest
        @MethodSource("doubles1")
        void getAndSetRestitution(double restitution) {
            collisionManager.setRestitution(restitution);

            assertEquals( restitution, collisionManager.getRestitution() );
        }

        @ParameterizedTest
        @MethodSource("doubles1")
        void setRestitutionTooLarge(double restitution) {
            var restitutionTooLarge = 1D + restitution;

            assertThrows(IllegalArgumentException.class,
                    () -> collisionManager.setRestitution(restitutionTooLarge),
                    "argument larger than 1");
        }

        @ParameterizedTest
        @MethodSource("doubles1")
        void setRestitutionTooSmall(double restitution) {
            var restitutionTooSmall = - restitution;

            assertThrows(IllegalArgumentException.class,
                    () -> collisionManager.setRestitution(restitutionTooSmall),
                    "argument smaller than 1");
        }
    }

}