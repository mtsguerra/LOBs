package lob.physics.actions;

import lob.physics.engine.PhysicsWorld;

/**
 * Ação que limpa todos os elementos do mundo físico.
 * Resolve os erros do ResetActionTest.
 */
public class ResetAction {
    private final PhysicsWorld world;

    public ResetAction(PhysicsWorld world) {
        this.world = world;
    }

    /**
     * O teste espera que esta ação possa ser executada.
     */
    public void execute() {
        world.reset();
    }
}