package com.georgen.hawthorne.tools.logging;

/* DTO to pass the name of the class and method, that created this DTO, or higher in the call chain */
public class TraceInfo extends Throwable {
    /* StackTrace depth index, which returns the process name; 0 and up = most recent to earliest */
    private int traceIndex;
    private StackTraceElement[] stackTrace;

    public TraceInfo(int traceIndex){
        if (traceIndex < 0) traceIndex = 0;
        this.traceIndex = traceIndex;
    }

    public String getMethodName(){
        initStackTrace();
        String methodName = isValid(stackTrace) ? stackTrace[traceIndex].getMethodName() : null;
        return methodName != null ? methodName : "";
    }

    public String getClassName(){
        initStackTrace();
        String className = isValid(stackTrace) ? stackTrace[traceIndex].getClassName() : null;
        return className != null ? className : "";
    }

    private void initStackTrace(){
        if (!isValid(this.stackTrace)){
            this.stackTrace = this.getStackTrace();
        }
    }

    private static boolean isValid(StackTraceElement[] stackTrace){
        return stackTrace != null && stackTrace.length > 0;
    }
}
