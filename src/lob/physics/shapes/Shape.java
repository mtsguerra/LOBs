package lob.physics.shapes;

import lob.physics.Vector2D;

public abstract sealed class Shape permits Circle, Rectangle {
    protected Vector2D position;

    public Shape(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition() { return position; }
    public void setPosition(Vector2D position) { this.position = position; }
}