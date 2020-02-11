package threads;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadManager {
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

    public List<List<String>> readAssetCSVThreaded(@NotNull List<String> paths, int numThreadsRequested) {
        List<ManagedThread<List<List<String>>>> threadList = new ArrayList<>();

        List<List<String>> threadResults = new ArrayList<>();

        int workPerThread;

        if (paths.size() < numThreadsRequested) {
            workPerThread = 1;
        } else {
            workPerThread = paths.size() / numThreadsRequested;
        }

        ExecutorService executor = Executors.newCachedThreadPool();

        int id = 0;
        for (int i = 0; i < paths.size();) {
            List<String> workList;
            if (((id + 1) * workPerThread) < paths.size()){
                workList = paths.subList(i, (id + 1) * workPerThread);
            } else {
                workList = paths.subList(i, paths.size());
            }

            threadList.add(new ManagedCSVThread("CSVT-" + id, workList));
            id++;
            i = id * workPerThread;
        }
        List<Future<List<List<String>>>> futureList = new ArrayList<>();

        try {
            futureList = executor.invokeAll(threadList);
            awaitTerminationAfterShutdown(executor);
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        try {
            for (Future<List<List<String>>> future : futureList) {
                threadResults.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return threadResults;
    }

    private void awaitTerminationAfterShutdown(@NotNull ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
