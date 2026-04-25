package lob.physics.events;

import lob.physics.shapes.Shape;

// Este é um evento específico que implementa a interface base
public class CollisionEvent implements PhysicsEvent {

    // Uma colisão envolve sempre duas formas!
    private final Shape shape1;
    private final Shape shape2;

    public CollisionEvent(Shape shape1, Shape shape2) {
        this.shape1 = shape1;
        this.shape2 = shape2;
    }

    public Shape getShape1() { return shape1; }
    public Shape getShape2() { return shape2; }
}