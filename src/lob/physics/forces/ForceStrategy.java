package lob.physics.forces;

import lob.physics.Vector2D;
import lob.physics.shapes.Shape;

public interface ForceStrategy {
    // Método base para o padrão Strategy de forças
    Vector2D getAcceleration(Shape shape);
}