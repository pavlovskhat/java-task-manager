package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility class executing input validations.
 * Logging information to the console.
 * Logging error message stack trace String
 * to .txt file.
 */
public class InputsLogs {
    private InputsLogs() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a string variable from a user input via a Scanner class
     * object.
     * @param prompt: String prompt to display to user.
     * @return String input from user.
     */
    public static String stringInput(String prompt) {

        Scanner stringScanner = new Scanner(System.in).useDelimiter("\n");
        print(prompt);
        return stringScanner.next();
    }

    /**
     * Returns an int variable from a user input via a Scanner class
     * object.
     * Error handling in place to handle input mismatch error.
     * @param prompt: String prompt to display to user.
     * @return valid int input from user.
     */
    public static int intInput(String prompt) {

        while (true) {
            Scanner intScanner = new Scanner(System.in).useDelimiter("\n");
            print(prompt);
            try {
                return intScanner.nextInt();
            } catch (InputMismatchException error) {
                addLog(Main.logPath, String.valueOf(error));
                print("!!! ERROR: INCORRECT INPUT !!!");
            }
        }
    }

    /**
     * Returns a LocalDate variable from a user input via a Scanner class
     * object.
     * Validates the input by applying ofPattern method to input.
     * @param prompt: String prompt to user input.
     * @return valid LocalDate input from user.
     */
    public static LocalDate getDate(String prompt) {

        while (true) {
            try {
                String stringDate = InputsLogs.stringInput(prompt);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(stringDate, format);
            } catch (Exception error) {
                addLog(Main.logPath, String.valueOf(error));
                print("!!! ERROR: INCORRECT FORMAT !!!");
            }
        }
    }

    /**
     * Outputs given String to console.
     * Placed here in case of logger class integration.
     * @param msg: String message to user.
     */
    public static void print(String msg) {

        System.out.print(msg);
    }

    /**
     * Writes given String to given String path.
     * @param path: File location String.
     * @param msg: String to write.
     */
    public static void addLog(String path, String msg) {
        LocalDateTime dateTime = LocalDateTime.now();
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(path, true)
            );
            writer.write(dateTime + " >>> " + msg + "\n");
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
