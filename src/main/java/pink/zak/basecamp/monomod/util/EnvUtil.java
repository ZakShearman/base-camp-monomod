package pink.zak.basecamp.monomod.util;

import org.jetbrains.annotations.NotNull;

public class EnvUtil {

    public static @NotNull String getOrDefault(@NotNull String key, @NotNull String defaultValue) {
        String value = System.getenv(key);

        if (value == null || value.isEmpty()) {
            return defaultValue;
        }

        return value;
    }
}
