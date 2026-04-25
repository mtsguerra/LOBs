package lob.physics.engine;

import lob.physics.shapes.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * O PhysicsWorld gere todos os objetos físicos e coordena a simulação.
 */
public class PhysicsWorld implements Iterable<Shape> {

    private final List<Shape> shapes;
    private final double width;
    private final double height;

    /**
     * Construtor que define as dimensões do mundo.
     * Resolve o erro: constructor PhysicsWorld in class... cannot be applied to given types.
     */
    public PhysicsWorld(double width, double height) {
        this.shapes = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    /**
     * Adiciona uma forma ao mundo.
     */
    public void addShape(Shape shape) {
        if (shape != null) {
            shapes.add(shape);
        }
    }

    /**
     * Atalho para addShape.
     */
    public void add(Shape shape) {
        addShape(shape);
    }

    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }

    /**
     * Getters para as dimensões (úteis para detetar colisões com as bordas).
     */
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    /**
     * Executa um passo da simulação física.
     */
    public void step(double dt) {
        // No futuro, este método irá:
        // 1. Aplicar forças (Gravidade, Atrito)
        // 2. Mover as formas (s.move(v, dt))
        // 3. Resolver colisões entre objetos e com as paredes
    }

    /**
     * Remove todos os objetos do mundo.
     */
    public void clear() {
        shapes.clear();
    }

    /**
     * Retorna a lista de todas as formas presentes.
     */
    public List<Shape> getShapes() {
        return shapes;
    }
}