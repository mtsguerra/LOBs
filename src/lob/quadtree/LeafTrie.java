package lob.quadtree;

import lob.physics.Vector2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LeafTrie<T extends HasPoint> extends Trie<T> {
    private List<T> elements = new ArrayList<>();
    private double xMin, xMax, yMin, yMax;

    public LeafTrie(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    @Override
    public Trie<T> insert(T element) {
        if (elements.size() < capacity) {
            elements.add(element);
            return this;
        }

        NodeTrie<T> newNode = new NodeTrie<>(xMin, xMax, yMin, yMax);

        for (T p : elements) {
            newNode.insert(p);
        }

        return newNode.insert(element);
    }

    @Override
    public void collectPoints(Vector2D center, double radius, Set<T> found) {
        for (T p : elements) {
            // Como agora usamos HasPoint, calculamos a distância usando os métodos p.x() e p.y()
            double dx = p.x() - center.getX(); // Assume que Vector2D tem o método getX()
            double dy = p.y() - center.getY(); // Assume que Vector2D tem o método getY()

            // Teorema de Pitágoras para ver se a distância é menor que o raio
            if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                found.add(p);
            }
        }
    }
}