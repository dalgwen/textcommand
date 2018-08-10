package net.roulleau.textcommand.exception;

import net.roulleau.textcommand.Report;

public class TextCommandTechnicalException extends RuntimeException implements CommonPartException{

    private static final long serialVersionUID = -5950075415094250739L;
    String message;    
    Report partialReport;

    public TextCommandTechnicalException(String message, Exception cause) {
        super(cause);
        this.message = message;
    }
    
    public TextCommandTechnicalException(String message) {
        this.message = message;
    }
    
    @Override
    public Report getPartialReport() {
        return partialReport;
    }

    public void setPartialReport(Report partialReport) {
        this.partialReport = partialReport;
    }
    
    @Override
    public String toString() {
        return message + (getCause() !=null ?". Cause : " + getCause().toString():"");
    }
}
