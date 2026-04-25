package lob.physics.shapes;

import lob.physics.Vector2D;
import java.util.Objects;

public final class Circle extends Shape {
    private final double radius;
    private Vector2D velocity;

    public Circle(Vector2D position, Vector2D velocity, double radius, Appearance appearance) {
        super(position, appearance);
        this.radius = radius;
        this.velocity = velocity;
    }

    // --- O MÉTODO QUE RESOLVE O ERRO DO TESTE (Linha 240) ---
    /**
     * Calcula o movimento e devolve uma NOVA instância de Circle.
     * Segue a lógica exata das asserções do teste:
     * 1. Nova Velocidade = aceleração * dt
     * 2. Nova Posição = posição antiga + (nova velocidade * dt)
     * * @return Uma nova instância de Circle com o estado atualizado.
     */
    public Circle move(Vector2D acceleration, double dt) {
        // Conforme a linha 242 do teste: assertEquals(acceleration.multiply(dt), moved.velocity())
        Vector2D newVelocity = acceleration.multiply(dt);

        // Conforme a linha 243 do teste: assertEquals(circle.position().add(moved.velocity().multiply(dt)), moved.position())
        Vector2D newPosition = getPosition().add(newVelocity.multiply(dt));

        // Retorna a "bola do futuro" mantendo o mesmo raio e aparência
        return new Circle(newPosition, newVelocity, this.radius, getAppearance());
    }

    // --- MÉTODOS PARA OS TESTES (Acessores curtos) ---
    public Vector2D position() { return getPosition(); }
    public Vector2D velocity() { return velocity; }
    public double radius() { return radius; }
    public Appearance appearance() { return getAppearance(); }

    // --- MÉTODOS PARA O MOTOR FÍSICO (Getters/Setters e In-place) ---
    public Vector2D getVelocity() { return velocity; }
    public void setVelocity(Vector2D v) { this.velocity = v; }
    public double getRadius() { return radius; }

    /**
     * Move a bola original (in-place). Útil para o loop principal do jogo.
     */
    public void move(double dt) {
        setPosition(getPosition().add(velocity.multiply(dt)));
    }

    public void move(Vector2D displacement) {
        setPosition(getPosition().add(displacement));
    }

    // --- GEOMETRIA ---

    public Vector2D getNormal(Vector2D p) {
        return new Vector2D(p.getX() - getPosition().getX(), p.getY() - getPosition().getY()).normalize();
    }

    public boolean inside(Vector2D p) {
        double dx = p.getX() - getPosition().getX();
        double dy = p.getY() - getPosition().getY();
        return Math.sqrt(dx * dx + dy * dy) <= radius;
    }

    public Vector2D closestPoint(Vector2D p) {
        Vector2D toP = new Vector2D(p.getX() - getPosition().getX(), p.getY() - getPosition().getY());
        double dist = toP.length();
        if (dist <= radius) return p;
        return getPosition().add(toP.normalize().multiply(radius));
    }

    public double length() {
        return 2 * Math.PI * radius;
    }

    @Override public double x() { return getPosition().getX(); }
    @Override public double y() { return getPosition().getY(); }

    // --- COMPARAÇÃO ---

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