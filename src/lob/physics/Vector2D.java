package lob.physics;

import lob.quadtree.HasPoint;

/**
 * Vector2D implementado como record para satisfazer os testes unitários.
 * Resolvido o erro de normalização do vetor nulo (NULL_VECTOR).
 */
public record Vector2D(double x, double y) implements HasPoint {

    // --- CONSTANTES DE DIREÇÃO ---
    public static final Vector2D NULL_VECTOR = new Vector2D(0, 0);
    public static final Vector2D NORTH = new Vector2D(0, -1);
    public static final Vector2D SOUTH = new Vector2D(0, 1);
    public static final Vector2D EAST = new Vector2D(1, 0);
    public static final Vector2D WEST = new Vector2D(-1, 0);
    public static final Vector2D NORTH_WEST = new Vector2D(-1, -1);
    public static final Vector2D SOUTH_WEST = new Vector2D(-1, 1);
    public static final Vector2D NORTH_EAST = new Vector2D(1, -1);
    public static final Vector2D SOUTH_EAST = new Vector2D(1, 1);

    // --- MÉTODOS DE ACESSO ---
    public double getX() { return x; }
    public double getY() { return y; }

    // --- COMPRIMENTO / MAGNITUDE ---

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double lengthSquared() {
        return (x * x) + (y * y);
    }

    public double norm() {
        return length();
    }

    // --- OPERAÇÕES MATEMÁTICAS ---

    public Vector2D add(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public Vector2D plus(Vector2D v) {
        return add(v);
    }

    public Vector2D minus(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D subtract(Vector2D v) {
        return minus(v);
    }

    public Vector2D multiply(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    public Vector2D scale(double scalar) {
        return multiply(scalar);
    }

    /**
     * Resolve o erro de normalização: normalize() de (0,0) deve resultar em (0,0).
     * Isto garante que o teste normalize() passe com comprimento 0.0.
     */
    public Vector2D normalize() {
        double len = length();
        if (len == 0) return NULL_VECTOR;
        return new Vector2D(x / len, y / len);
    }

    /**
     * Produto Escalar / Dot Product.
     */
    public double innerProduct(Vector2D v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    public double dot(Vector2D v) {
        return innerProduct(v);
    }
}