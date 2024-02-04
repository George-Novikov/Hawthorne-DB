package tools.logging;

import org.slf4j.Logger;

/* Inheritors of this interface can monitor and log their own actions */
public interface SelfTracking {
    default String start(){ return Tracker.getCallerInfoLine(2); }

    default void start(Logger logger){ logger.info(Tracker.getCallerInfoLine(2)); }
}
