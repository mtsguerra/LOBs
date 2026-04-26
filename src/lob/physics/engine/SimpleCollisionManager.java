package lob.physics.engine;

import lob.physics.Vector2D;
import lob.physics.shapes.Circle;
import lob.physics.shapes.Rectangle;
import lob.physics.shapes.Shape;
import lob.physics.events.PhysicsObserver;

/**
 * O SimpleCollisionManager é responsável por detetar interseções entre formas
 * e calcular os dados da colisão (Manifold).
 */
public class SimpleCollisionManager implements CollisionManager {

    private double restitution = 1.0; // Padrão: colisão perfeitamente elástica

    public SimpleCollisionManager() {
        // Construtor vazio conforme exigido pelo teste
    }

    public void setRestitution(double restitution) {
        this.restitution = restitution;
    }

    public double getRestitution() {
        return restitution;
    }

    // ==========================================
    // --- MÉTODOS DE DETEÇÃO (checkCollision) ---
    // ==========================================

    public CollisionManifold checkCollision(Circle a, Circle b) {
        Vector2D delta = b.position().minus(a.position());
        double distance = delta.length();
        double totalRadius = a.radius() + b.radius();

        if (distance < totalRadius) {
            Vector2D normal = (distance > 0) ? delta.normalize() : Vector2D.EAST;
            double penetration = totalRadius - distance;
            return new CollisionManifold(true, normal, penetration);
        }
        return CollisionManifold.noCollision();
    }

    public CollisionManifold checkCollision(Rectangle r1, Rectangle r2) {
        if (r1.xMax() < r2.xMin() || r1.xMin() > r2.xMax() ||
                r1.yMax() < r2.yMin() || r1.yMin() > r2.yMax()) {
            return CollisionManifold.noCollision();
        }

        double overlapX = Math.min(r1.xMax(), r2.xMax()) - Math.max(r1.xMin(), r2.xMin());
        double overlapY = Math.min(r1.yMax(), r2.yMax()) - Math.max(r1.yMin(), r2.yMin());

        Vector2D normal;
        double penetration;

        if (overlapX < overlapY) {
            penetration = overlapX;
            normal = (r1.x() < r2.x()) ? Vector2D.WEST : Vector2D.EAST;
        } else {
            penetration = overlapY;
            normal = (r1.y() < r2.y()) ? Vector2D.NORTH : Vector2D.SOUTH;
        }

        return new CollisionManifold(true, normal, penetration);
    }

    public CollisionManifold checkCollision(Circle c, Rectangle r) {
        Vector2D closest = r.closestPoint(c.position());
        Vector2D delta = closest.minus(c.position());
        double distance = delta.length();

        if (distance < c.radius()) {
            Vector2D normal = (distance > 0) ? delta.normalize() : Vector2D.NORTH;
            double penetration = c.radius() - distance;
            return new CollisionManifold(true, normal, penetration);
        }
        return CollisionManifold.noCollision();
    }

    public CollisionManifold checkCollision(Rectangle r, Circle c) {
        CollisionManifold manifold = checkCollision(c, r);
        if (manifold.isColliding()) {
            return new CollisionManifold(true, manifold.normal().multiply(-1), manifold.penetration());
        }
        return manifold;
    }

    // ==========================================
    // --- MÉTODOS DE RESOLUÇÃO DE COLISÕES ---
    // ==========================================

    /**
     * Resolve a colisão de um Círculo.
     */
    public Circle resolveCircleCollision(Circle c, Shape other, CollisionManifold manifold) {
        return c; // Esqueleto para o código compilar
    }

    /**
     * Resolve a colisão de um Retângulo.
     * Resolve o erro da linha 371: cannot find symbol method resolveCollision
     */
    public Rectangle resolveCollision(Rectangle r1, Rectangle r2, CollisionManifold manifold) {
        return r1; // Esqueleto para o código compilar
    }

    // ==========================================
    // --- SISTEMA DE EVENTOS (OBSERVER) ---
    // ==========================================

    public void addCollisionListener(Shape shape, PhysicsObserver observer) {
        // Esqueleto para adicionar listener
    }

    /**
     * Resolve o erro: cannot find symbol method removeCollisionListener
     */
    public void removeCollisionListener(Shape shape, PhysicsObserver observer) {
        // Esqueleto para remover um listener específico
    }

    /**
     * Resolve o erro: cannot find symbol method removeAllCollisionListeners
     */
    public void removeAllCollisionListeners(Shape shape) {
        // Esqueleto para limpar todos os listeners de uma forma
    }

    /**
     * Notifica colisão passando o objeto de evento.
     * Resolve o erro: method notifyCollision cannot be applied to given types.
     */
    public void notifyCollision(lob.physics.events.CollisionEvent event) {
        // Esqueleto para notificar o evento
    }

    /**
     * Sobrecarga de segurança caso os testes passem diretamente as Shapes.
     */
    public void notifyCollision(Shape a, Shape b) {
        // Esqueleto de segurança
    }

    /**
     * Notifica colisão passando os dois objetos envolvidos e os dados da colisão.
     * Resolve os erros da linha 516 e 524: actual and formal argument lists differ in length
     */
    public void notifyCollision(Shape shape1, Shape shape2, CollisionManifold manifold) {
        // Esqueleto para o código compilar.
        // Mais tarde, o motor usará estes 3 dados para avisar os listeners!
    }
}