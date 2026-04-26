package lob.quadtree;

import lob.physics.Vector2D;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator; // Import necessário para o for-each

/**
 * Fachada para a estrutura Quadtree que gere pontos no espaço.
 * Implementa Iterable para permitir o uso em loops for-each.
 */
public class PointQuadtree<T extends HasPoint> implements Iterable<T> {

    private Trie<T> root;
    private final double xMin, yMin, xMax, yMax;

    public PointQuadtree(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;

        // Inicializamos a árvore com uma folha.
        this.root = new LeafTrie<>(xMin, xMax, yMin, yMax);
    }

    /**
     * Permite usar a Quadtree num loop for-each.
     * Resolve o erro: for-each not applicable to expression type
     */
    @Override
    public Iterator<T> iterator() {
        return getAll().iterator();
    }

    /**
     * Insere um elemento e verifica se está dentro dos limites.
     */
    public void insert(T element) {
        checkBoundaries(element);
        this.root = this.root.insert(element);
    }

    /**
     * Insere um elemento, substituindo-o se já existir na mesma posição.
     */
    public void insertReplace(T element) {
        checkBoundaries(element);
        this.root = this.root.insertReplace(element);
    }

    /**
     * Valida se o ponto está dentro do mapa.
     * Nota: Usei PointOutOfBoundsException (com 's') para bater certo com o teste.
     */
    private void checkBoundaries(T element) {
        if (element.x() < xMin || element.x() > xMax ||
                element.y() < yMin || element.y() > yMax) {
            throw new PointOutOfBoundException();
        }
    }

    public T find(T element) {
        return root.find(element);
    }

    public void delete(T element) {
        this.root = this.root.delete(element);
    }

    public Set<T> findNear(double x, double y, double radius) {
        Set<T> found = new HashSet<>();
        root.collectNear(x, y, radius, found);
        return found;
    }

    public Set<T> find(Vector2D center, double radius) {
        Set<T> found = new HashSet<>();
        root.collectPoints(center, radius, found);
        return found;
    }

    /**
     * Devolve todos os elementos guardados na árvore.
     */
    public java.util.Collection<T> getAll() {
        Set<T> all = new HashSet<>();
        double centerX = (xMin + xMax) / 2;
        double centerY = (yMin + yMax) / 2;
        double maxRadius = Math.max(xMax - xMin, yMax - yMin) * 2; // Raio extra por segurança

        root.collectNear(centerX, centerY, maxRadius, all);
        return all;
    }
}