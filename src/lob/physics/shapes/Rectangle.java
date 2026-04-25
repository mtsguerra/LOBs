package lob.physics.shapes;

import lob.physics.Vector2D;

public final class Rectangle extends Shape {
    private double xMin, xMax, yMin, yMax;

    public Rectangle(Vector2D position, double width, double height) {
        super(position);
        // O referencial é canto superior esquerdo (x,y)
        this.xMin = position.getX();
        this.yMin = position.getY();
        this.xMax = xMin + width;
        this.yMax = yMin + height;
    }

    // Getters para os limites (usados nas colisões)
    public double getXMin() { return xMin; }
    public double getXMax() { return xMax; }
    public double getYMin() { return yMin; }
    public double getYMax() { return yMax; }
}