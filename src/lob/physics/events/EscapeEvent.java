package lob.physics.events;

import lob.physics.shapes.Shape;

/**
 * Representa o evento de uma forma a sair dos limites do mundo.
 * Resolve o erro: actual and formal argument lists differ in length
 */
public record EscapeEvent(Shape escaped, Shape boundary) implements PhysicsEvent {
    // O record cria automaticamente os métodos escaped() e boundary()
    // exigidos pelas linhas 46 e 47 do teu teste.
}