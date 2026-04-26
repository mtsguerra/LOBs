package lob.physics.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de ações pendentes (adiadas).
 * Evita modificar o mundo da física enquanto este está a iterar sobre as formas.
 */
public class PendingActions {

    // Lista que guarda as ações em espera
    private final List<ActionOnShapes> actions = new ArrayList<>();

    /**
     * Adiciona uma ação à lista de espera.
     * Resolve o erro da linha 41: cannot find symbol method defer
     */
    public void defer(ActionOnShapes action) {
        actions.add(action);
    }

    /**
     * Executa todas as ações guardadas pela ordem que foram agendadas
     * e depois limpa a lista.
     * Resolve o erro da linha 48: cannot find symbol method executePendingActions
     */
    public void executePendingActions() {
        for (ActionOnShapes action : actions) {
            // Nota: se o teu ActionOnShapes tiver um nome diferente para o método de executar
            // (por exemplo: action.apply() ou action.run()), podes precisar de ajustar a linha abaixo!
            action.execute();
        }

        // Limpa a lista para o próximo frame/turno
        actions.clear();
    }
}