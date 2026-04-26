package lob.physics.engine;

import lob.physics.shapes.Shape;
import lob.physics.events.CollisionEvent;
import lob.physics.events.PhysicsObserver;

/**
 * Interface que define como as colisões são detetadas e resolvidas.
 * Atualizada para satisfazer os requisitos do PhysicsWorldTest (Mock).
 */
public interface CollisionManager {

    /** * Verifica se existe colisão entre duas formas.
     */
    CollisionManifold checkCollision(Shape s1, Shape s2);

    /** * Resolve a resposta física (velocidade/posição) após uma colisão.
     */
    Shape resolveCollision(Shape colliding, Shape collided, CollisionManifold collision);

    /** * Adiciona um observador para eventos de colisão de uma forma específica.
     */
    void addCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer);

    // --- NOVOS MÉTODOS EXIGIDOS PELO TESTE (MOCK) ---

    /** * Remove um observador específico. Resolve o erro da linha 112 do teste.
     */
    void removeCollisionListener(Shape shape, PhysicsObserver<CollisionEvent> observer);

    /** * Remove todos os observadores associados a uma forma. Resolve o erro da linha 117 do teste.
     */
    void removeAllCollisionListeners(Shape shape);

    /** * Retorna o coeficiente de restituição (elasticidade). Resolve o erro da linha 122 do teste.
     */
    double getRestitution();

    /** * Define o coeficiente de restituição. Resolve o erro da linha 127 do teste.
     */
    void setRestitution(double restitution);
}