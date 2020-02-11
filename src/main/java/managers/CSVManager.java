package managers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVManager {
    private static CSVManager instance;

    private CSVManager() {

    }

    public static CSVManager getInstance() {
        if (instance == null) {
            synchronized (CSVManager.class) {
                if (instance == null) {
                    instance = new CSVManager();
                }
            }
        }

        return instance;
    }

    public List<List<String>> readLocationCSV() {
        String path = System.getProperty("user.dir") + "\\locationcodes.csv";

        return readCSV(path);
    }

    public List<List<String>> readAssetCSV(@NotNull List<String> filenames) {
         String path;

        List<List<String>> tempData = new ArrayList<>();

        for (String filename : filenames) {
            path = System.getProperty("user.dir") + "\\asset-csv\\" + filename;
            tempData.addAll(trimData(readCSV(path)));
        }

        return tempData;
    }

    @NotNull
    private List<List<String>> trimData(@NotNull List<List<String>> input) {
        List<List<String>> finalData = new ArrayList<>();

        for (List<String> asset : input.subList(0, input.size() - 1)) {
            List<String> temp = new ArrayList<>();
            temp.add(asset.get(0));
            temp.add(asset.get(1));
            temp.add(asset.get(4));
            temp.add(asset.get(6));
            temp.add(asset.get(11));
            temp.add(asset.get(7));

            finalData.add(temp);
        }

        finalData.remove(0);
        finalData.remove(finalData.size() - 1);
        return finalData;
    }

    @NotNull
    private List<List<String>> readCSV(String path) {
        List<List<String>> tempData = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                tempData.add(Arrays.asList(values));
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println(e.getMessage());
        }

        return tempData;
    }
}
