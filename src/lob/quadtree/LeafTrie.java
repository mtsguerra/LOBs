package lob.quadtree;

import lob.physics.Vector2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LeafTrie extends Trie {
    private List<Vector2D> points = new ArrayList<>();

    // A folha precisa de saber as suas fronteiras para quando tiver de se dividir!
    private double xMin, xMax, yMin, yMax;

    public LeafTrie(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    @Override
    public Trie insert(Vector2D point) {
        // Se ainda há espaço, adiciona o ponto
        if (points.size() < capacity) {
            points.add(point);
            return this;
        }

        // Se excedeu a capacidade, cria um novo Node com as MESMAS fronteiras desta folha
        NodeTrie newNode = new NodeTrie(xMin, xMax, yMin, yMax);

        // Redistribui os pontos antigos da folha para o novo nó
        for (Vector2D p : points) {
            newNode.insert(p);
        }

        // Insere o novo ponto e retorna o Node (que agora substitui esta folha na árvore)
        return newNode.insert(point);
    }

    @Override
    public void collectPoints(Vector2D center, double radius, Set<Vector2D> found) {
        for (Vector2D p : points) {
            // Verifica se o ponto está dentro do círculo de pesquisa
            if (p.add(center.scale(-1)).norm() <= radius) {
                found.add(p);
            }
        }
    }
}