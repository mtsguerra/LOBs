package lob.physics.shapes;

import lob.physics.Vector2D;

public final class Circle extends Shape {
    private double radius;
    private Vector2D velocity;

    public Circle(Vector2D position, double radius, Vector2D velocity) {
        super(position);
        this.radius = radius;
        this.velocity = velocity;
    }

    public double getRadius() { return radius; }
    public Vector2D getVelocity() { return velocity; }
    public void setVelocity(Vector2D v) { this.velocity = v; }
}