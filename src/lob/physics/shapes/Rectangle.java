package lob.physics.shapes;

import lob.physics.Vector2D;

public final class Rectangle extends Shape {
    private double xMin, yMin, xMax, yMax;

    public Rectangle(double xMin, double yMin, double xMax, double yMax, Appearance appearance) {
        super(new Vector2D((xMin + xMax) / 2, (yMin + yMax) / 2), appearance);
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    // --- CORREÇÃO DO MÉTODO MOVE ---
    // Alterado de void para Rectangle para resolver o erro 'void' type not allowed
    public Rectangle move(Vector2D velocity, double dt) {
        double dx = velocity.getX() * dt;
        double dy = velocity.getY() * dt;

        this.xMin += dx;
        this.xMax += dx;
        this.yMin += dy;
        this.yMax += dy;

        setPosition(new Vector2D((xMin + xMax) / 2, (yMin + yMax) / 2));
        return this; // Devolve o próprio retângulo
    }

    // --- MÉTODO GETNORMAL (Pedido no Build Output) ---
    // Calcula o vetor normal (direção perpendicular) da face mais próxima do ponto p
    public Vector2D getNormal(Vector2D p) {
        double dLeft = Math.abs(p.getX() - xMin);
        double dRight = Math.abs(p.getX() - xMax);
        double dBottom = Math.abs(p.getY() - yMin);
        double dTop = Math.abs(p.getY() - yMax);

        double min = Math.min(Math.min(dLeft, dRight), Math.min(dBottom, dTop));

        if (min == dLeft) return new Vector2D(-1, 0);
        if (min == dRight) return new Vector2D(1, 0);
        if (min == dBottom) return new Vector2D(0, -1);
        return new Vector2D(0, 1); // Top
    }

    // --- MÉTODOS RESTANTES ---
    public Vector2D closestPoint(Vector2D p) {
        double closestX = Math.max(xMin, Math.min(p.getX(), xMax));
        double closestY = Math.max(yMin, Math.min(p.getY(), yMax));
        return new Vector2D(closestX, closestY);
    }

    public boolean inside(Vector2D p) {
        return p.getX() >= xMin && p.getX() <= xMax && p.getY() >= yMin && p.getY() <= yMax;
    }

    public Vector2D position() { return getPosition(); }
    public Appearance appearance() { return getAppearance(); }
    public Vector2D lowerLeft() { return new Vector2D(xMin, yMin); }
    public Vector2D lowerRight() { return new Vector2D(xMax, yMin); }
    public Vector2D upperLeft() { return new Vector2D(xMin, yMax); }
    public Vector2D upperRight() { return new Vector2D(xMax, yMax); }

    @Override
    public double x() { return getPosition().getX(); }
    @Override
    public double y() { return getPosition().getY(); }
}