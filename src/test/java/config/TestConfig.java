package config;

import java.time.Duration;

public final class TestConfig {

    public static final Duration DEFAULT_WAIT = Duration.ofSeconds(10);
    public static final Duration LONG_WAIT = Duration.ofSeconds(20);

    public static final long OVERLAY_DISMISS_TIMEOUT_MS = 8_000;
    public static final long OVERLAY_POLL_INTERVAL_MS = 250;

    private TestConfig() {
    }
}
