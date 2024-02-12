package com.georgen.hawthorne.tools.logging;

/* This class returns a method and class name for tracking library actions */
public class Tracker {
    private static final int DEFAULT_TRACE_INDEX = 1;
    private static final String PROCESS_START_PREFIX = "****";

    /* The name of the process one level higher than this class */
    public static String getCallerInfo(){
        TraceInfo traceInfo = new TraceInfo(DEFAULT_TRACE_INDEX);
        return getCallerInfo(traceInfo);
    }

    /* Process name by the stackTrace index */
    public static String getCallerInfo(int traceIndex){
        TraceInfo traceInfo = new TraceInfo(traceIndex);
        return getCallerInfo(traceInfo);
    }

    /* Log string: prefix and name of the process one level higher than this class */
    public static String getCallerInfoLine(){
        TraceInfo traceInfo = new TraceInfo(DEFAULT_TRACE_INDEX);
        return getCallerInfoLine(traceInfo);
    }

    /* Log string: prefix and process name by the stackTrace index */
    public static String getCallerInfoLine(int traceIndex){
        TraceInfo traceInfo = new TraceInfo(traceIndex);
        return getCallerInfoLine(traceInfo);
    }

    private static String getCallerInfo(TraceInfo traceInfo){
        return String.format(
                "%s.%s()",
                traceInfo.getClassName(),
                traceInfo.getMethodName()
        );
    }

    private static String getCallerInfoLine(TraceInfo traceInfo){
        return String.format(
                "%s %s",
                PROCESS_START_PREFIX,
                getCallerInfo(traceInfo)
        );
    }
}
