package net.roulleau.textcommand.exception;

public class InvocationExecutionException extends CommandExecutionException {

    private static final long serialVersionUID = 1015193675812324793L;

    public InvocationExecutionException(String message) {
        super(message);
    }
    
    public InvocationExecutionException(String message, Exception cause) {
        super(message, cause);
    }


}
