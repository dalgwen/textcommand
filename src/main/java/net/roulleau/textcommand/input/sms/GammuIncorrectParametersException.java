package net.roulleau.textcommand.input.sms;

public class GammuIncorrectParametersException extends Exception {

    private static final long serialVersionUID = -5283078993317452372L;

    private String originalTextCommand;
    
    public String getOriginalTextCommand() {
        return originalTextCommand;
    }

    GammuIncorrectParametersException(String message, String originalTextCommand) {
        super(message);
        this.originalTextCommand = originalTextCommand;
    }
    
}
