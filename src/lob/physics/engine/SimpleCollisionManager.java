package lob.physics.engine;

import lob.physics.Vector2D;
import lob.physics.shapes.Circle;
import lob.physics.shapes.Rectangle;

/**
 * O SimpleCollisionManager é responsável por detetar interseções entre formas
 * e calcular os dados da colisão (Manifold).
 */
public class SimpleCollisionManager implements CollisionManager {

    public SimpleCollisionManager() {
        // Construtor vazio conforme exigido pelo teste na linha 25
    }

    /**
     * Deteta colisão entre dois círculos.
     * Resolve o erro da linha 57 do SimpleCollisionManagerTest.
     */
    public CollisionManifold checkCollision(Circle a, Circle b) {
        // Vetor que aponta do centro de A para o centro de B
        Vector2D delta = b.position().minus(a.position());
        double distance = delta.length();

        // A soma dos raios é a distância mínima para NÃO haver colisão
        double totalRadius = a.radius() + b.radius();

        if (distance < totalRadius) {
            // Colisão detetada!
            // Se os centros forem idênticos (distância zero), inventamos uma normal
            Vector2D normal = (distance > 0) ? delta.normalize() : new Vector2D(1, 0);

            // Penetração é o quanto as bolas "entraram" uma na outra
            double penetration = totalRadius - distance;

            return new CollisionManifold(true, normal, penetration);
        }

        return CollisionManifold.noCollision();
    }

    /**
     * Deteta colisão entre um círculo e um retângulo.
     */
    public CollisionManifold checkCollision(Circle c, Rectangle r) {
        // Encontramos o ponto no retângulo mais próximo do centro do círculo
        Vector2D closest = r.closestPoint(c.position());
        Vector2D delta = closest.minus(c.position());
        double distance = delta.length();

        if (distance < c.radius()) {
            // A normal aponta do círculo para o ponto de colisão
            Vector2D normal = (distance > 0) ? delta.normalize() : new Vector2D(0, -1);
            double penetration = c.radius() - distance;

            return new CollisionManifold(true, normal, penetration);
        }

        return CollisionManifold.noCollision();
    }

    /**
     * Sobrecarga para tratar a ordem inversa (Retângulo vs Círculo).
     */
    public CollisionManifold checkCollision(Rectangle r, Circle c) {
        CollisionManifold manifold = checkCollision(c, r);
        if (manifold.isColliding()) {
            // Se invertermos a ordem dos objetos, temos de inverter a normal
            return new CollisionManifold(true, manifold.getNormal().multiply(-1), manifold.getPenetration());
        }
        return manifold;
    }
}