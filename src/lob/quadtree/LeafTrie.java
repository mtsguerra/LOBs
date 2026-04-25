package lob.quadtree;

import lob.physics.Vector2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LeafTrie<T extends HasPoint> extends Trie<T> {
    private final List<T> elements = new ArrayList<>();
    private final double xMin, xMax, yMin, yMax;

    public LeafTrie(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    @Override
    public Trie<T> insert(T element) {
        // Se ainda temos espaço, adicionamos à lista
        if (elements.size() < getCapacity()) {
            elements.add(element);
            return this;
        }

        // Se encheu, transformamos esta folha num Nó (divisão em 4)
        NodeTrie<T> newNode = new NodeTrie<>(xMin, xMax, yMin, yMax);

        // Migramos os elementos antigos para o novo nó
        for (T p : elements) {
            newNode.insert(p);
        }

        // Inserimos o novo elemento no nó (que decidirá o quadrante certo)
        return newNode.insert(element);
    }

    @Override
    public void collectPoints(Vector2D center, double radius, Set<T> found) {
        // 1. Otimização: Se o círculo de busca nem toca neste retângulo, ignoramos
        if (center.getX() + radius < xMin || center.getX() - radius > xMax ||
                center.getY() + radius < yMin || center.getY() - radius > yMax) {
            return;
        }

        // 2. Verificamos os elementos um a um
        for (T p : elements) {
            // Usamos o método getDistance que definimos na classe pai (Trie)
            double dist = Trie.getDistance(center.getX(), center.getY(), p.x(), p.y());

            if (dist <= radius) {
                found.add(p);
            }
        }
    }
}