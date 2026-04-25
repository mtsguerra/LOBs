package lob.quadtree;

import lob.physics.Vector2D;
import java.util.Set;

public class NodeTrie extends Trie {
    private Trie topLeft;
    private Trie topRight;
    private Trie bottomLeft;
    private Trie bottomRight;

    private double xMid, yMid;

    public NodeTrie(double xMin, double xMax, double yMin, double yMax) {
        this.xMid = (xMin + xMax) / 2;
        this.yMid = (yMin + yMax) / 2;

        // Agora criamos as folhas passando as fronteiras de cada um dos 4 quadrantes!
        this.topLeft = new LeafTrie(xMin, xMid, yMin, yMid);
        this.topRight = new LeafTrie(xMid, xMax, yMin, yMid);
        this.bottomLeft = new LeafTrie(xMin, xMid, yMid, yMax);
        this.bottomRight = new LeafTrie(xMid, xMax, yMid, yMax);
    }

    @Override
    public Trie insert(Vector2D point) {
        if (point.getX() < xMid) {
            if (point.getY() < yMid)
                topLeft = topLeft.insert(point);
            else
                bottomLeft = bottomLeft.insert(point);
        } else {
            if (point.getY() < yMid)
                topRight = topRight.insert(point);
            else
                bottomRight = bottomRight.insert(point);
        }
        return this;
    }

    @Override
    public void collectPoints(Vector2D center, double radius, Set<Vector2D> found) {
        // (No futuro, poderás otimizar isto para não pesquisar em quadrantes vazios/distantes)
        topLeft.collectPoints(center, radius, found);
        topRight.collectPoints(center, radius, found);
        bottomLeft.collectPoints(center, radius, found);
        bottomRight.collectPoints(center, radius, found);
    }
}