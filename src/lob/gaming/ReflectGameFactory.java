package lob.gaming;

import java.lang.reflect.InvocationTargetException;

public class ReflectGameFactory {
    // Definido na FAQ: O Loader para carregar as classes
    private final static ClassLoader LOADER = ReflectGameFactory.class.getClassLoader();

    // Este método cria a instância de um jogo dado o nome da classe
    // Ex: "lob.gaming.games.Cannon"
    public Object createInstance(String className) throws Exception {
        try {
            Class<?> clazz = LOADER.loadClass(className);
            // Usa reflexão para chamar o construtor vazio
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new Exception("Erro ao carregar o jogo: " + className, e);
        }
    }
}