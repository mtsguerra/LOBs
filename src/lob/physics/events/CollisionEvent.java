package lob.physics.events;

import lob.physics.shapes.Shape;
import lob.physics.engine.CollisionManifold;

/**
 * Representa o evento de duas formas a baterem uma na outra.
 * Resolve o erro: actual and formal argument lists differ in length
 */
public record CollisionEvent(Shape colliding, Shape collided, CollisionManifold collision) implements PhysicsEvent {
    // O Java record cria automaticamente os métodos colliding(), collided() e collision()
    // que o teste exige nas linhas 56, 57 e 58.
}