package cachemap;

/**
 * Provides a way to override the system time.
 * Useful for unit-testing purposes.
 */
public class Clock {
    private static Long time;

    /**
     * Returns the system time if the time has not been set.
     */
    public static long getTime() {
        return time == null ? System.currentTimeMillis() : time;
    }

    /**
     * Sets the time. This will cause getTime() to return the given time
     * instead of the system time.
     */
    public static void setTime(long time) {
        Clock.time = time;
    }

    /**
     * Clears the time. This will cause getTime() to return the system time.
     */
    public static void clearTime() {
        Clock.time = null;
    }
}
