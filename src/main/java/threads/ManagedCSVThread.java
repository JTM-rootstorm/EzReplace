package threads;

import java.util.ArrayList;
import java.util.List;

class ManagedCSVThread extends ManagedThread {
    private List<String> csvToRead;
    private List<List<String>> csvData;

    ManagedCSVThread(String id) {
        super(id);
        csvToRead = new ArrayList<>();
    }

    @Override
    public void run() {

    }

    public List<List<String>> getCSVData() {
        return csvData;
    }
}
