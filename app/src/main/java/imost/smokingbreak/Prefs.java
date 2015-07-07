package imost.smokingbreak;

/**
 * Temporary class for storing constants and data
 */
public class Prefs {
    public final static long NONSTOP_WORK_DURATION = 1000L * 60L;// 1 minute//1000L * 60L * 60L * 2L; // 2 hours;
    public final static long BREAK_DURATION = 1000L * 60L * 5L; // 5 minutes
    private static Long startedWorkingAt;
    private static Long startedBreakAt;
    private static boolean working = false;

    public static boolean isWorking() {
        return working;
    }
    public static void breakStarted() {
        startedBreakAt = System.currentTimeMillis();
        working = false;
    }

    public static void workingStarted() {
        startedWorkingAt = System.currentTimeMillis();
        working = true;
    }

    public static long getWorkingFor() {
        return !working || startedWorkingAt == null ? 0
                : (System.currentTimeMillis() - startedWorkingAt);
    }

    public static String workingTimeToStr() {
        long workingFor = getWorkingFor();
        if (workingFor <= 0) {
            return "00:00";
        }
        workingFor /= 1000L * 60L; // in minutes;
        StringBuilder ret = new StringBuilder();
        if (workingFor / 60 < 10) {
           ret.append("0");
        }
        ret.append(workingFor / 60);
        ret.append(":");
        if (workingFor % 60 < 10) {
            ret.append("0");
        }
        ret.append(workingFor % 60);
        return ret.toString();
    }

    public static long getBreakTimeLeft() {
        long breakTimeLeft = BREAK_DURATION;
        if (!working && startedBreakAt != null) {
            breakTimeLeft -= System.currentTimeMillis() - startedBreakAt;
        }
        return breakTimeLeft;
    }
    public static String breakTimeToStr() {
        long breakTimeLeft = getBreakTimeLeft();
        if (breakTimeLeft <= 0) {
            return "00:00";
        }
        breakTimeLeft /= 1000L; //in seconds;
        StringBuilder ret = new StringBuilder();
        if (breakTimeLeft / 60 < 10) {
            ret.append("0");
        }
        ret.append(breakTimeLeft / 60).append(":");
        if (breakTimeLeft % 60 < 10) {
            ret.append("0");
        }
        ret.append(breakTimeLeft % 60);
        return ret.toString();
    }

    public static boolean isTimeToBreak() {
        return working && getWorkingFor() > NONSTOP_WORK_DURATION;
    }

    public static boolean isTimeToWork() {
        return !working && getBreakTimeLeft() < 0;
    }
}
