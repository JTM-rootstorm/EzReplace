import layout.CLIForm;
import layout.MainForm;
import managers.CSVManager;
import managers.SQLiteManager;
import managers.SettingsManager;
import threads.ThreadManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Runner {

    public static void main(String[] args) {
        init();

        List<String> argList = Arrays.asList(args);
        SettingsManager.parseArgs(argList);

        if (SettingsManager.nogui) {
            System.out.println("CLI requested...");
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

        SQLiteManager.getInstance().loadLocationsToMemory(CSVManager.getInstance().readLocationCSV());

        System.out.println("Done");
    }

    private static void readAssetsToMemory() {
        System.out.print("Reading asset CSVs to memory...");

        File assets = new File(System.getProperty("user.dir") + File.separator + "asset-csv" + File.separator);
        List<String> assetCSVFiles =
                Arrays.asList(Objects.requireNonNull(assets.list((dir, name) -> name.toLowerCase().endsWith(".csv"))));

        List<List<String>> results = ThreadManager.getInstance().readAssetCSVThreaded(assetCSVFiles, SettingsManager.numThreadsRequested);

        SQLiteManager.getInstance().loadAssetsToDB(results);

        System.out.println("Done");
    }
}
