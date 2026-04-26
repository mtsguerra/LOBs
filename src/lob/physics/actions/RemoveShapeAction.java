package lob.physics.actions;

import lob.physics.engine.PhysicsWorld;
import lob.physics.shapes.Shape;

/**
 * Ação que remove uma forma específica do mundo.
 * Resolve os erros do RemoveShapeActionTest.
 */
public class RemoveShapeAction {
    private final PhysicsWorld world;
    private final Shape shape;

    public RemoveShapeAction(PhysicsWorld world, Shape shape) {
        this.world = world;
        this.shape = shape;
    }

    public void execute() {
        world.remove(shape);
    }
}