package lob.physics.actions;

import lob.physics.engine.PhysicsWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddShapeActionTest extends ActionsTestData {

    @BeforeEach
    void setUp() {
        world = new PhysicsWorld(WIDTH,HEIGHT);
    }


    @Test
    void execute() {
        AddShapeAction action = new AddShapeAction(world,EMPTY_BALL);

        assertFalse( hasEmptyBall() , "no empty ball yet");

        action.execute();

        assertTrue( hasEmptyBall() , "should have the empty ball");
    }


}