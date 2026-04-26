package lob.physics.engine;

import lob.physics.Vector2D;

/**
 * O CollisionManifold guarda os detalhes técnicos de uma colisão.
 */
public class CollisionManifold {
    private final boolean colliding;
    private final Vector2D normal;
    private final double penetration;

    /**
     * Constante estática exigida pelo teste.
     * A normal é null para satisfazer o assertNull(CollisionManifold.NO_COLLISION.normal()).
     */
    public static final CollisionManifold NO_COLLISION = new CollisionManifold(false, null, 0.0);

    // --- CONSTRUTOR ORIGINAL (Normal primeiro) ---
    public CollisionManifold(boolean colliding, Vector2D normal, double penetration) {
        this.colliding = colliding;
        this.normal = normal;
        this.penetration = penetration;
    }

    // --- NOVO CONSTRUTOR (Penetração primeiro) ---
    public CollisionManifold(boolean colliding, double penetration, Vector2D normal) {
        this.colliding = colliding;
        this.penetration = penetration;
        this.normal = normal;
    }

    public boolean isColliding() {
        return colliding;
    }

    // --- MÉTODOS EXIGIDOS PELOS TESTES (Nomes curtos) ---

    public Vector2D normal() {
        return normal;
    }

    public double penetration() {
        return penetration;
    }

    // --- GETTERS CLÁSSICOS (Mantidos por compatibilidade) ---

    public Vector2D getNormal() {
        return normal;
    }

    public double getPenetration() {
        return penetration;
    }

    /**
     * Retorna a constante de ausência de colisão.
     */
    public static CollisionManifold noCollision() {
        return NO_COLLISION;
    }
}