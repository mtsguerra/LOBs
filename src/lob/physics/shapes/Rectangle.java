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

    // --- MÉTODOS DE GEOMETRIA (Novos Erros) ---

    // Retorna o ponto dentro/na borda do retângulo mais próximo de p
    public Vector2D closestPoint(Vector2D p) {
        double closestX = Math.max(xMin, Math.min(p.getX(), xMax));
        double closestY = Math.max(yMin, Math.min(p.getY(), yMax));
        return new Vector2D(closestX, closestY);
    }

    // Atalho para obter a posição (semelhante ao appearance())
    public Vector2D position() {
        return getPosition();
    }

    // Verifica se um ponto está dentro do retângulo
    public boolean inside(Vector2D p) {
        return p.getX() >= xMin && p.getX() <= xMax &&
                p.getY() >= yMin && p.getY() <= yMax;
    }

    // Move o retângulo somando a velocidade * tempo às coordenadas
    public void move(Vector2D velocity, int dt) {
        double dx = velocity.getX() * dt;
        double dy = velocity.getY() * dt;

        this.xMin += dx;
        this.xMax += dx;
        this.yMin += dy;
        this.yMax += dy;

        // Atualiza a posição central na classe pai (Shape)
        setPosition(new Vector2D((xMin + xMax) / 2, (yMin + yMax) / 2));
    }

    // --- MÉTODOS QUE JÁ TINHAMOS ---

    public Vector2D lowerLeft() { return new Vector2D(xMin, yMin); }
    public Vector2D lowerRight() { return new Vector2D(xMax, yMin); }
    public Vector2D upperLeft() { return new Vector2D(xMin, yMax); }
    public Vector2D upperRight() { return new Vector2D(xMax, yMax); }
    public Appearance appearance() { return getAppearance(); }

    @Override
    public double x() { return getPosition().getX(); }
    @Override
    public double y() { return getPosition().getY(); }
}