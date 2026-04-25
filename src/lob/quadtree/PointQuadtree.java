package lob.quadtree;

import lob.physics.Vector2D;
import java.util.HashSet;
import java.util.Set;

// A classe agora é genérica para aceitar qualquer "HasPoint"
public class PointQuadtree<T extends HasPoint> {

    private Trie<T> root;

    // O teste envia 4 coordenadas para definir o tamanho inicial do mundo
    public PointQuadtree(double xMin, double yMin, double xMax, double yMax) {
        // Inicializamos a árvore com uma folha que cobre toda essa área.
        // Lembra-te que o nosso LeafTrie pede a ordem (xMin, xMax, yMin, yMax)
        this.root = new LeafTrie<>(xMin, xMax, yMin, yMax);
    }

    public void insert(T element) {
        this.root = this.root.insert(element);
    }

    public Set<T> find(Vector2D center, double radius) {
        Set<T> found = new HashSet<>();
        root.collectPoints(center, radius, found);
        return found;
    }
}