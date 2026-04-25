package lob.physics.actions;

import lob.TestData;
import lob.physics.engine.PhysicsWorld;
import lob.physics.shapes.Circle;
import lob.physics.shapes.Shape;

import static lob.physics.Vector2D.NULL_VECTOR;

public class ActionsTestData extends TestData {
    protected PhysicsWorld world;

    protected Circle EMPTY_BALL = new Circle(NULL_VECTOR,NULL_VECTOR,0,null);

    boolean hasEmptyBall() {
        for(Shape s: world)
            if(s.equals(EMPTY_BALL))
                return true;
        return false;
    }
}
