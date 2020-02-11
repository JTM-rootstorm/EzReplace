package layout;

import function.Enums;
import function.Functions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Scanner;

public class CLIForm {
    private CLIForm() {}

    public static void nogui() {
        String choice;
        do {
            getUserInput();

            List<String> results = Functions.gatherResults();
            String finalString = Functions.generateFinalString(results);

            System.out.println(finalString);
            System.out.println("\n\nRun another? [Y/N]: ");
            choice = queryInput().toUpperCase();
        } while (choice.equals("Y"));
    }

    private static void getUserInput() {
        System.out.print("Barcode or Serial?: ");
        Functions.selectionStrings[Enums.SelectionIndex.BAR_SERIAL_COMBO.getValue()] = queryInput().toLowerCase();

        System.out.print("\nEnter barcode/serial: ");
        Functions.selectionStrings[Enums.SelectionIndex.BAR_SERIAL_TEXT.getValue()] = queryInput();

        System.out.print("\nEnter location code: ");
        Functions.selectionStrings[Enums.SelectionIndex.LOCATION_CODE.getValue()] = queryInput();

        System.out.print("\nEnter device type: ");
        Functions.selectionStrings[Enums.SelectionIndex.DEVICE_TYPE.getValue()] = queryInput();

        System.out.print("\nEnter tech notes: ");
        Functions.selectionStrings[Enums.SelectionIndex.TECH_NOTE.getValue()] = queryInput();

        System.out.print("\nEnter replacement request: ");
        Functions.selectionStrings[Enums.SelectionIndex.REQUESTING.getValue()] = queryInput();
    }

    @NotNull
    private static String queryInput() {
        String input;
        Scanner sc = new Scanner(System.in);

        input = sc.nextLine();

        return input;
    }
}
