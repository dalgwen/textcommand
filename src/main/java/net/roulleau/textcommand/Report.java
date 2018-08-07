package net.roulleau.textcommand;

public class Report {

    protected String originalCommand;
    
    protected String methodName;

    protected Object returnedObject;
 
    public String getOriginalCommand() {
        return originalCommand;
    }

    public void setOriginalCommand(String originalCommand) {
        this.originalCommand = originalCommand;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getReturnedObject() {
        return returnedObject;
    }

    public void setReturnedObject(Object returnedObject) {
        this.returnedObject = returnedObject;
    }
    
    @Override
    public String toString() {
        return "Report [originalCommand=" + originalCommand + ", methodName=" + methodName + ", returnedObject=" + returnedObject + "]";
    }

}

