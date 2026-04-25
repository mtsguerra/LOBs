package lob.physics.actions;

import lob.physics.engine.PhysicsWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@code ResetAction} functionality within the context of a physics world.
 *
 * This class extends {@code ActionsTestData} to utilize preset data and behaviors
 * for testing purposes, such as access to a shared {@code PhysicsWorld} instance
 * and predefined shapes.
 *
 * Responsibilities:
 * - Sets up a clean test environment with a new physics world before each test.
 * - Validates the behavior of the {@code ResetAction} class, particularly the
 *   interactions between the action and the physics world.
 */
class ResetActionTest extends ActionsTestData {


    @BeforeEach
    void setUp() {
        world = new PhysicsWorld(WIDTH,HEIGHT);
    }


    /**
     * Tests the behavior of the {@code execute} method of the {@code ResetAction} class.
     *
     * The test verifies the following behaviors:
     * - Initially, the physics world does not contain the empty ball.
     * - After adding the empty ball to the physics world, it should be present.
     * - A {@code ResetAction} can be instantiated and does not affect the
     *   presence of the empty ball before execution.
     * - After executing the {@code ResetAction}, the physics world should be
     *   reset, and the empty ball should no longer be present.
     *
     * Assertions ensure the correct behavior of the {@code ResetAction} when
     * applied to the physics world.
     */
    @Test
    void execute() {
        assertFalse( hasEmptyBall() , "no empty ball yet");

        world.addShape(EMPTY_BALL);

        assertTrue( hasEmptyBall() , "should have empty ball");

        ResetAction action = new ResetAction(world);

        assertTrue( hasEmptyBall() , "should still have empty ball");

        action.execute();

        assertFalse( hasEmptyBall() , "empty ball should have be removed");
    }

}