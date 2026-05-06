package config;

import java.time.Duration;

public final class TestConfig {

    public static final Duration DEFAULT_WAIT = Duration.ofSeconds(10);
    public static final Duration LONG_WAIT = Duration.ofSeconds(20);

    private TestConfig() {
    }
}
