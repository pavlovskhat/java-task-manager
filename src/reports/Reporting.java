package reports;

import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import main.InputsLogs;
import main.Main;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import task.Task;
import user.User;

/**
 * Report class for generating and
 * viewing reports.
 */
public class Reporting {

    private Reporting() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Method to calculate reporting statistics for each program
     * user.
     * > Number of tasks per user.
     * > Percentage of tasks assigned to user.
     * > Percentage of tasks finalized.
     * > Percentage of tasks pending.
     * > Percentage of tasks overdue.
     */
    public static void userReports() {

        Task[] taskArray = Main.getTaskArray();
        User[] userArray = Main.getUserArray();
        Integer totalTasks = taskArray.length;
        String[] reportArray;
        HashMap<Integer, String[]> workbookMap = new HashMap<>();
        String[] hashmapHeader = {
                "user",
                "total_tasks",
                "%_assigned",
                "%_finalized",
                "%_pending",
                "%_overdue"
        };
        workbookMap.put(0, hashmapHeader);
        int rowIndex = 0;
        for (User user : userArray) {
            rowIndex++;
            Integer taskCount = user.getTaskCount();
            Integer userFinalized = 0;
            Integer userPending = 0;
            Integer userOverdue = 0;
            for (Task task : taskArray) {
                if (!task.getStatus() && task.getAssigned().equals(user.getUserName())) {
                    userPending++;
                    if (LocalDate.now().isAfter(task.getDueDate())) {
                        userOverdue++;
                    }
                } else if (task.getStatus() && task.getAssigned().equals(user.getUserName())) {
                    userFinalized++;
                }
                reportArray = new String[]{
                        user.getUserName(),
                        String.valueOf(user.getTaskCount()),
                        ReportHelpers.percentage(taskCount, totalTasks),
                        ReportHelpers.percentage(userFinalized, taskCount),
                        ReportHelpers.percentage(userPending, taskCount),
                        ReportHelpers.percentage(userOverdue, userPending)
                };
                workbookMap.put(rowIndex, reportArray);
            }
        }
        workbookCompiler(workbookMap);
    }

    /**
     * Writes prepared report data to external xlsx file with current
     * date added to file name.
     * @param workbook: Prepared Workbook object.
     */
    public static void reportFileWriter(Workbook workbook) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDate today = LocalDate.now();
        String todayString = formatter.format(today);
        String fileName = String.format("reports/%s_user_report.xlsx", todayString);
        try {
            workbook.save(fileName);
            Loggers.printInfo("*** REPORT GENERATED ***");
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * Method to compile a xlsx worksheet to be used to write the reporting
     * data to a xlsx document.
     * @param workbookMap: Hashmap key: row index; value: reporting data array.
     */
    public static void workbookCompiler(Map<Integer, String[]> workbookMap) {

        Set<Integer> keySet = workbookMap.keySet();
        int colIndex = (workbookMap.get(0).length) - 1;
        Workbook workbook = new Workbook();
        Worksheet worksheet = workbook.getWorksheets().get(0);
        for (int key : keySet) {
            String[] currentRow = workbookMap.get(key);
            for (int index = 0; index <= colIndex; index++) {
                if (index == 0 || key == 0) {
                    worksheet.getCells().get(key, index).putValue(currentRow[index]);
                } else {
                    int numberValue = Integer.parseInt(currentRow[index]);
                    worksheet.getCells().get(key, index).putValue(numberValue);
                }
            }
        }
        reportFileWriter(workbook);
    }

    /**
     * Reports main menu.
     * Requests validated integer input.
     * Matches input with switch case.
     */
    public static void reportMenu() {

        String menu = """
                *** REPORT MENU ***
                1) Generate report
                2) View reports
                3) Return to main menu
                ==================
                Select option >_
                """;
        while (true) {
            String choice = InputsLogs.stringInput(menu);
            switch (choice) {
                case "1" -> userReports();
                case "2" -> reportViewMenu();
                case "3" -> {
                    return;
                }
                default -> Loggers.printWarning("!!! INVALID CHOICE !!!");
            }
        }
    }

    /**
     * Displays all report files.
     * Requests and validates integer input.
     * Input selects report file index.
     */
    public static void reportViewMenu() {

        File[] fileArray = ReportHelpers.compileReportList(
                ReportHelpers.getRelativePath("\\reports"));
        ReportHelpers.viewAllReports(fileArray);
        int fileChoice = (InputsLogs.intInput("Select file number >_ ") - 1);
        File file = fileArray[fileChoice];
        reportMapGenerator(file);
    }

    /**
     * Generates Map object from given File object.
     * > Key: Integer.
     * > Value: Array of cell Strings.
     * @param file File object.
     */
    public static void reportMapGenerator(File file) {

        try {
            DataFormatter formatter = new DataFormatter();
            FileInputStream read = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(read);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Map<Integer, String[]> displayMap = new HashMap<>();
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                String[] tableRow = new String[6];
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellIndex = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    tableRow[cellIndex] = formatter.formatCellValue(cell);
                    cellIndex ++;
                }
                displayMap.put(rowIndex, tableRow);
                rowIndex ++;
            }
            displaySpecificReport(displayMap);
            read.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    /**
     * Employs Formatter to display report file as table.
     * @param tableMap: Map containing report information.
     */
    public static void displaySpecificReport(Map<Integer, String[]> tableMap) {

        String tableBanner = "-".repeat(100);
        Set<Integer> keySet = tableMap.keySet();
        Formatter tableFormatter = new Formatter();
        for (Integer key : keySet) {
            String[] currentRow = tableMap.get(key);
            tableFormatter.format(
                    tableBanner + "\n|%15s| %15s| %15s| %15s| %15s| %15s|\n",
                    currentRow[0],
                    currentRow[1],
                    currentRow[2],
                    currentRow[3],
                    currentRow[4],
                    currentRow[5]
            );
        }
        tableFormatter.format(tableBanner);
        System.out.print(tableFormatter + "\n");
    }
}
