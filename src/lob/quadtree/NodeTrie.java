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
        if (element.x() < xMid) {
            if (element.y() < yMid) topLeft = topLeft.insert(element);
            else bottomLeft = bottomLeft.insert(element);
        } else {
            if (element.y() < yMid) topRight = topRight.insert(element);
            else bottomRight = bottomRight.insert(element);
        }
        return this;
    }

    // ==========================================
    // --- NOVOS MÉTODOS EXIGIDOS PELOS TESTES ---
    // ==========================================

    /**
     * Procura e devolve um elemento na árvore.
     */
    public T find(T element) {
        if (element.x() < xMid) {
            if (element.y() < yMid) return topLeft.find(element);
            else return bottomLeft.find(element);
        } else {
            if (element.y() < yMid) return topRight.find(element);
            else return bottomRight.find(element);
        }
    }

    /**
     * Insere um elemento substituindo-o se já existir.
     */
    public Trie<T> insertReplace(T element) {
        if (element.x() < xMid) {
            if (element.y() < yMid) topLeft = topLeft.insertReplace(element);
            else bottomLeft = bottomLeft.insertReplace(element);
        } else {
            if (element.y() < yMid) topRight = topRight.insertReplace(element);
            else bottomRight = bottomRight.insertReplace(element);
        }
        return this;
    }

    /**
     * Remove um elemento da árvore.
     */
    public Trie<T> delete(T element) {
        if (element.x() < xMid) {
            if (element.y() < yMid) topLeft = topLeft.delete(element);
            else bottomLeft = bottomLeft.delete(element);
        } else {
            if (element.y() < yMid) topRight = topRight.delete(element);
            else bottomRight = bottomRight.delete(element);
        }
        return this;
    }

    /**
     * Variante do collectPoints usando coordenadas soltas (exigido na linha 66).
     */
    public void collectNear(double x, double y, double radius, Set<T> found) {
        // Otimização de colisão da caixa
        if (x + radius < xMin || x - radius > xMax ||
                y + radius < yMin || y - radius > yMax) {
            return;
        }

        topLeft.collectNear(x, y, radius, found);
        topRight.collectNear(x, y, radius, found);
        bottomLeft.collectNear(x, y, radius, found);
        bottomRight.collectNear(x, y, radius, found);
    }

    // --- MANTIDO DO TEU CÓDIGO ORIGINAL ---

    @Override
    public void collectPoints(Vector2D center, double radius, Set<T> found) {
        if (center.getX() + radius < xMin || center.getX() - radius > xMax ||
                center.getY() + radius < yMin || center.getY() - radius > yMax) {
            return;
        }
        topLeft.collectPoints(center, radius, found);
        topRight.collectPoints(center, radius, found);
        bottomLeft.collectPoints(center, radius, found);
        bottomRight.collectPoints(center, radius, found);
    }
}