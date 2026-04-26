package lob.physics.engine;

import lob.physics.Vector2D;
import lob.physics.forces.ForceStrategy;
import lob.physics.shapes.Shape;
import lob.physics.events.PhysicsEvent;
import lob.physics.events.PhysicsObserver;
import lob.physics.events.PhysicsSubject;
import lob.physics.events.CollisionEvent;
import java.util.*;

/**
 * O PhysicsWorld gere a simulação e serve de fachada para o CollisionManager.
 * Atualizado para sincronizar com os nomes exigidos pelo PhysicsWorldTest.
 */
public class PhysicsWorld extends PhysicsSubject<PhysicsEvent> implements Iterable<Shape> {

    // --- CONFIGURAÇÃO ESTÁTICA ---
    private static double margin = 100.0;

    public static double getMargin() { return margin; }
    public static void setMargin(double newMargin) { margin = newMargin; }
    public static void setMargin(int newMargin) { margin = (double) newMargin; }

    // --- CAMPOS DE INSTÂNCIA ---
    private final List<Shape> shapes = new ArrayList<>();
    private final List<ForceStrategy> forces = new ArrayList<>();
    private final double width;
    private final double height;
    private Vector2D gravity = Vector2D.NULL_VECTOR;
    private ForceStrategy forceStrategy;
    private CollisionManager collisionManager = new SimpleCollisionManager();

    public PhysicsWorld(double width, double height) {
        this.width = width;
        this.height = height;
    }

    // --- SIMULAÇÃO (Resolve o erro da linha 296 e 534) ---

    /**
     * O teste chama update(dt). Mudamos o nome de 'step' para 'update'.
     */
    public void update(double dt) {
        // No futuro, aqui correrá a lógica de integração física
    }

    /**
     * Sobrecarga para quando o teste passa um valor inteiro.
     */
    public void update(int dt) {
        update((double) dt);
    }

    // --- GESTÃO DE FORMAS (Resolve os erros removeShape das linhas 560, 586 e 611) ---

    public void addShape(Shape shape) {
        if (shape != null) shapes.add(shape);
    }

    public void add(Shape shape) {
        addShape(shape);
    }

    public void remove(Shape shape) {
        shapes.remove(shape);
    }

    /**
     * Atalho exigido pelo teste PhysicsWorldTest.
     */
    public void removeShape(Shape shape) {
        remove(shape);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }

    // --- FACHADA DE COLISÕES (Resolve os erros das linhas 522 e 540) ---

    public void addCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer) {
        collisionManager.addCollisionListener(shape, observer);
    }

    public void removeAllCollisionListeners(Shape shape) {
        collisionManager.removeAllCollisionListeners(shape);
    }

    public double getRestitution() {
        return collisionManager.getRestitution();
    }

    public void setRestitution(double restitution) {
        collisionManager.setRestitution(restitution);
    }

    public CollisionManager getCollisionManager() { return collisionManager; }

    public void setCollisionManager(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    // --- GESTÃO DE FORÇAS E GRAVIDADE ---

    public void addForce(ForceStrategy force) {
        forces.add(force);
    }

    public List<ForceStrategy> getForces() {
        return forces;
    }

    public Vector2D getGravity() { return gravity; }
    public void setGravity(Vector2D gravity) { this.gravity = gravity; }

    public ForceStrategy getForceStrategy() {
        return forceStrategy;
    }

    public void setForceStrategy(ForceStrategy strategy) {
        this.forceStrategy = strategy;
    }

    // --- ESTADO E LIMPEZA ---

    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public void clear() {
        shapes.clear();
    }

    public void reset() {
        shapes.clear();
        forces.clear();
        gravity = Vector2D.NULL_VECTOR;
    }
}