package lob;

public class LotsOfBallsException extends Exception {

    // 1. Vazio
    public LotsOfBallsException() {
        super();
    }

    // 2. Só mensagem
    public LotsOfBallsException(String message) {
        super(message);
    }

    // 3. Mensagem + Causa
    public LotsOfBallsException(String message, Throwable cause) {
        super(message, cause);
    }

    // 4. Só causa
    public LotsOfBallsException(Throwable cause) {
        super(cause);
    }

    // 5. COMPLETO (Resolve o erro da linha 105: String, Throwable, boolean, boolean)
    public LotsOfBallsException(String message, Throwable cause,
                                boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}