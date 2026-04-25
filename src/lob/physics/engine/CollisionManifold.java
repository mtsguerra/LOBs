package lob.physics.engine;

import lob.physics.Vector2D;

/**
 * O CollisionManifold guarda os detalhes técnicos de uma colisão.
 */
public class CollisionManifold {
    private final boolean colliding;   // Novo campo necessário para os testes
    private final Vector2D normal;
    private final double penetration;

    // Construtor atualizado para incluir o estado da colisão
    public CollisionManifold(boolean colliding, Vector2D normal, double penetration) {
        this.colliding = colliding;
        this.normal = normal;
        this.penetration = penetration;
    }

    /**
     * Indica se houve colisão efetiva.
     * Resolve o erro: cannot find symbol method isColliding()
     */
    public boolean isColliding() {
        return colliding;
    }

    public Vector2D getNormal() {
        return normal;
    }

    public double getPenetration() {
        return penetration;
    }

    /**
     * Método utilitário para criar um objeto que representa a ausência de colisão.
     * Útil para retornar no CollisionManager quando os objetos estão longe.
     */
    public static CollisionManifold noCollision() {
        return new CollisionManifold(false, Vector2D.NULL_VECTOR, 0.0);
    }
}