package managers;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SettingsManager {
    public static int numThreadsRequested = 1;
    public static boolean nogui = false;
    public static boolean noclipboard = false;

    public static void parseArgs(@NotNull List<String> args) {
        args.stream()
                .filter(arg -> arg.contains("threads"))
                .findFirst()
                .ifPresent(threadArg -> numThreadsRequested = Integer.parseInt(threadArg.split("=")[1]));

        args.stream()
                .filter(arg -> arg.contains("nogui"))
                .findFirst()
                .ifPresent(guiarg -> nogui = true);

        args.stream()
                .filter(arg -> arg.contains("noclipboard"))
                .findFirst()
                .ifPresent(noclip -> noclipboard = true);
    }
}
