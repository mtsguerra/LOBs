package lob.physics;

import lob.quadtree.HasPoint; // 1. Adicionamos o import da interface

// 2. Dizemos que o Vector2D implementa a interface HasPoint
public class Vector2D implements HasPoint {

    public static final Vector2D NULL_VECTOR = new Vector2D(0, 0);

    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    // Norma do vetor
    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    // Soma de dois vetores
    public Vector2D add(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    // Escalonamento (multiplicar vetor por um escalar)
    public Vector2D scale(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    // Retorna um vetor com a mesma direção mas comprimento (magnitude) = 1
    public Vector2D normalize() {
        double mag = Math.sqrt(x * x + y * y);
        if (mag == 0) return new Vector2D(0, 0); // Evita divisão por zero
        return new Vector2D(x / mag, y / mag);
    }

    // Já agora, se não tiveres o multiply:
    public Vector2D multiply(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    // Produto escalar (dot product)
    public double dot(Vector2D v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    // 3. Os métodos obrigatórios da interface HasPoint!
    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }
}