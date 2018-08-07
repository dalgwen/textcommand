package net.roulleau.textcommand.exception;

public class MethodDeclarationException extends RuntimeException {

    private static final long serialVersionUID = -516673667700556543L;
    String message;
    
    public MethodDeclarationException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
    
}
