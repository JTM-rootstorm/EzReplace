package threads;

import managers.CSVManager;

import java.util.List;

class ManagedCSVThread extends ManagedThread<List<List<String>>>{
    private List<String> csvToRead;

    ManagedCSVThread(String id, List<String> workload) {
        super(id);
        csvToRead = workload;
    }

    public List<List<String>> run() {
        return CSVManager.getInstance().readAssetCSV(csvToRead);
    }

    @Override
    public List<List<String>> call() {
        return run();
    }
}
