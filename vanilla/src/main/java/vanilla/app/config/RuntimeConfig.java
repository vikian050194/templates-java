package vanilla.app.config;

public final class RuntimeConfig {

    public enum RunMode {
        DEV,
        TEST,
        PROD
    }
    private final RunMode runMode;

    public static int port() {
        // TODO read port from *.resources
        return 8080;
    }

    private static class Holder {

        private static final RuntimeConfig INSTANCE = new RuntimeConfig();
    }

    public static final RuntimeConfig getInstance() {
        return Holder.INSTANCE;
    }

    public RunMode runMode() {
        return runMode;
    }

    private RuntimeConfig() {
        // TODO parse string to enum
        this.runMode = switch (System.getProperty("RUN_ENV", "DEV")) {
            case "PROD" ->
                RunMode.PROD;
            case "TEST" ->
                RunMode.TEST;
            default ->
                RunMode.DEV;
        };
    }

}
