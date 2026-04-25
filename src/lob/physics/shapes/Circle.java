package lob.physics.shapes;

import lob.physics.Vector2D;
import java.util.Objects;

public final class Circle extends Shape {
    private final double radius;
    private Vector2D velocity;

    public Circle(Vector2D position, double radius, Vector2D velocity) {
        super(position);
        this.radius = radius;
        this.velocity = velocity;
    }

    public Circle(Vector2D position, Vector2D velocity, double radius) {
        this(position, radius, velocity);
    }

    // --- ESTE É O CONSTRUTOR QUE RESOLVE O ERRO DO PRINT ---
    public Circle(Vector2D position, Vector2D velocity, double radius, Appearance appearance) {
        super(position, appearance);
        this.radius = radius;
        this.velocity = velocity;
    }

    public double getRadius() { return radius; }
    public Vector2D getVelocity() { return velocity; }
    public void setVelocity(Vector2D v) { this.velocity = v; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return Double.compare(circle.radius, radius) == 0 &&
                Objects.equals(getPosition(), circle.getPosition()) &&
                Objects.equals(velocity, circle.velocity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), radius, velocity);
    }
}