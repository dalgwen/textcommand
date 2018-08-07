package net.roulleau.textcommand.exception;

public class NoMatchingMethodFoundException extends CommandExecutionException {

    private static final long serialVersionUID = -2920397194331358608L;

    public NoMatchingMethodFoundException() {
        super("Cannot find matching method");
    }

}
