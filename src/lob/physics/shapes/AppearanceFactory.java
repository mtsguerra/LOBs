package lob.physics.shapes;

public interface AppearanceFactory {

    /**
     * Método que devolve a aparência associada a um nome.
     * Ex: getAppearance("ball") pode devolver uma cor azul.
     */
    Appearance getAppearance(String shapeName);
}