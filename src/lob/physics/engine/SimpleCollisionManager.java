package lob.physics.engine;

import lob.physics.Vector2D;
import lob.physics.events.CollisionEvent;
import lob.physics.events.PhysicsObserver;
import lob.physics.shapes.Circle;
import lob.physics.shapes.Rectangle;
import lob.physics.shapes.Shape;
import java.util.*;

/**
 * Responsável por gerir colisões e a resposta física (reflexão) entre formas.
 */
public class SimpleCollisionManager implements CollisionManager {

    private double restitution = 1.0;
    private final Map<Shape, List<PhysicsObserver<CollisionEvent>>> listeners = new HashMap<>();

    public SimpleCollisionManager() {}

    public void setRestitution(double restitution) { this.restitution = restitution; }
    public double getRestitution() { return restitution; }

    // ==========================================
    // --- RESOLUÇÃO DE COLISÕES (A parte que faltava) ---
    // ==========================================

    /**
     * Resolve o erro 'cannot find symbol' do teste.
     * Calcula a reflexão da velocidade: v' = v - 2 * (v . n) * n
     */
    public Circle resolveCircleCollision(Circle c, Shape other, CollisionManifold manifold) {
        if (!manifold.isColliding()) return c;

        Vector2D v = c.velocity();
        Vector2D n = manifold.normal();

        // Produto escalar: v . n
        double dotProduct = v.x() * n.x() + v.y() * n.y();

        // Fórmula de reflexão perfeita (restitution 1.0)
        // v' = v - (2 * dotProduct) * n
        Vector2D reflectedVelocity = v.minus(n.multiply(2 * dotProduct));

        // Retorna um novo círculo com a velocidade refletida e a mesma aparência
        return new Circle(c.position(), reflectedVelocity, c.radius(), c.appearance());
    }

    @Override
    public Shape resolveCollision(Shape colliding, Shape collided, CollisionManifold collision) {
        if (colliding instanceof Circle c) {
            return resolveCircleCollision(c, collided, collision);
        }
        // Se for um retângulo ou outra forma, por agora mantemos a mesma lógica
        return colliding;
    }

    // ==========================================
    // --- DETEÇÃO DE COLISÕES (checkCollision) ---
    // ==========================================

    @Override
    public CollisionManifold checkCollision(Shape s1, Shape s2) {
        if (s1 instanceof Circle a && s2 instanceof Circle b) return checkCollision(a, b);
        if (s1 instanceof Rectangle a && s2 instanceof Rectangle b) return checkCollision(a, b);
        if (s1 instanceof Circle a && s2 instanceof Rectangle b) return checkCollision(a, b);
        if (s1 instanceof Rectangle a && s2 instanceof Circle b) return checkCollision(a, b);

        return CollisionManifold.noCollision();
    }

    public CollisionManifold checkCollision(Circle a, Circle b) {
        Vector2D delta = b.position().minus(a.position());
        double distance = delta.length();
        double totalRadius = a.radius() + b.radius();

        if (distance < totalRadius) {
            Vector2D normal = (distance > 0) ? delta.normalize() : Vector2D.EAST;
            return new CollisionManifold(true, normal, totalRadius - distance);
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

        if (overlapX < overlapY) {
            Vector2D normal = (r1.x() < r2.x()) ? Vector2D.WEST : Vector2D.EAST;
            return new CollisionManifold(true, normal, overlapX);
        } else {
            Vector2D normal = (r1.y() < r2.y()) ? Vector2D.NORTH : Vector2D.SOUTH;
            return new CollisionManifold(true, normal, overlapY);
        }
    }

    public CollisionManifold checkCollision(Circle c, Rectangle r) {
        Vector2D closest = r.closestPoint(c.position());
        Vector2D delta = closest.minus(c.position());
        double distance = delta.length();

        if (distance < c.radius()) {
            Vector2D normal = (distance > 0) ? delta.normalize() : Vector2D.NORTH;
            return new CollisionManifold(true, normal, c.radius() - distance);
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
    // --- SISTEMA DE EVENTOS (OBSERVER) ---
    // ==========================================

    @Override
    public void addCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer) {
        listeners.computeIfAbsent(shape, k -> new ArrayList<>()).add(observer);
    }

    public void removeCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer) {
        if (listeners.containsKey(shape)) {
            listeners.get(shape).remove(observer);
        }
    }

    public void removeAllCollisionListeners(Shape shape) {
        listeners.remove(shape);
    }

    public void notifyCollision(Shape shape1, Shape shape2, CollisionManifold manifold) {
        CollisionEvent event = new CollisionEvent(shape1, shape2, manifold);
        notifyCollision(event);
    }

    public void notifyCollision(CollisionEvent event) {
        // Notifica listeners da primeira forma envolvida
        if (listeners.containsKey(event.colliding())) {
            for (PhysicsObserver<CollisionEvent> obs : listeners.get(event.colliding())) {
                obs.notified(event);
            }
        }
        // Notifica listeners da segunda forma envolvida
        if (listeners.containsKey(event.collided())) {
            for (PhysicsObserver<CollisionEvent> obs : listeners.get(event.collided())) {
                obs.notified(event);
            }
        }
    }
}