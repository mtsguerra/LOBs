package lob.physics.actions;

import lob.physics.engine.PhysicsWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The {@code RemoveShapeActionTest} class provides unit tests for validating the functionality
 * of the {@code RemoveShapeAction} class in the context of removing shapes from a physics world.
 * The tests are designed to ensure the correct behavior of shape removal operations.
 *
 * This test class extends {@code ActionsTestData}, which provides the required test setup
 * environment, including the physics world and shape definitions.
 *
 * Key responsibilities of this test class include:
 * - Setting up the test environment with a new physics world before each test case.
 * - Verifying the behavior of the {@code execute} method in the {@code RemoveShapeAction} class
 *   to ensure it performs proper shape removal from the physics world.
 *
 * The primary focus is on the interaction between the physics world and the {@code RemoveShapeAction},
 * ensuring shapes are added and removed as expected during the testing process.
 */
class RemoveShapeActionTest extends ActionsTestData{


    @BeforeEach
    void setUp() {
        world = new PhysicsWorld(WIDTH,HEIGHT);
    }


    /**
     * Tests the behavior of the {@code execute} method of the {@code RemoveShapeAction} class.
     *
     * This test case verifies the following behaviors:
     * - Initially, the physics world does not contain the empty ball.
     * - After adding the empty ball to the physics world, it should be present.
     * - A {@code RemoveShapeAction} instance can be created and does not affect the
     *   presence of the empty ball before execution.
     * - After executing the {@code RemoveShapeAction}, the empty ball should be
     *   removed from the physics world.
     *
     * Assertions ensure the correct behavior of the {@code RemoveShapeAction} when
     * applied to the physics world, particularly the removal functionality.
     */
    @Test
    void execute() {
        assertFalse( hasEmptyBall() , "no empty ball yet");

        world.addShape(EMPTY_BALL);

        assertTrue( hasEmptyBall() , "should have empty ball");

        RemoveShapeAction action = new RemoveShapeAction(world,EMPTY_BALL);

        assertTrue( hasEmptyBall() , "should still have empty ball");

        action.execute();

        assertFalse( hasEmptyBall() , "empty ball should have be removed");
    }

}