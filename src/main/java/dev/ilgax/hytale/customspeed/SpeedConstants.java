package dev.ilgax.hytale.customspeed;

public final class SpeedConstants {
    // Tolerance for floating-point comparisons
    public static final float FLOAT_TOLERANCE = 0.001f;

    // Normal speed value
    public static final float NORMAL_SPEED = 1.0f;

    // Default toggle target when none is set
    public static final float DEFAULT_TOGGLE_SPEED = 0.5f;

    // Decimal precision for speed display (2 decimal places)
    public static final int SPEED_DECIMAL_PLACES = 2;
    public static final float SPEED_ROUNDING_FACTOR = 100.0f;

    // Default adjustment increments
    public static final float DEFAULT_ADJUSTMENT_DELTA = 0.1f;

    private SpeedConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
