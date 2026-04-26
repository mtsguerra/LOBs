package lob.quadtree;

/**
 * Exceção lançada quando se tenta inserir um ponto fora da área da Quadtree.
 */
public class PointOutOfBoundException extends RuntimeException {
    public PointOutOfBoundException() {
        super("Ponto fora dos limites!");
    }
}