package reports;

import main.InputsLogs;

import java.io.File;

/**
 * Utility class containing helper
 * methods to support Reporting class.
 */
public class ReportHelpers {

    private ReportHelpers() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns result of a percentage calculation.
     * @param smallerNumber Smaller Integer value.
     * @param largerNumber Larger Integer value.
     * @return Calculated percentage value as String.
     */
    public static String percentage(Integer smallerNumber, Integer largerNumber) {

        float result = (smallerNumber.floatValue() / largerNumber.floatValue()) * 100;
        return String.format("%.0f", result);
    }

    /**
     * Returns relative path of given directory.
     * @return String relative path.
     */
    public static String getRelativePath(String path) {
        String localDir = System.getProperty("user.dir");
        return localDir + path;
    }

    /**
     * Returns array of files in reports directory.
     * @param path: String relative path to reports
     *            directory.
     * @return Array of File objects.
     */
    public static File[] compileReportList(String path) {
        File directory = new File(path);
        return directory.listFiles();
    }

    /**
     * Displays array File objects as strings.
     * @param fileArray: Array of File objects.
     */
    public static void viewAllReports(File[] fileArray) {
        StringBuilder displayString = new StringBuilder();
        displayString.append("*** REPORTS ***");
        int index = 1;
        if (fileArray != null) {
            for (File file : fileArray) {
                displayString.append("\n")
                        .append(index).append(") ")
                        .append(file.getName());
                index ++;
            }
        }
        InputsLogs.print(String.valueOf(displayString));
    }
}
