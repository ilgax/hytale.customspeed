package dev.ilgax.hytale.customspeed;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PluginVersion {
    private static final String VERSION;
    private static final String DEFAULT_VERSION = "UNKNOWN";

    static {
        String loadedVersion = DEFAULT_VERSION;
        try (InputStream input = PluginVersion.class.getClassLoader().getResourceAsStream("version.properties")) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                loadedVersion = props.getProperty("version", DEFAULT_VERSION);
            }
        } catch (IOException e) {
            Logger.getLogger(PluginVersion.class.getName())
                .log(Level.WARNING, "Failed to load version from properties file", e);
        }
        VERSION = loadedVersion;
    }

    public static String getVersion() {
        return VERSION;
    }

    private PluginVersion() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
