package lob.quadtree;

import lob.physics.Vector2D;
import java.util.Set;

public abstract class Trie<T extends HasPoint> {
    protected static int capacity = 10;

    public static void setCapacity(int cap) { capacity = cap; }
    public static int getCapacity() { return capacity; }

    public abstract Trie<T> insert(T element);

    // ==========================================
    // --- NOVOS MÉTODOS ABSTRATOS EXIGIDOS ---
    // ==========================================
    public abstract T find(T element);
    public abstract Trie<T> insertReplace(T element);
    public abstract Trie<T> delete(T element);
    public abstract void collectNear(double x, double y, double radius, Set<T> found);

    // ==========================================

    public static double getDistance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public abstract void collectPoints(Vector2D center, double radius, Set<T> found);
}