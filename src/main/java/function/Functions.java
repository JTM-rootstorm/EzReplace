package function;

import managers.SQLiteManager;
import managers.SettingsManager;
import org.jetbrains.annotations.NotNull;

import javax.sql.rowset.CachedRowSet;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Functions {
    public static String[] selectionStrings = new String[] { null, null, null, null, null, null };

    private static void copyTextToClipboard(String paneString) {
        if (SettingsManager.noclipboard) {
            return;
        }

        StringSelection stringSelection = new StringSelection(paneString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @NotNull
    public static java.util.List<String> gatherResults() {
        java.util.List<String> results = new ArrayList<>();
        CachedRowSet resultSet = generateCachedRow();
        try {
            results.add(resultSet.getString("description"));
            results.add(selectionStrings[Enums.SelectionIndex.DEVICE_TYPE.getValue()]);
            results.add(resultSet.getString("barcode"));
            results.add(resultSet.getString("serial"));
            results.add(resultSet.getString("asset"));
            results.add(resultSet.getString("purchase_date"));
            results.add(resultSet.getString("po_number"));
            results.add(selectionStrings[Enums.SelectionIndex.TECH_NOTE.getValue()]);
            results.add(selectionStrings[Enums.SelectionIndex.REQUESTING.getValue()]);
        } catch (SQLException s) {
            System.err.println(s.getMessage());
        }

        results.add(0, SQLiteManager.getInstance()
                .selectFromLocationDB(selectionStrings[Enums.SelectionIndex.LOCATION_CODE.getValue()]));
        results.add(1, selectionStrings[Enums.SelectionIndex.LOCATION_CODE.getValue()]);

        return results;
    }

    @NotNull
    public static String generateFinalString(@NotNull List<String> results) {
        String sb = "[u][b]Replacement Request[/b][/u]\n\n" +
                "Location Name: " + results.get(0) + "\n" +
                "Location Code: " + results.get(1) + "\n" +
                "Description: " + results.get(2) + "\n" +
                "Type: " + results.get(3) + "\n" +
                "Barcode: " + results.get(4) + "\n" +
                "Serial: " + results.get(5) + "\n" +
                "Asset ID: " + results.get(6) + "\n" +
                "Purchase Date: " + results.get(7) + "\n" +
                "PO#: " + results.get(8) + "\n\n" +
                "Tech Notes: " + "\n" + results.get(9) + "\n\n" +
                "Requesting: " + "\n" + results.get(10) + "\n";

        copyTextToClipboard(sb);
        return sb;
    }

    private static CachedRowSet generateCachedRow() {
        CachedRowSet cachedRowSet;

        if (selectionStrings[Enums.SelectionIndex.BAR_SERIAL_COMBO.getValue()].toLowerCase().equals("barcode")) {
            cachedRowSet =  SQLiteManager.getInstance()
                    .selectByBarcode(selectionStrings[Enums.SelectionIndex.BAR_SERIAL_TEXT.getValue()]);
        }
        else {
            cachedRowSet =  SQLiteManager.getInstance()
                    .selectBySerial(selectionStrings[Enums.SelectionIndex.BAR_SERIAL_TEXT.getValue()]);
        }

        return cachedRowSet;
    }
}
