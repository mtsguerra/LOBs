package lob.gaming;

import java.lang.reflect.InvocationTargetException;

// 1. Adicionamos o Genérico aqui em cima para o teste parar de reclamar
public class ReflectGameFactory<T extends GameAnimation> {

    private final static ClassLoader LOADER = ReflectGameFactory.class.getClassLoader();

    // Construtor vazio para o teste da linha 32 (new ReflectGameFactory<>())
    public ReflectGameFactory() {
    }

    // 2. Mudamos o retorno de "Object" para "T"
    @SuppressWarnings("unchecked") // Isto diz ao Java "confia em mim, o cast é seguro"
    public T createInstance(String className) throws Exception {
        try {
            Class<?> clazz = LOADER.loadClass(className);
            // 3. Fazemos o "cast" (T) do objeto criado para o tipo Genérico do jogo
            return (T) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new Exception("Erro ao carregar o jogo: " + className, e);
        }
    }
}