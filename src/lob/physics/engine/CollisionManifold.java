package lob.physics.engine;

import lob.physics.Vector2D;

public class CollisionManifold {
    private final Vector2D normal;
    private final double penetration;

    public CollisionManifold(Vector2D normal, double penetration) {
        this.normal = normal;
        this.penetration = penetration;
    }

    public Vector2D getNormal() {
        return normal;
    }

    public double getPenetration() {
        return penetration;
    }
}