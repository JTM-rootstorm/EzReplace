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
    private List<List<String>> csvData = new ArrayList<>();

    private static CSVManager instance;

    private CSVManager() {

    }

    public static CSVManager getInstance() {
        if (instance == null) {
            instance = new CSVManager();
        }

        return instance;
    }

    public void readLocationCSV() {
        clearCsvList();

        String path = System.getProperty("user.dir") + "\\locationcodes.csv";

        csvData = readCSV(path);
    }

    public void readAssetCSV(@NotNull List<String> filenames) {
        clearCsvList();

        String path;

        List<List<String>> tempData = new ArrayList<>();

        for (String filename : filenames) {
            path = System.getProperty("user.dir") + "\\asset-csv\\" + filename;
            tempData.addAll(trimData(readCSV(path)));
        }

        csvData = tempData;
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

    private void clearCsvList() {
        if (!csvData.isEmpty()) {
            csvData.clear();
        }
    }

    public List<List<String>> getCsvData() {
        return csvData;
    }
}
