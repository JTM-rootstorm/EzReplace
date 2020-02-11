package threads;

import java.util.concurrent.Callable;

abstract class ManagedThread<T> implements Callable<T> {
    private String id;

    ManagedThread(String id) {
        this.id = id;
    }

    abstract public T call();

    public String getThreadId() {
        return id;
    }
}
