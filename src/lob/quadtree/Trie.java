package lob.quadtree;

import lob.physics.Vector2D;
import java.util.Set;

public abstract class Trie<T extends HasPoint> {
    protected static int capacity = 10;

    public static void setCapacity(int cap) { capacity = cap; }
    public static int getCapacity() { return capacity; }

    // O insert agora aceita T em vez de apenas Vector2D
    public abstract Trie<T> insert(T element);

    // O found agora guarda elementos do tipo T
    public abstract void collectPoints(Vector2D center, double radius, Set<T> found);
}