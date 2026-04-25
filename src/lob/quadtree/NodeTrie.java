package lob.quadtree;

import lob.physics.Vector2D;
import java.util.Set;

public class NodeTrie<T extends HasPoint> extends Trie<T> {
    private Trie<T> topLeft;
    private Trie<T> topRight;
    private Trie<T> bottomLeft;
    private Trie<T> bottomRight;

    private double xMid, yMid;

    public NodeTrie(double xMin, double xMax, double yMin, double yMax) {
        this.xMid = (xMin + xMax) / 2;
        this.yMid = (yMin + yMax) / 2;

        this.topLeft = new LeafTrie<>(xMin, xMid, yMin, yMid);
        this.topRight = new LeafTrie<>(xMid, xMax, yMin, yMid);
        this.bottomLeft = new LeafTrie<>(xMin, xMid, yMid, yMax);
        this.bottomRight = new LeafTrie<>(xMid, xMax, yMid, yMax);
    }

    @Override
    public Trie<T> insert(T element) {
        // Usamos element.x() e element.y() da interface HasPoint
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
        topLeft.collectPoints(center, radius, found);
        topRight.collectPoints(center, radius, found);
        bottomLeft.collectPoints(center, radius, found);
        bottomRight.collectPoints(center, radius, found);
    }
}