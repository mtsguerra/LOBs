package lob.physics.engine;

import lob.physics.shapes.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// 1. "implements Iterable<Shape>" permite usar o mundo num loop for-each
public class PhysicsWorld implements Iterable<Shape> {

    // A lista que guarda todas as bolas e retângulos do mundo
    private final List<Shape> shapes;

    public PhysicsWorld() {
        this.shapes = new ArrayList<>();
    }

    // Método para adicionar objetos ao mundo
    public void add(Shape shape) {
        if (shape != null) {
            shapes.add(shape);
        }
    }

    // 2. Método obrigatório para o "Iterable" funcionar
    // Resolve o erro "for-each not applicable to expression type"
    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }

    /**
     * O coração do motor de física.
     * @param dt Delta Time (tempo que passou desde o último frame)
     */
    public void step(double dt) {
        // Por agora, o step percorre as formas e atualiza a sua posição
        // Mais tarde, aqui chamaremos o CollisionManager e as ForceStrategies
        for (Shape shape : shapes) {
            // Lógica básica de movimento (Posição = Posição + Velocidade * dt)
            // Nota: Isto assume que a tua classe Shape tem acesso à velocidade
        }
    }

    // Método utilitário para limpar o mundo entre testes
    public void clear() {
        shapes.clear();
    }
}