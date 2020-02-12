package managers;

public class FTPManager {
    private static FTPManager instance;

    private FTPManager() {}

    public static FTPManager getInstance() {
        if (instance == null) {
            synchronized (FTPManager.class) {
                if (instance == null) {
                    instance = new FTPManager();
                }
            }
        }

        return instance;
    }
}
