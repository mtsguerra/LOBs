package lob.physics;

public class Vector2D {

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

    // Produto escalar (dot product)
    public double dot(Vector2D v) {
        return (this.x * v.x) + (this.y * v.y);
    }
}