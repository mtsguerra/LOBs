package lob.quadtree;

import lob.physics.Vector2D;
import java.util.Set;

public abstract class Trie {
    protected static int capacity = 10;

    public static void setCapacity(int cap) { capacity = cap; }
    public static int getCapacity() { return capacity; }

    public abstract Trie insert(Vector2D point);
    public abstract void collectPoints(Vector2D center, double radius, Set<Vector2D> found);
}