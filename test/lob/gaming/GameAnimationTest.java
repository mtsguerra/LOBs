package lob.gaming;

import lob.LotsOfBallsException;
import lob.TestData;
import lob.gaming.games.MockGame;
import lob.physics.shapes.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static lob.gaming.games.MockGame.*;
import static lob.physics.Vector2D.NULL_VECTOR;
import static org.junit.jupiter.api.Assertions.*;

class GameAnimationTest {

    MockGame game;

    @BeforeEach
    void setUp() throws LotsOfBallsException {
        game = new MockGame();
    }

    /**
     * Tests whether the {@link GameAnimation} class correctly implements the {@link Runnable} interface.
     */
    @Test
    void checkRunnable() {

        List<Class<?>> types = Arrays.asList(GameAnimation.class.getInterfaces());
        assertTrue(types.contains(Runnable.class), "GameAnimation should implement Runnable");
    }

    /**
     * A test class to validate the behavior of the appearance system within the game class.
     * This class includes nested test classes for specific components such as the {@code AppearanceFactory}.
     * The tests focus on ensuring the correct behavior of appearance retrieval and factory integration.
     */
    @Nested
    class AppearanceTest extends TestData {

        /**
         * Validates the default state of the appearance factory in the game instance.
         * Ensures that the appearance factory is null as expected when no factory has been set.
         * Specifically, this test checks that the getter method for the appearance factory
         * returns a null value by default.
         */
        @Test
        void getAppearanceFactory() {
            assertNull(GameAnimation.getAppearanceFactory(), "should be null by default");
        }

        /**
         * Tests that the default appearance returned by the game is null when no appearance
         * factory has been set. Uses parameterized testing with different double values to
         * verify this behavior.
         *
         * @param value the double value used to generate randomized strings for testing
         */
        @ParameterizedTest
        @MethodSource("doubles1")
        void getAppearanceDefault(double value) {
            var randomString = "Random String: " + value;
            assertNull(game.getAppearance(randomString),
                    "appearance should be null by default (no)");
        }

        /**
         * Test class for verifying the behavior of the {@code AppearanceFactory} integration within the game logic.
         * This test suite ensures that the appearance factory is correctly initialized, retrieved, and used
         * to generate appearances based on input criteria.
         */
        @Nested
        class AppearanceFactoryTest extends TestData {

            static AppearanceFactory appearanceFactory = name -> new MockAppearance();

            @BeforeAll
            static void setUp() {
                MockGame.setAppearanceFactory(appearanceFactory);
            }

            /**
             * Tests the {@code getAppearanceFactory()} method of the game.
             *
             * This test validates that the {@code AppearanceFactory} returned by the invoked
             * method matches the expected instance. The validation ensures that the factory
             * is correctly set and retrieved, as per the configured behavior of the game.
             */
            @Test
            void getAppearanceFactory() {
                assertEquals(appearanceFactory, GameAnimation.getAppearanceFactory(),
                        "unexpected factory");
            }

            /**
             * Validates the behavior of the {@code getAppearance(String name)} method in the game class by passing
             * a dynamic string constructed using a {@code double} value. Ensures that the returned instance is of the
             * expected type {@code MockAppearance}.
             *
             * @param value a double value used to construct a unique string, which is passed to the {@code getAppearance}
             *              method of the game class. The uniqueness of the string ensures comprehensive test coverage
             *              for varying input scenarios.
             */
            @ParameterizedTest
            @MethodSource("doubles1")
            void getAppearanceCustom(double value) {
                var randomString = "Random String: " + value;

                Appearance appearance = game.getAppearance(randomString);

                assertInstanceOf(MockAppearance.class, appearance, "unexpected appearance type");
            }
        }

    }


    /**
     * Contains nested test classes and methods for verifying the functionality of FPS (Frames Per Second)
     * and step size behavior in the {@link GameAnimation} class. This test class ensures that default
     * settings and custom configurations produce expected results.
     */
    @Nested
    class FPSandStepTest extends TestData {
        final static int EXPECTED_DEFAULT_FPS = 60;

        /**
         * Tests the {@link GameAnimation#getFPS()} method to ensure it correctly retrieves
         * the default frames per second (FPS) value as defined by the game animation system.
         * The default FPS is expected to match the predefined constant.
         */
        @Test
        void getFPS() {
            assertEquals(EXPECTED_DEFAULT_FPS, GameAnimation.getFPS(),
                    "unexpected default FPS");
        }

        /**
         * Tests the {@link GameAnimation#getStep()} method to verify its correctness in computing the simulation step size.
         * The expected step size is determined as the reciprocal of the default frames per second (FPS).
         */
        @Test
        void getStep() {
            assertEquals(1.0D / EXPECTED_DEFAULT_FPS, GameAnimation.getStep(), EPSILON,
                    "unexpected step");

        }

        @Nested
        class SetFPS {
            final static int ANOTHER_FPS = 50;

            /**
             * Configures the game environment by setting the frames per second (FPS)
             * using a predefined value before all tests in the containing test class are executed.
             */
            @BeforeAll
            static void setUp() {
                MockGame.setFPS(ANOTHER_FPS);
            }

            /**
             * Tests the {@link MockGame#getFPS()} method to verify that the frame rate per second (FPS)
             * set for the game is correctly retrieved.
             */
            @Test
            void getFPS() {
                assertEquals(ANOTHER_FPS, MockGame.getFPS(),
                        "unexpected FPS after setting it");
            }

            /**
             * Tests the {@link GameAnimation#getStep()} method to validate the calculated
             * step size based on the frame rate per second (FPS) set for the game.
             */
            @Test
            void getStep() {
                assertEquals(1D / ANOTHER_FPS, GameAnimation.getStep(), EPSILON,
                        "unexpected step after setting a new FPS");
            }

        }


    }

    @Nested
    class MessageShower extends TestData {
        String message;
        Consumer<String> mockShower = (m) -> message = m;

        @Test
        void getMessageShower() {
            assertNotNull(game.getMessageShower(), "some message sower expected");
        }

        @Test
        void setMessageShower() {
            game.setMessageShower(mockShower);

            assertEquals(mockShower, game.getMessageShower(), "unexpected message shower");
        }

        @ParameterizedTest
        @MethodSource("doubles1")
        void showMessage(double value) {
            var randomMessage = "some random message with " + value;

            game.setMessageShower(mockShower);

            game.showMessage(randomMessage);

            assertEquals(randomMessage, message, "unexpected message");
        }
    }


    /**
     * A set of unit tests for validating the behavior of frame-related methods
     * in a {@code GameAnimation} instance.
     *
     * The {@code FrameShowerTest} class is a nested test class designed to ensure
     * correct functionality of the {@link FrameShower} interface and its related
     * methods, including setting, retrieving, and rendering frames.
     *
     * This test class uses a mocked implementation of {@link FrameShower} to
     * validate frame-rendering logic.
     */
    @Nested
    class FrameShowerTest {
        int counter;
        FrameShower mockShower = (w) -> counter++;

        @BeforeEach
        void setUp() {
            counter = 0;
        }

        /**
         * Tests that the {@code getFrameShower} method of the {@code game} object
         * correctly retrieves the currently active {@link FrameShower} instance.
         */
        @Test
        void getFrameShower() {

            assertNotNull(game.getFrameShower(), "some frame shower expected");
        }

        /**
         * Tests the {@code setFrameShower} method of the {@code game} object to ensure it correctly sets
         * the {@link FrameShower} instance that will handle frame rendering.
         */
        @Test
        void setFrameShower() {
            game.setFrameShower(mockShower);

            assertEquals(mockShower, game.getFrameShower(), "unexpected shower");
        }

        /**
         * Tests the behavior of the {@code showFrame} method of the {@code game} object.
         *
         * This test verifies that the {@code showFrame} method uses the currently set {@link FrameShower}
         * instance to display frames from the game world. It ensures that the frame counter increases
         * as frames are rendered and validates that the correct number of frames are shown during
         * the loop.
         */
        @Test
        void showFrame() {
            final var n = 10;

            game.setFrameShower(mockShower);

            IntStream.range(0, n).forEach(i -> {
                assertEquals(i, counter, "frames shown:" + i);
                game.showFrame();
            });
            assertEquals(n, counter, "no frames shown yet");
        }
    }

    /**
     * Unit tests for verifying the correct implementation of
     * abstract methods in the {@link GameAnimation} class.
     */
    @Nested
    class AbstractMethodsTest {

        @Test
        void getName() {
            assertEquals(GAME_NAME, game.getName(), "unexpected name");
        }

        @Test
        void getInstructions() {
            assertEquals(GAME_INSTRUCTIONS, game.getInstructions(), "unexpected instructions");
        }

        @Test
        void getWidth() {
            assertEquals(GAME_WIDTH, game.getWidth(), "unexpected width");
        }

        @Test
        void getHeight() {
            assertEquals(GAME_HEIGHT, game.getHeight(), "unexpected height");
        }
    }

    /**
     * A nested test class to validate the animation functionality of the game environment.
     *
     * This class contains tests and utility methods that ensure the game animation behaves
     * as expected, including frame-by-frame validations, state transitions, and timing checks.
     * The animation test process involves running a mock game scenario, monitoring the game state,
     * and verifying game behavior during a simulated animation cycle.
     */
    @Nested
    class AnimateTest {
        static final Circle ZERO_BALL = new Circle(NULL_VECTOR, NULL_VECTOR, 0, null);
        static final int FRAMES_MAX = 100;

        int frameCounter = 0;

        /**
         * Tests the animation sequence of the game environment.
         *
         * This test method simulates the animation process by initiating the game,
         * verifying its state transitions, and tracking animation frames.
         * It asserts that the game starts correctly, remains in a running state during
         * animation, and transitions properly to a finished state at the expected frame count.
         * Additionally, it verifies that the game resets once the animation ends.
         *
         * The steps performed in this method include:
         * - Adding a predefined shape to the game world.
         * - Setting a custom frame shower to process and validate frames.
         * - Starting the game and ensuring the running state activates correctly.
         * - Continuously checking and validating the game state while it runs.
         * - Asserting the frame counter matches the expected maximum frames.
         * - Monitoring and verifying the reset mechanism triggers as expected after the animation ends.
         *
         * The test enforces constraints on timing, frame progression, and system state
         * during the animation to ensure compliance with the defined game behavior.
         */
        @Test
        void animate() {
            var initialResets = game.getResetCounter();

            game.world.addShape(ZERO_BALL);
            game.setFrameShower(this::myFrameShower);

            assertFalse(game.isRunning(), "not yet running");

            assertTimeoutPreemptively(Duration.ofMillis(100), game::start);

            assertTrue(game.isRunning(), "should be running");

            assertThrows( IllegalStateException.class, game::start,
                    "should not be able to start a running game");

            while (game.isRunning()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            assertEquals(FRAMES_MAX, frameCounter, "unexpected frame counter");

            int waitForResetCounter = 0;
            while(game.getResetCounter() == initialResets) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if( ++waitForResetCounter > 10) {
                    fail("wait too long for reset");
                }
            }

        }


        void myFrameShower(Iterable<Shape> shapes) {
            checkGameState(shapes);
            checkRealFPS();

            if (++frameCounter >= FRAMES_MAX)
                game.stop();
        }

        private void checkGameState(Iterable<Shape> shapes) {
            assertTrue(game.isRunning(), "should be running");

            shapes.forEach(shape -> assertEquals(ZERO_BALL, shape, "unexpected shape") );
        }

        long previous = 0;

        private void checkRealFPS() {
            long now = System.currentTimeMillis();
            if (frameCounter > 5) { // ignore first frames
                double fps = 1000D / (now - previous) ;
                assertEquals(GameAnimation.getFPS(), fps,20, //10
                        "actual FPS too different from target FPS");
            }
            previous = now;
        }

    }

}



