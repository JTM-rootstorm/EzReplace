package threads;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
    private List<ManagedThread> threadList = new ArrayList<>();

    private static ThreadManager instance;

    private ThreadManager() {}

    public static ThreadManager getInstance() {
        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    instance = new ThreadManager();
                }
            }
        }

        return instance;
    }
}
