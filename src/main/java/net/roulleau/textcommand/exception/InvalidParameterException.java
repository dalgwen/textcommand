package net.roulleau.textcommand.exception;

public class InvalidParameterException extends TextCommandTechnicalException {

    private static final long serialVersionUID = 1619716587419757274L;

    String parameterName;

    public String getParameterName() {
        return parameterName;
    }

    public InvalidParameterException(String parameterName) {
        super("Cannot find value for parameter " + parameterName);
        this.parameterName = parameterName;
    }

    @Override
    public String toString() {
        return "Cannot find value for parameter " + parameterName;
    }

}
