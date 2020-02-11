package threads;

abstract class ManagedThread extends Thread {
    private Thread t;
    private String id;

    ManagedThread(String id) {
        this.id = id;
    }

    abstract public void run();

    public void start() {
        if (t == null) {
            t = new Thread(this, id);
            t.start();
        }
    }
}
