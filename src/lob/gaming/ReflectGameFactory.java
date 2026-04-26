package lob.gaming;

import lob.LotsOfBallsException;
import java.nio.file.Path;
import java.util.*;

/**
 * Fábrica que utiliza Reflexão para encontrar e instanciar jogos.
 * Ajustada para compatibilidade estrita com Class<T>.
 */
public class ReflectGameFactory<T extends GameAnimation> {

    // Mudamos para Class<T> para bater certo com a expectativa do teste
    private final Map<String, Class<T>> gameClasses = new HashMap<>();

    public ReflectGameFactory() {
    }

    public Set<String> getAvailableGameNames() {
        return gameClasses.keySet();
    }

    public T getGame(String name) throws LotsOfBallsException {
        Class<T> clazz = gameClasses.get(name);
        if (clazz == null) throw new LotsOfBallsException("Jogo não encontrado: " + name);
        return createInstance(clazz);
    }

    /**
     * Carrega uma classe e retorna como Class<T>.
     * Resolve o erro: incompatible types java.lang.Class<? extends T>
     */
    @SuppressWarnings("unchecked")
    public Class<T> loadGameClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Class<T> gameClass = (Class<T>) clazz;

            String gameName = getGameName(gameClass);
            if (gameName != null) {
                gameClasses.put(gameName, gameClass);
            }
            return gameClass;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Class<T> loadGameClass(Path path) {
        return loadGameClass(getClassName(path));
    }

    public void loadGameClass(Class<T> clazz, String name) {
        gameClasses.put(name, clazz);
    }

    public String getGameName(Class<T> clazz) {
        try {
            return (String) clazz.getField("GAME_NAME").get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public T createInstance(Class<T> clazz) throws LotsOfBallsException {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new LotsOfBallsException("Erro ao instanciar: " + clazz.getName());
        }
    }

    public void collectGameClassesFromPackage(String packageName) {
        // Esqueleto para compilar
    }

    public String getClassName(Path filePath) {
        String fileName = filePath.getFileName().toString();
        return fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
    }
}