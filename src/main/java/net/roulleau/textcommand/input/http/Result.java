package net.roulleau.textcommand.input.http;

import net.roulleau.textcommand.Report;

public class Result extends Report {

    private ResultStatus resultStatus = ResultStatus.UNDEFINED;
    private String detailedMessage;

    public Result() {        
    }
    
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    
    public Result(Report execute) {
        this.setMethodName(execute.getMethodName());
        this.setOriginalCommand(execute.getOriginalCommand());
        this.setReturnedObject(execute.getReturnedObject());
        this.setResultStatus(ResultStatus.OK);
    }
    
    public static enum ResultStatus {
        OK,
        NO_MATCH,
        EXECUTION_FAILED,
        UNDEFINED;
    }
}
