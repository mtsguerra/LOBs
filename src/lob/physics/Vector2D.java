package lob.physics;

import lob.quadtree.HasPoint;
import java.util.Objects;

public final class Vector2D implements HasPoint {

    public static final Vector2D NULL_VECTOR = new Vector2D(0, 0);

    // --- CONSTANTES DE DIREÇÃO (Resolve o erro: cannot find symbol WEST/EAST) ---
    public static final Vector2D NORTH = new Vector2D(0, -1);
    public static final Vector2D SOUTH = new Vector2D(0, 1);
    public static final Vector2D EAST = new Vector2D(1, 0);   // Direita
    public static final Vector2D WEST = new Vector2D(-1, 0);  // Esquerda
    public static final Vector2D NORTH_WEST = new Vector2D(-1, -1);
    public static final Vector2D SOUTH_WEST = new Vector2D(-1, 1);
    public static final Vector2D NORTH_EAST = new Vector2D(1, -1);
    public static final Vector2D SOUTH_EAST = new Vector2D(1, 1);

    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // --- MÉTODOS DE ACESSO (Interface HasPoint e Getters) ---

    @Override
    public double x() { return x; }

    @Override
    public double y() { return y; }

    public double getX() { return x; }

    public double getY() { return y; }

    // --- COMPRIMENTO / MAGNITUDE ---

    /**
     * Resolve o erro: cannot find symbol method length()
     */
    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Retorna o comprimento do vetor ao quadrado (otimização para evitar Math.sqrt).
     * Resolve o erro: cannot find symbol method lengthSquared()
     */
    public double lengthSquared() {
        return (x * x) + (y * y);
    }

    /**
     * Subtrai um vetor a este. (Apelido para o método minus).
     * Resolve o erro: cannot find symbol method subtract(Vector2D)
     */
    public Vector2D subtract(Vector2D v) {
        return minus(v); // Reutilizamos a lógica que já lá está!
    }

    public double norm() {
        return length();
    }

    // --- OPERAÇÕES MATEMÁTICAS ---

    public Vector2D add(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    /**
     * Resolve o erro: cannot find symbol method minus()
     */
    public Vector2D minus(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D multiply(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    public Vector2D scale(double scalar) {
        return multiply(scalar);
    }

    public Vector2D normalize() {
        double len = length();
        if (len == 0) return NULL_VECTOR;
        return new Vector2D(x / len, y / len);
    }

    /**
     * Resolve o erro: cannot find symbol method innerProduct()
     */
    public double innerProduct(Vector2D v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    public double dot(Vector2D v) {
        return innerProduct(v);
    }

    // --- COMPARAÇÃO E TEXTO (Essencial para os Testes) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Double.compare(vector2D.x, x) == 0 &&
                Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}