package lob.physics.engine;

import lob.TestData;
import lob.physics.events.CollisionEvent;
import lob.physics.events.MockObserver;
import lob.physics.events.PhysicsObserver;
import lob.physics.forces.ForceStrategy;
import lob.physics.forces.GravityStrategy;
import lob.physics.forces.NoForceStrategy;
import lob.physics.shapes.Circle;
import lob.physics.shapes.Rectangle;
import lob.physics.shapes.Shape;
import lob.physics.Vector2D;
import lob.quadtree.PointQuadtree;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static lob.physics.Vector2D.NULL_VECTOR;
import static org.junit.jupiter.api.Assertions.*;

class PhysicsWorldTest extends TestData {
    PhysicsWorld physicsWorld;
    Circle circle = new Circle(NULL_VECTOR,NULL_VECTOR,1,null);
    Rectangle rectangle = new Rectangle(2,-1,3,1,null);
    Rectangle boundary  = new Rectangle(-1,-1,1,1,null);
    Rectangle anotherRectangle = new Rectangle(0,10,10,20,null);

    @BeforeEach
    void setUp() {
        physicsWorld = new PhysicsWorld(WIDTH,HEIGHT);
    }

    /**
     * Tests on static property margin
     */
    @Nested
    class MarginTest extends TestData {

        /**
         * The default margin should be 100
         */
        @Test
        @Order(1)
        void testGetMarginDefault() {
            assertEquals(100.0D,  PhysicsWorld.getMargin(), "unexpected default margin");
        }

        /**
         * Check that the set margin is correctly retrieved.
         *
         * @param margin
         */
        @ParameterizedTest
        @MethodSource("doubles1")
        void testGetMargin(double margin) {
            PhysicsWorld.setMargin(margin);

            assertEquals(margin, PhysicsWorld.getMargin(), "unexpected margin");
        }

        /**
         * Setting a margin with a negative value must raise an exception
         */
        @Test
        void testSetMarginInvalid() {

            assertThrows(IllegalArgumentException.class, () -> PhysicsWorld.setMargin(-1),
                    "margin must be >= 0, exception expected");

        }

    }


    /**
     * Test class for verifying the behavior and functionality of the CollisionManager
     * within the physics world system using unit tests.
     *
     * This test suite includes tests for:
     * - Validating the default implementation of the CollisionManager.
     * - Ensuring that the physics world allows replacing the collision manager
     *   with a custom implementation.
     *
     * The CollisionManager is responsible for managing collision detection and resolution logic
     * within the physics system. Proper functionality of this component is crucial for accurate
     * physics simulation and event handling.
     */
    @Nested
    class CollisionManagerTest {

        class MockCollisionManager implements CollisionManager {

            @Override
            public CollisionManifold checkCollision(Shape s1, Shape s2) {
                return null;
            }

            @Override
            public Shape resolveCollision(Shape colliding, Shape collided, CollisionManifold collision) {
                return null;
            }

            @Override
            public void addCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer) {

            }

            @Override
            public void removeCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer) {

            }

            @Override
            public void removeAllCollisionListeners(Shape shape) {

            }

            @Override
            public double getRestitution() {
                return 0;
            }

            @Override
            public void setRestitution(double restitution) {

            }
        }

        /**
         * Verifies the default behavior of the {@code getCollisionManager} method in the physics world system.
         *
         * This test ensures that the collision manager retrieved from the physics world:
         * - Is not {@code null}.
         * - Is an instance of the default implementation, {@code SimpleCollisionManager}.
         *
         * The collision manager is responsible for handling collision detection and response
         * in the physics system, and this test validates that the default configuration
         * provides the expected behavior.
         */
        @Test
        @Order(1)
        void testGetCollisionManagerDefault() {

            var collisionManager = physicsWorld.getCollisionManager();

            assertNotNull(collisionManager);
            assertTrue( collisionManager instanceof SimpleCollisionManager, "unexpected collision manager type");

        }

        /**
         * Tests the functionality of the {@code setCollisionManager} method in the physics world system.
         *
         * This test verifies that the collision manager within the physics world can be updated to a custom
         * collision manager implementation. The test ensures that:
         * - The collision manager is properly set to the new instance.
         * - The type of the collision manager reflects the change, allowing runtime polymorphism
         *   with a custom implementation.
         *
         * The collision manager is a critical component responsible for handling collision detection
         * and response logic. This test asserts that the physics world delegates this responsibility
         * accurately when a new collision manager is supplied.
         */
        @Test
        void setCollisionManager() {
            var otherCollisionManager = new MockCollisionManager();

            physicsWorld.setCollisionManager(otherCollisionManager);

            assertTrue( physicsWorld.getCollisionManager() instanceof MockCollisionManager, "unexpected collision manager type");
        }
    }

    /**
     * A test class designed to verify the behavior of the ForceStrategy
     * functionality within a physics world. This class contains nested tests
     * that ensure the correct default force strategy is returned and that
     * custom force strategies can be set and retrieved properly.
     */
    @Nested
    class ForceStrategyTest {
        static final Vector2D DUMMY = new Vector2D(1, 1);

        /**
         * Tests that the default force strategy in the physics world is correctly returned
         * and behaves as expected. The default force strategy should apply no force,
         * meaning it should always return a null or zero acceleration vector.
         */
        @Test
        void getDefaultForceStrategy() {
            var strategy = physicsWorld.getForceStrategy();

            assertEquals(NULL_VECTOR, strategy.getAcceleration(null ),
                    "default force strategy should be No Force");
        }

        /**
         * Tests the functionality of setting and retrieving a custom force strategy
         * in the physics world. This method ensures that after setting a specific
         * force strategy, the `getForceStrategy` method correctly returns the same
         * instance, and the strategy behaves as expected.
         */
        @Test
        void setGetForceStrategy() {
            var strategy = new MockForceStrategy(DUMMY);

            physicsWorld.setForceStrategy(strategy);

            Assertions.assertEquals(strategy, physicsWorld.getForceStrategy());

            assertEquals(DUMMY, strategy.getAcceleration(null));
        }
    }


    /**
     * Test class for validating restitution behavior in the physics simulation world.
     *
     * The restitution coefficient is a property of the physics world that determines
     * how elastic collisions are. A restitution coefficient of 1.0 represents a fully elastic
     * collision with no energy loss, while values closer to 0 indicate inelastic collisions.
     */
    @Nested
    class RestitutionTest extends TestData {

        /**
         * Validates that the default restitution coefficient of the physics world
         * is set to 1.0, which indicates perfectly elastic collisions.
         *
         * The test asserts that the initial restitution value of the physics world
         * retrieved via {@code getRestitution()} is equal to 1.0. A restitution value
         * of 1.0 ensures no energy loss during collisions.
         */
        @Test
        void getDefaultRestitution() {
            assertEquals(1.0 , physicsWorld.getRestitution() ,
                    "default restitution should be 1.0 (totally elastic)");
        }

        /**
         * Tests that the restitution coefficient of the physics world can be set and
         * retrieved correctly.
         *
         * @param restitution the restitution coefficient to set in the physics world.
         *                     This value must be between 0 and 1, inclusive.
         */
        @ParameterizedTest
        @MethodSource("doubles1")
        void getAndSetRestitution(double restitution) {
            physicsWorld.setRestitution(restitution);

            assertEquals( restitution, physicsWorld.getRestitution() );
        }

        @ParameterizedTest
        @MethodSource("doubles1")
        void setRestitutionTooLarge(double restitution) {
            var restitutionTooLarge = 1D + restitution;

            assertThrows(IllegalArgumentException.class,
                    () -> physicsWorld.setRestitution(restitutionTooLarge),
                    "argument larger than 1");
        }

        @ParameterizedTest
        @MethodSource("doubles1")
        void setRestitutionTooSmall(double restitution) {
            var restitutionTooSmall = - restitution;

            assertThrows(IllegalArgumentException.class,
                    () -> physicsWorld.setRestitution(restitutionTooSmall),
                    "argument smaller than 1");
        }
    }

    @Nested
    class  UpdateTest {

        /**
         * Validates the behavior of a circle's position and velocity after applying an acceleration
         * over a time interval (dt) in a physics simulation. Verifies that the circle is correctly
         * updated within the physics world and matches the expected state.
         *
         * @param circle the circle to be tested, whose position and velocity are updated during the simulation
         * @param acceleration the vector representing the acceleration applied to the circle
         * @param dt the time interval over which the simulation updates the circle's state
         */
        void checkCircleChange(Circle circle, Vector2D acceleration,double dt) {

            physicsWorld.addShape(circle);

            physicsWorld.update(dt);

            var moved = getSingleCircle();

            assertEquals(circle.appearance(), moved.appearance(), "unexpected apearence");
            assertEquals(circle.radius(), moved.radius(), "unexpected radius");

            var expectedVelocity = circle.velocity().add( acceleration.multiply(dt) );
            var expectedPosition = circle.position().add( expectedVelocity.multiply(dt));

            assertEquals(expectedPosition, moved.position(), "unexpected position");
            assertEquals(expectedVelocity, moved.velocity(), "unexpected velocity");

        }

        /**
         * The UpdateNoForcesDefault class provides unit tests to verify the behavior
         * of Circle objects when no forces are applied. It uses parameterized tests
         * with a variety of input data to ensure the correctness of simulation under specific conditions.
         */
        @Nested
        class UpdateNoForcesDefault extends TestData {

            @ParameterizedTest
            @MethodSource("doubles1")
            void stationaryCircle(double dt) {
                checkCircleChange(circle, NULL_VECTOR, dt);
            }

            @ParameterizedTest
            @MethodSource("doubles4")
            void movingCircle(double x, double y, double radius, double dt) {
                var velocity = new Vector2D(x, y);
                var circle = new Circle(NULL_VECTOR, velocity, radius, null);

                checkCircleChange(circle, NULL_VECTOR, dt);
            }
        }


        @Nested
        class UpdateNoForces extends TestData {

            @BeforeEach
            void setUp() {
                physicsWorld.setForceStrategy(new NoForceStrategy());
            }

            @ParameterizedTest
            @MethodSource("doubles1")
            void stationaryCircle(double dt) {
                checkCircleChange(circle, NULL_VECTOR, dt);
            }

            @ParameterizedTest
            @MethodSource("doubles4")
            void movingCircle(double x, double y, double radius, double dt) {
                var velocity = new Vector2D(x, y);
                var circle = new Circle(NULL_VECTOR, velocity, radius, null);

                checkCircleChange(circle, NULL_VECTOR, dt);
            }
        }

        @Nested
        class UpdateWithGravity extends TestData {
            private final static Vector2D GRAVITY = new Vector2D(0,9.8);

            @BeforeEach
            void setUp() {
                physicsWorld.setForceStrategy(new GravityStrategy());
            }

            @ParameterizedTest
            @MethodSource("doubles1")
            void stationaryCircle(double dt) {
                checkCircleChange(circle, GRAVITY, dt);
            }

            @ParameterizedTest
            @MethodSource("doubles4")
            void movingCircle(double x, double y, double radius, double dt) {
                var velocity = new Vector2D(x, y);
                var circle = new Circle(NULL_VECTOR, velocity, radius, null);

                checkCircleChange(circle, GRAVITY, dt);
            }
        }

        @Nested
        class UpdateWestWind extends TestData {
            private final static Vector2D WEST_WIND = new Vector2D(10,0);

            @BeforeEach
            void setUp() {
                physicsWorld.setForceStrategy(new ForceStrategy(

                ) {
                    @Override
                    public Vector2D getAcceleration(Shape shape) {
                        return WEST_WIND;
                    }
                });
            }

            @ParameterizedTest
            @MethodSource("doubles1")
            void stationaryCircle(double dt) {
                checkCircleChange(circle, WEST_WIND, dt);
            }

            @ParameterizedTest
            @MethodSource("doubles4")
            void movingCircle(double x, double y, double radius, double dt) {
                var velocity = new Vector2D(x, y);
                var circle = new Circle(NULL_VECTOR, velocity, radius, null);

                checkCircleChange(circle, WEST_WIND, dt);
            }
        }

    }


    /**
     * Tests the behavior of the `PhysicsWorld` iterator method by verifying
     * that shapes added to the `physicsWorld` can be correctly iterated over.
     *
     * The test first ensures that no shapes are present initially. Then, shapes
     * are added to the physics world, and the iterator is validated by collecting
     * the shapes into a list and asserting their count.
     */
    @Test
    void iterator() {

        assertEquals( 0 , countShapes() , "no shapes added");

        physicsWorld.addShape(circle);

        assertEquals( 1 , countShapes() ,"one shape added");

        physicsWorld.addShape(rectangle);

        assertEquals( 2 , countShapes() ,"another shape added");
    }



    /**
     * Tests the `reset` method of the `PhysicsWorld` class to ensure that all shapes
     * are properly removed from the physics world when the method is invoked.
     *
     * The test begins by adding multiple shapes (`circle` and `rectangle`) to the
     * `physicsWorld`. The count of shapes is verified to ensure they have been correctly
     * added. Once the `reset` method is called, the count of shapes is checked again to
     * confirm that all shapes have been cleared from the physics world.
     *
     * Verifies the functionality and correctness of the `reset` method in managing
     * the state of the physics world and its shapes.
     */
    @Test
    void reset() {
        physicsWorld.addShape(circle);
        physicsWorld.addShape(rectangle);

        assertEquals( 2 , countShapes() );

        physicsWorld.reset();

        assertEquals( 0 , countShapes() );

    }


    @Nested
    class AddShapeTest {

        @Test
        void addShapeNull() {
            assertThrows(NullPointerException.class,
                    () -> physicsWorld.addShape( null ));
        }

        /**
         * Tests the `addShape` method of the `PhysicsWorld` class, ensuring that
         * shapes are properly added to the physics world and accurately reflected in
         * the count of shapes.
         *
         * This test case starts by verifying that the physics world is initially empty.
         * Afterward, it adds a circle and confirms that the shape count increases
         * accordingly. The test then adds a rectangle and verifies that the total count
         * reflects both shapes. The method demonstrates that the `addShape` functionality
         * correctly integrates new shapes into the physics world.
         */
        @Test
        void addShape() {
            assertEquals( 0 , countShapes() );

            physicsWorld.addShape(circle);

            assertEquals( 1 , countShapes() );

            physicsWorld.addShape(rectangle);

            assertEquals( 2 , countShapes() );
        }

        /**
         * Tests the behavior of adding a shape to the physics world within a collision event callback.
         * This test ensures that the callback mechanism behaves correctly and deferred additions
         * are only applied after the physics update cycle is completed.
         *
         * Behavior verified in this method:
         * 1. Shapes are added to the physics world initially.
         * 2. A collision event listener is registered with a shape to handle collision events.
         * 3. In the collision callback, an additional shape is slated for addition to the physics world.
         * 4. The new shape is only added to the physics world after an update cycle (`physicsWorld.update`).
         * 5. Ensures that removing the collision listener prevents further additions to the world.
         */
        @Test
        void addShapeInCollisionEventCallback() {
            var forceFieldRight = new MockForceStrategy(new Vector2D(1,0));
            var dt = 1;

            physicsWorld.addShape(circle);
            physicsWorld.addShape(rectangle);

            assertEquals( 2 , countShapes(), "" +
                    "Both shapes should be present");

            physicsWorld.addCollisionListener(rectangle,
                    (event) -> {
                        physicsWorld.addShape(anotherRectangle);
                        assertEquals( 2 , countShapes(),
                                "not immediately added");
                    });

            physicsWorld.setForceStrategy(forceFieldRight);
            physicsWorld.update(dt);

            assertEquals( 3 , countShapes(),"should be added after update" );

            physicsWorld.removeAllCollisionListeners(rectangle);

            physicsWorld.update(dt);
            assertEquals( 3 , countShapes(), "but not again" );
        }

    }


    @Nested
    class RemoveShapeTest {

        /**
         * Verifies that the `removeShape` method in the `PhysicsWorld` class throws a {@link NullPointerException}
         * when a null shape is passed as an argument.
         *
         * This test ensures that the `removeShape` method correctly validates its input and enforces
         * the requirement that the shape to be removed cannot be null.
         */
        @Test
        void removeShapeNull() {
            assertThrows( NullPointerException.class,
                    () -> physicsWorld.removeShape(null) );
        }


        /**
         * Tests the `removeShape` method of the `PhysicsWorld` class to ensure that
         * shapes are correctly removed from the physics world.
         *
         * The test begins by verifying that the physics world is initially empty.
         * A shape (`circle`) is then added to the physics world, and its presence
         * is confirmed by asserting the shape count. Subsequently, the `removeShape`
         * method is called to remove the shape from the physics world. The test
         * concludes by verifying that the shape count correctly reflects that the
         * shape has been removed.
         *
         * Ensures the correctness of the `removeShape` method by validating its
         * ability to remove shapes and update the state of the physics world properly.
         */
        @Test
        void removeShape() {
            assertEquals( 0 , countShapes() );

            physicsWorld.addShape(circle);

            assertEquals( 1 , countShapes() );

            physicsWorld.removeShape(circle);

            assertEquals( 0 , countShapes() );
        }

        /**
         * Check if a {@link lob.quad.PointQuadtree} is returned
         * and the number of shapes it contains has the expected value
         * after adding and removing them.
         */
        @Test
        void getShapes() {

            PointQuadtree<Shape> shapes = physicsWorld.getShapes();

            assertNotNull( shapes , "non null quad tree expected");

            assertEquals( 0 , shapeStream( physicsWorld.getShapes() ).count() ,
                    "no shapes expected");

            physicsWorld.addShape(circle);

            assertEquals( 1 , shapeStream( physicsWorld.getShapes()).count() ,
                    "one shapes expected");

            physicsWorld.removeShape(circle);

            assertEquals( 0, shapeStream( physicsWorld.getShapes()).count() ,
                    "no shapes expected");

        }

        /**
         * Tests the behavior of removing a shape during a collision event callback in the `PhysicsWorld`.
         *
         * This test ensures that shapes scheduled for removal during a collision event are not
         * immediately removed but are deferred until after the current update cycle completes.
         *
         * The test verifies this behavior with the following steps:
         * 1. Adds two shapes (`circle` and `rectangle`) to the `physicsWorld`.
         * 2. Attaches a collision listener to the `rectangle` that attempts to remove it when a collision event occurs.
         * 3. Applies a force strategy and updates the physics world to trigger a collision.
         * 4. Asserts that:
         *    - Both shapes are initially present.
         *    - The shape is not immediately removed within the callback.
         *    - The shape is removed from the `physicsWorld` after the update cycle completes.
         *
         * This test demonstrates the proper handling of deferred shape removal to maintain the internal consistency of the physics world during updates.
         */
        @Test
        void removeShapeInCollisionEventCallback() {
            var forceFieldRight = new MockForceStrategy(new Vector2D(1,0));
            var dt = 1;

            physicsWorld.addShape(circle);
            physicsWorld.addShape(rectangle);

            assertEquals( 2 , countShapes(), "" +
                    "Both shapes should be present");

            physicsWorld.addCollisionListener(rectangle,
                    (event) -> {
                        physicsWorld.removeShape(rectangle);
                        assertEquals( 2 , countShapes(),"not immediately removed");
                    });

            physicsWorld.setForceStrategy(forceFieldRight);
            physicsWorld.update(dt);

            assertEquals( 1 , countShapes(), "should be removed after update" );

        }

        /**
         * Tests the behavior of removing a shape during an escape event callback in the `PhysicsWorld`.
         *
         * This test ensures that shapes scheduled for removal during an escape event are not immediately removed
         * within the event callback but are deferred until the current update cycle completes.
         *
         * The test verifies this behavior with the following steps:
         * 1. Adds two shapes (`circle` and `rectangle`) to the `physicsWorld`.
         * 2. Attaches an escape listener to the `boundary` that attempts to remove the `rectangle` during an escape event.
         * 3. Applies a force strategy to move shapes and updates the physics world to trigger the escape event.
         * 4. Asserts that:
         *    - Both shapes are initially present in the physics world.
         *    - The shape is not immediately removed within the callback.
         *    - The shape is removed from the physics world after the update cycle completes.
         *
         * This test demonstrates the deferred removal mechanism in the context of escape events, ensuring the
         * internal consistency of the physics world during updates and event handling.
         */
        @Test
        void removeShapeInEscapeEventCallback() {
            var forceFieldLeft = new MockForceStrategy(new Vector2D(-1,0));
            var dt = 2;

            physicsWorld.addShape(circle);
            physicsWorld.addShape(rectangle);

            assertEquals( 2 , countShapes(),
                    "Both shapes should be present");

            physicsWorld.addEscapeListener(boundary,
                    (event) -> {
                        physicsWorld.removeShape(rectangle);
                        assertEquals( 2 , countShapes(),
                                "Shape is not immediately removed");
                    });

            // the ball moves to the left and the rectangle is to the right
            physicsWorld.setForceStrategy(forceFieldLeft);
            physicsWorld.update(dt);

            assertEquals( 1 , countShapes(),
                    "Shape should be removed after update" );
        }

    }

    /**
     * Unit tests for verifying the behavior of the `findShape` method in the `PhysicsWorld` class.
     * The `findShape` method is used to locate the first shape in the physics world that matches
     * a given condition, defined by a predicate.
     */
    @Nested
    class FindShapeTest {

        /**
         * Tests the functionality of the `findShape` method in the `PhysicsWorld` class
         * for locating a `Circle` shape within the physics world.
         *
         * This test first verifies that the `findShape` method correctly returns `null`
         * when no `Circle` has been added to the physics world. It then ensures that
         * after adding a `Circle`, the method correctly identifies and returns the
         * newly added `Circle`.
         *
         * The assertions performed in this method check the behavior of the `findShape`
         * method when dealing with a specific shape type, ensuring the correctness
         * of the method's implementation.
         */
        @Test
        void findShapeCircle() {
            physicsWorld.addShape(rectangle);

            assertNull(physicsWorld.findShape(s -> s instanceof Circle), "no circle added yet");

            physicsWorld.addShape(circle);

            Shape shape = physicsWorld.findShape(s -> s instanceof Circle);

            assertEquals(circle, shape, "circle expected");
        }

        /**
         * Tests the functionality of the `findShape` method in the `PhysicsWorld` class
         * for locating a `Rectangle` shape within the physics world.
         *
         * This test verifies that the `findShape` method correctly returns `null`
         * when no `Rectangle` object has been added to the physics world. It then ensures
         * that after adding a `Rectangle`, the method properly identifies and returns
         * the newly added `Rectangle`.
         *
         * The assertions in this test validate the behavior of the `findShape` method,
         * ensuring its accuracy and reliability in finding a specific shape type
         * based on the provided condition.
         */
        @Test
        void findShapeRectangle() {
            physicsWorld.addShape(circle);

            assertNull(physicsWorld.findShape(s -> s instanceof Rectangle), "no rectangle added yet");

            physicsWorld.addShape(rectangle);

            Shape shape = physicsWorld.findShape(s -> s instanceof Rectangle);

            assertEquals(rectangle, shape, "rectangle expected");
        }

    }


    @Test
    void resetShapeInCollisionEventCallback() {
        var forceFieldRight = new MockForceStrategy(new Vector2D(1,0));
        var dt = 1;

        physicsWorld.addShape(circle);
        physicsWorld.addShape(rectangle);

        assertEquals( 2 , countShapes(), "" +
                "Both shapes should be present");

        physicsWorld.addCollisionListener(rectangle,
                (event) -> {
                    physicsWorld.reset();
                    assertEquals( 2 , countShapes(),
                            "Shapes are not immediately removed");
                });

        physicsWorld.setForceStrategy(forceFieldRight);
        physicsWorld.update(dt);

        assertEquals( 0 , countShapes(),
                "Shapes should be removed after update" );
    }


    @Nested
    class CollisionListenerTest {

        /**
         * Tests the behavior of adding a collision listener with null parameters in the physics world.
         *
         * This test method validates that the {@code addCollisionListener} method of the physics world
         * correctly handles null inputs by throwing appropriate exceptions. Specifically, it covers the
         * following scenarios:
         *
         * 1. When the observer parameter is null, a {@code NullPointerException} is expected.
         * 2. When the shape parameter is null, a {@code NullPointerException} is expected.
         * 3. When both the shape and observer parameters are null, a {@code NullPointerException} is expected.
         *
         * Assertions are made to verify that the {@code addCollisionListener} method enforces non-null
         * constraints on its parameters.
         */
        @Test
        void addCollisionListenerWithNulls() {
            assertAll(
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.addCollisionListener(circle, null),
                            "observer is null"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.addCollisionListener(null,
                                    (event) -> {}),
                            "shape is null"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.addCollisionListener(null, null),
                            "shape is null")
            );
        }

        /**
         * Tests the behavior of removing a collision listener with null parameters in the physics world.
         *
         * This test method validates that the {@code removeCollisionListener} method of the physics world
         * correctly handles null inputs by throwing appropriate exceptions. Specifically, it covers the
         * following scenarios:
         *
         * 1. When the observer parameter is null, a {@code NullPointerException} is expected.
         * 2. When the shape parameter is null, a {@code NullPointerException} is expected.
         * 3. When both the shape and observer parameters are null, a {@code NullPointerException} is expected.
         *
         * Assertions are made to verify that the {@code removeCollisionListener} method enforces non-null
         * constraints on its parameters.
         */
        @Test
        void removeCollisionListenerWithNulls() {
            assertAll(
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.removeCollisionListener(circle, null),
                            "observer is null"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.removeCollisionListener(null,
                                    (event) -> {}),
                            "shape is null"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.removeCollisionListener(null, null),
                            "shape and observer are null"),
                    () -> assertThrows( NullPointerException.class,
                                    () -> physicsWorld.removeAllCollisionListeners(null),
                            "shape is null")
            );
        }

        /**
         * Tests adding and removing a collision listener for a rectangular shape in the physics world.
         * The test verifies the behavior and sequence of collision event triggering when a collision listener
         * is added, removed, or re-added for a specific shape.
         *
         * This method covers the following scenarios:
         * 1. Adding a collision listener to a rectangular shape and confirming that collision events are triggered.
         * 2. Removing the collision listener and ensuring no further collision events are triggered for the shape.
         * 3. Re-adding the collision listener and verifying that collision events resume for the shape.
         *
         * The test ensures correctness by asserting the expected count of collision triggers as the listener
         * is added, removed, and added back.
         */
        @Test
        void addAndRemoveCollisionListener() {
            var forceFieldRight = new MockForceStrategy(new Vector2D(1,0));
            var dt = 1;
            var observer = new MockObserver();

            physicsWorld.setForceStrategy(forceFieldRight);

            physicsWorld.addShape(circle);
            physicsWorld.addShape(rectangle);

            physicsWorld.addCollisionListener(rectangle,observer);

            physicsWorld.update(dt);

            assertEquals( 1 , observer.getCount());

            resetCircle();
            physicsWorld.update(dt);

            assertEquals( 2 , observer.getCount(),
                    "Should trigger the event again if you repeat it");

            physicsWorld.removeCollisionListener(rectangle,observer);

            resetCircle();

            physicsWorld.update(dt);

            assertEquals( 2 , observer.getCount(),
                    "Listener was removed, shouldn't be updated");

            physicsWorld.addCollisionListener(rectangle,observer);

            resetCircle();

            physicsWorld.update(dt);

            assertEquals( 3 , observer.getCount(),
                    "Listener added again, should be updated");

        }


        /**
         * Tests the behavior of adding, removing, and re-adding a shape
         * as a collision subject in the physics world. Validates
         * that the collision listener receives events in the expected
         * sequence based on the operations performed.
         *
         * This test includes the following scenarios:
         * - Adding two shapes, one of which is registered as a subject
         *   for collision listening.
         * - Verifying that the listener receives the correct number of
         *   collision events after updates.
         * - Removing the shape and validating that no collision events
         *   are received for the removed shape.
         * - Re-adding the shape without re-registering it as a collision
         *   subject and confirming no additional events are received.
         * - Re-registering the shape as a collision subject and confirming
         *   that the listener receives the appropriate updates.
         *
         * Assertions verify the count of collision events against expected
         * values in each stage to ensure proper behavior.
         */
        @Test
        void addAndRemoveAndAddAgainSubjectOfCollision() {
            var forceFieldRight = new MockForceStrategy(new Vector2D(1,0));
            var dt = 1;
            var observer = new MockObserver();

            physicsWorld.setForceStrategy(forceFieldRight);

            physicsWorld.addShape(circle);
            physicsWorld.addShape(rectangle);

            physicsWorld.addCollisionListener(rectangle,observer);

            assertEquals( 0 , observer.getCount(), "no events received before update");

            physicsWorld.update(dt);

            assertEquals( 1 , observer.getCount(), "should receive an event");

            resetCircle();
            physicsWorld.update(dt);

            assertEquals( 2 , observer.getCount(),
                    "should trigger the event again if you repeat it");

            resetCircle();
            physicsWorld.removeShape(rectangle);

            physicsWorld.update(dt);

            assertEquals( 2 , observer.getCount(),
                    "Rectangle was removed, observer shouldn't be updated");

            resetCircle();
            physicsWorld.addShape(rectangle);

            physicsWorld.update(dt);

            assertEquals( 2 , observer.getCount(),
                    "Rectangle added again, but not added as subject, should be updated");


            resetCircle();
            physicsWorld.addCollisionListener(rectangle,observer);
            physicsWorld.update(dt);

            assertEquals( 3 , observer.getCount(),
                    "added again as subject, should be updated, but only once");
        }

    }

    @Nested
    class EscapeListenerTest {

        @Test
        void addEscapeListenerWithNulls() {
            assertAll(
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.addEscapeListener(rectangle, null),
                            "observer is null"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.addEscapeListener(null,
                                    (event) -> {}),
                            "shape is null"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.addEscapeListener(null, null))
            );
        }

        /**
         * Verifies that the `removeEscapeListener` method in the `physicsWorld` behaves as expected when
         * provided with null parameters.
         *
         * This method validates the following scenarios:
         * 1. Ensures that a `NullPointerException` is thrown when the observer parameter is null.
         * 2. Ensures that a `NullPointerException` is thrown when the shape parameter is null.
         * 3. Ensures that a `NullPointerException` is thrown when both the observer and shape parameters are null.
         *
         * The assertions use `assertThrows` to confirm that the proper exceptions are raised
         * with appropriate cases of null parameters passed to the method.
         */
        @Test
        void removeEscapeListenerWithNulls() {
            assertAll(
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.removeEscapeListener(rectangle, null),
                            "observer is null"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.removeEscapeListener(null,
                                    (event) -> {}),
                            "shape is null"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.removeEscapeListener(null, null),
                            "shape and observer are nulls"),
                    () -> assertThrows( NullPointerException.class,
                            () -> physicsWorld.removeAllEscapeListeners(null ),
                            "shape is null"
                    )
            );
        }


        /**
         * Tests the functionality of adding and removing an escape listener in the physics world.
         *
         * This method verifies the following:
         *
         * 1. An observer is triggered when a shape escapes a specified boundary while the listener is active.
         * 2. Repeated triggering of the escape event updates the observer count.
         * 3. Removing the escape listener prevents the observer from being updated when the event occurs.
         * 4. Re-adding the escape listener resumes updates to the observer when the event occurs.
         *
         * The method performs these checks using a mock observer, mock force strategy, and test boundary,
         * ensuring consistent outcomes for escape events within specified conditions.
         */
        @Test
        void addAndRemoveEscapeListener() {
            var forceFieldRight = new MockForceStrategy(new Vector2D(1,0));
            var dt = 2;
            var observer = new MockObserver();

            physicsWorld.setForceStrategy(forceFieldRight);

            physicsWorld.addShape(circle);

            physicsWorld.addEscapeListener(boundary,observer);

            physicsWorld.update(dt);

            assertEquals( 1 , observer.getCount());

            resetCircle();
            physicsWorld.update(dt);

            assertEquals( 2 , observer.getCount(),
                    "Should trigger the event again if you repeat it");

            physicsWorld.removeEscapeListener(boundary,observer);

            resetCircle();

            physicsWorld.update(dt);

            assertEquals( 2 , observer.getCount(),
                    "Listener was removed, shouldn't be updated");

            physicsWorld.addEscapeListener(boundary,observer);

            resetCircle();

            physicsWorld.update(dt);

            assertEquals( 3 , observer.getCount(),
                    "Listener added again, should be updated");

        }

    }

    /*******************************************\
     *                   UTILS                 *
     *******************************************/

    /**
     * Create a shape's strea a shape's iterable.
     *
     * @param iterable of shapes.
     * @return stream of shapes.
     */
    private Stream<Shape> shapeStream(Iterable<Shape> iterable) {
        return StreamSupport.stream(iterable.spliterator(),false);
    }


    /**
     * Counts the number of shapes currently present in the physics world.
     * Depends on the physical world being iterable.
     *
     * @return the total count of shapes available in the physics world
     */
    private long countShapes() {
        return shapeStream(physicsWorld).count();
    }

    /**
     * Retrieves the single circle from the current stream of shapes in the physics world.
     * This method asserts there is exactly one shape present in the stream and returns it
     * if it is a circle. If the shape is not a circle, the method fails the test.
     *
     * @return the single {@code Circle} present in the current shape stream
     * @throws AssertionError if there is not exactly one shape in the stream
     * @throws AssertionError if the single shape in the stream is not a {@code Circle}
     */
    private Circle getSingleCircle() {

        assertEquals( 1 , shapeStream(physicsWorld).count(), "expected only one shape");

        Shape shape = shapeStream(physicsWorld).findFirst().get();

        if(shape instanceof Circle circle)
            return circle;
        else {
            fail("unexpected shape: " + shape);
            return null;
        }
    }


    /**
     * Remove the first and only circle in the physic world
     * and reset it to its original value
     */
    private void resetCircle() {
        var movedCircle = shapeStream(physicsWorld)
                .filter(s -> s instanceof Circle)
                .findFirst()
                .get();

        physicsWorld.removeShape(movedCircle);
        physicsWorld.addShape(circle);
    }

}