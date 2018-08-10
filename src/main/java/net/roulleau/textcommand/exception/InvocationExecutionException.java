package net.roulleau.textcommand.exception;

import java.lang.reflect.InvocationTargetException;

public class InvocationExecutionException extends CommandExecutionException {

    private static final long serialVersionUID = 1015193675812324793L;

    public InvocationExecutionException(String message) {
        super(message);
    }
    
    public InvocationExecutionException(String message, Exception cause) {
        super(message, cause);
    }

    public String getDetailedMessage() {
        if (getCause() instanceof InvocationTargetException) {
            String originalExceptionMessage = ((InvocationTargetException) getCause()).getTargetException().getMessage();
            return originalExceptionMessage;
        } else {
            return getCause().getMessage();
        }
    }

}
