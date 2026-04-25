package lob.physics.actions;

import lob.physics.engine.PhysicsWorld;
import lob.physics.shapes.Shape;

public class AddShapeAction implements ActionOnShapes {
    private final PhysicsWorld world;
    private final Shape shape;

    public AddShapeAction(PhysicsWorld world, Shape shape) {
        this.world = world;
        this.shape = shape;
    }

    @Override
    public void execute() {
        // Quando a ação é executada, adiciona a forma ao mundo
        world.addShape(shape);
    }
}