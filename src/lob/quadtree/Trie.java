package lob.quadtree;

import lob.physics.Vector2D;
import java.util.Set;

public abstract class Trie<T extends HasPoint> {
    protected static int capacity = 10;

    public static void setCapacity(int cap) { capacity = cap; }
    public static int getCapacity() { return capacity; }

    // O insert agora aceita T em vez de apenas Vector2D
    public abstract Trie<T> insert(T element);

    public static double getDistance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // O found agora guarda elementos do tipo T
    public abstract void collectPoints(Vector2D center, double radius, Set<T> found);
}