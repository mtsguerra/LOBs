package lob.physics.forces;

import lob.physics.Vector2D;
import lob.physics.shapes.Shape;

public class GravityStrategy implements ForceStrategy {

    private final Vector2D gravity;

    // O teste na linha 21 faz "new GravityStrategy()", por isso precisamos de um construtor vazio
    // que defina a gravidade padrão (0 para os lados, 9.8 para baixo).
    public GravityStrategy() {
        this.gravity = new Vector2D(0, 9.8);
    }

    // Também é boa prática ter um construtor que permita mudar a gravidade se quisermos
    public GravityStrategy(Vector2D gravity) {
        this.gravity = gravity;
    }

    @Override
    public Vector2D getAcceleration(Shape shape) {
        // Devolve o vetor da gravidade para puxar a forma para baixo!
        return gravity;
    }
}