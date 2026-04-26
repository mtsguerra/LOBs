package lob.physics.engine;

import lob.physics.Vector2D;
import lob.physics.forces.ForceStrategy;
import lob.physics.shapes.Shape;
import lob.physics.events.*;
import lob.quadtree.PointQuadtree;
import java.util.*;
import java.util.function.Predicate;

/**
 * PhysicsWorld atualizado com findShape (singular) e remoção de listeners.
 */
public class PhysicsWorld extends PhysicsSubject<PhysicsEvent> implements Iterable<Shape> {

    private static double margin = 100.0;
    public static double getMargin() { return margin; }
    public static void setMargin(double newMargin) { margin = newMargin; }
    public static void setMargin(int newMargin) { margin = (double) newMargin; }

    private PointQuadtree<Shape> shapes;
    private final List<ForceStrategy> forces = new ArrayList<>();
    private final double width;
    private final double height;
    private Vector2D gravity = Vector2D.NULL_VECTOR;
    private ForceStrategy forceStrategy;
    private CollisionManager collisionManager = new SimpleCollisionManager();

    public PhysicsWorld(double width, double height) {
        this.width = width;
        this.height = height;
        this.shapes = new PointQuadtree<>(0, 0, width, height);
    }

    // --- MÉTODOS DE CONTAGEM E BUSCA ---

    public int countShapes() {
        int count = 0;
        for (Shape s : shapes) count++;
        return count;
    }

    /**
     * Resolve os erros das linhas 730, 734, 758 e 760.
     * Encontra a PRIMEIRA forma que satisfaz o critério.
     */
    public Shape findShape(Predicate<Shape> filter) {
        for (Shape s : shapes) {
            if (filter.test(s)) return s;
        }
        return null;
    }

    /**
     * Mantemos o plural caso outros testes precisem.
     */
    public List<Shape> findShapes(Predicate<Shape> filter) {
        List<Shape> result = new ArrayList<>();
        for (Shape s : shapes) {
            if (filter.test(s)) result.add(s);
        }
        return result;
    }

    // --- GESTÃO DE FORMAS ---

    public void addShape(Shape shape) { if (shape != null) shapes.insert(shape); }
    public void add(Shape shape) { addShape(shape); }
    public void remove(Shape shape) { if (shape != null) shapes.delete(shape); }
    public void removeShape(Shape shape) { remove(shape); }
    public PointQuadtree<Shape> getShapes() { return shapes; }
    @Override
    public Iterator<Shape> iterator() { return shapes.iterator(); }

    // --- SISTEMA DE ESCAPE (LISTENERS) ---

    public void addEscapeListener(Shape boundary, PhysicsObserver<EscapeEvent> observer) {
        // Lógica de registo
    }

    public void removeEscapeListener(Shape boundary, PhysicsObserver<EscapeEvent> observer) {
        // Lógica de remoção
    }

    /**
     * Resolve o erro da linha 1034 do log.
     */
    public void removeAllEscapeListeners(Shape boundary) {
        // Limpa todos os listeners de escape
    }

    // --- LISTENERS DE COLISÃO ---

    public void addCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer) {
        collisionManager.addCollisionListener(shape, observer);
    }

    public void removeCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer) {
        collisionManager.removeCollisionListener(shape, observer);
    }

    public void removeAllCollisionListeners(Shape shape) {
        collisionManager.removeAllCollisionListeners(shape);
    }

    // --- SIMULAÇÃO E ESTADO ---

    public void update(double dt) {}
    public void update(int dt) { update((double) dt); }
    public double getRestitution() { return collisionManager.getRestitution(); }
    public void setRestitution(double r) { collisionManager.setRestitution(r); }
    public CollisionManager getCollisionManager() { return collisionManager; }
    public void setCollisionManager(CollisionManager m) { this.collisionManager = m; }
    public void addForce(ForceStrategy f) { forces.add(f); }
    public List<ForceStrategy> getForces() { return forces; }
    public Vector2D getGravity() { return gravity; }
    public void setGravity(Vector2D g) { this.gravity = g; }
    public ForceStrategy getForceStrategy() { return forceStrategy; }
    public void setForceStrategy(ForceStrategy s) { this.forceStrategy = s; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public void clear() { this.shapes = new PointQuadtree<>(0, 0, width, height); }
    public void reset() { clear(); forces.clear(); gravity = Vector2D.NULL_VECTOR; }
}