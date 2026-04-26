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
        if (elements.size() < getCapacity()) {
            elements.add(element);
            return this;
        }

        NodeTrie<T> newNode = new NodeTrie<>(xMin, xMax, yMin, yMax);
        for (T p : elements) {
            newNode.insert(p);
        }
        return newNode.insert(element);
    }

    // ==========================================
    // --- NOVOS MÉTODOS IMPLEMENTADOS ---
    // ==========================================

    @Override
    public T find(T element) {
        for (T p : elements) {
            // Verifica se está na mesma posição exata
            if (Double.compare(p.x(), element.x()) == 0 && Double.compare(p.y(), element.y()) == 0) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Trie<T> insertReplace(T element) {
        for (int i = 0; i < elements.size(); i++) {
            T p = elements.get(i);
            // Se encontrar um ponto na mesma posição, substitui-o
            if (Double.compare(p.x(), element.x()) == 0 && Double.compare(p.y(), element.y()) == 0) {
                elements.set(i, element);
                return this;
            }
        }
        // Se não encontrou nenhum para substituir, faz uma inserção normal
        return insert(element);
    }

    @Override
    public Trie<T> delete(T element) {
        // Remove da lista o elemento que está naquela posição
        elements.removeIf(p -> Double.compare(p.x(), element.x()) == 0 && Double.compare(p.y(), element.y()) == 0);
        return this; // A estrutura original mantém-se
    }

    @Override
    public void collectNear(double x, double y, double radius, Set<T> found) {
        // Otimização da caixa delimitadora
        if (x + radius < xMin || x - radius > xMax ||
                y + radius < yMin || y - radius > yMax) {
            return;
        }

        for (T p : elements) {
            double dist = Trie.getDistance(x, y, p.x(), p.y());
            if (dist <= radius) {
                found.add(p);
            }
        }
    }

    // --- MANTIDO DO TEU CÓDIGO ORIGINAL ---

    @Override
    public void collectPoints(Vector2D center, double radius, Set<T> found) {
        if (center.getX() + radius < xMin || center.getX() - radius > xMax ||
                center.getY() + radius < yMin || center.getY() - radius > yMax) {
            return;
        }

        for (T p : elements) {
            double dist = Trie.getDistance(center.getX(), center.getY(), p.x(), p.y());
            if (dist <= radius) {
                found.add(p);
            }
        }
    }
}