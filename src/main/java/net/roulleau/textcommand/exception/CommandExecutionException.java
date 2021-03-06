package net.roulleau.textcommand.exception;

import net.roulleau.textcommand.Report;

public class CommandExecutionException extends Exception implements CommonPartException{
    
    private static final long serialVersionUID = -4771464419529549917L;
    String message;    
    Report partialReport;

    public CommandExecutionException(String message, Exception cause) {
        super(cause);
        this.message = message;
    }
    
    public CommandExecutionException(String message) {
        this.message = message;
    }
    
    @Override
    public Report getPartialReport() {
        return partialReport != null ? partialReport : new Report();
    }

    public void setPartialReport(Report partialReport) {
        this.partialReport = partialReport;
    }
    
    @Override
    public String toString() {
        return message + (getCause() !=null ?". Cause : " + getCause().toString():"");
    }
    
}
