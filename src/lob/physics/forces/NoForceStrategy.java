package lob.physics.forces;

import lob.physics.Vector2D;
import lob.physics.shapes.Shape;

public class NoForceStrategy implements ForceStrategy {

    @Override
    public Vector2D getAcceleration(Shape shape) {
        // Como o nome indica ("No Force"), esta estratégia não aplica aceleração nenhuma.
        // Portanto, devolvemos o vetor nulo que criámos há pouco!
        return Vector2D.NULL_VECTOR;
    }
}