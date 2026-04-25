package lob.physics.shapes;

import lob.physics.Vector2D;
import lob.quadtree.HasPoint;

public abstract class Shape implements HasPoint {
    private Vector2D position;
    private Appearance appearance; // Adicionamos este campo

    public Shape(Vector2D position) {
        this(position, null);
    }

    // Novo construtor que aceita a aparência
    public Shape(Vector2D position, Appearance appearance) {
        this.position = position;
        this.appearance = appearance;
    }

    public Vector2D getPosition() { return position; }
    public void setPosition(Vector2D position) { this.position = position; }

    public Appearance getAppearance() { return appearance; }
    public void setAppearance(Appearance appearance) { this.appearance = appearance; }

    @Override
    public double x() { return position.getX(); }

    @Override
    public double y() { return position.getY(); }
}