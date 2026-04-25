package lob.physics.forces;

import lob.physics.Vector2D;
import lob.physics.shapes.Shape;
import lob.physics.shapes.Circle;

public class FrictionStrategy implements ForceStrategy {
    private final double friction;

    public FrictionStrategy(double friction) {
        this.friction = friction;
    }

    @Override
    public Vector2D getAcceleration(Shape s) {
        // O atrito só faz sentido para objetos que se movem (como o Circle)
        if (s instanceof Circle) {
            Vector2D v = ((Circle) s).getVelocity();

            // Se a bola estiver parada, não há atrito
            if (v.getX() == 0 && v.getY() == 0) {
                return new Vector2D(0, 0);
            }

            // Aceleração = Direção oposta da velocidade * coeficiente de atrito
            // a = -friction * normalize(v)
            return v.normalize().multiply(-friction);
        }
        return new Vector2D(0, 0);
    }
}