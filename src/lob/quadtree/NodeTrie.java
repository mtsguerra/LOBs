package lob.quadtree;

import lob.physics.Vector2D;
import java.util.Set;

public class NodeTrie<T extends HasPoint> extends Trie<T> {
    private Trie<T> topLeft, topRight, bottomLeft, bottomRight;

    // Guardamos os limites para otimizar a procura
    private final double xMin, xMax, yMin, yMax;
    private final double xMid, yMid;

    public NodeTrie(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        this.xMid = (xMin + xMax) / 2;
        this.yMid = (yMin + yMax) / 2;

        // Inicializamos os quadrantes como folhas
        this.topLeft = new LeafTrie<>(xMin, xMid, yMin, yMid);
        this.topRight = new LeafTrie<>(xMid, xMax, yMin, yMid);
        this.bottomLeft = new LeafTrie<>(xMin, xMid, yMid, yMax);
        this.bottomRight = new LeafTrie<>(xMid, xMax, yMid, yMax);
    }

    @Override
    public Trie<T> insert(T element) {
        // Decidimos o quadrante com base no x e y do elemento
        if (element.x() < xMid) {
            if (element.y() < yMid)
                topLeft = topLeft.insert(element);
            else
                bottomLeft = bottomLeft.insert(element);
        } else {
            if (element.y() < yMid)
                topRight = topRight.insert(element);
            else
                bottomRight = bottomRight.insert(element);
        }
        return this;
    }

    @Override
    public void collectPoints(Vector2D center, double radius, Set<T> found) {
        // Otimização: Se o círculo nem toca na área total deste Nó, ignora os filhos
        if (center.getX() + radius < xMin || center.getX() - radius > xMax ||
                center.getY() + radius < yMin || center.getY() - radius > yMax) {
            return;
        }

        // Delegamos a procura aos 4 quadrantes
        topLeft.collectPoints(center, radius, found);
        topRight.collectPoints(center, radius, found);
        bottomLeft.collectPoints(center, radius, found);
        bottomRight.collectPoints(center, radius, found);
    }
}