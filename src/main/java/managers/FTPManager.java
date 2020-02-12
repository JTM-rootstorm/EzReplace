package managers;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;

public class FTPManager {
    private static FTPManager instance;

    private FTPClient client;

    private FTPManager() {
        client = new FTPClient();
        connectToServer();
    }

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

    private void connectToServer() {
        String url = "tech";

        try {
            client.connect(url, 21);
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.login("anonymous", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String remotePath, String localPath) {
        try (FileOutputStream fos = new FileOutputStream(localPath)) {
            client.retrieveFile(remotePath, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
