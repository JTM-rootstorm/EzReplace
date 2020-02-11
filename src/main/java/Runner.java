import layout.CLIForm;
import layout.MainForm;
import managers.CSVManager;
import managers.SQLiteManager;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class Runner {
    public static void main(String[] args) {
        init();

        if (Arrays.asList(args).contains("nogui")) {
            System.out.println("No GUI requested...");
            //CLIForm.nogui();
            CLIForm.nogui();
        } else {
            MainForm.createAndShowGUI();
        }
    }

    private static void init() {
        readLocationsToMemory();
        readAssetsToMemory();
    }

    private static void readLocationsToMemory() {
        System.out.print("Reading locations to memory...");

        CSVManager.getInstance().readLocationCSV();
        SQLiteManager.getInstance().loadLocationsToMemory(CSVManager.getInstance().getCsvData());

        System.out.println("Done");
    }

    private static void readAssetsToMemory() {
        System.out.print("Reading asset CSVs to memory...");

        File assets = new File(System.getProperty("user.dir") + "\\asset-csv\\");
        java.util.List<String> assetCSVFiles =
                Arrays.asList(Objects.requireNonNull(assets.list((dir, name) -> name.toLowerCase().endsWith(".csv"))));

        CSVManager.getInstance().readAssetCSV(assetCSVFiles);
        SQLiteManager.getInstance().loadAssetsToDB(CSVManager.getInstance().getCsvData());

        System.out.println("Done");
    }
}
