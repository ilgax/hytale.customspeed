package dev.ilgax.hytale.customspeed;

public class SpeedValidator {
    private final SpeedConfig config;

    public SpeedValidator(SpeedConfig config) {
        this.config = config;
    }

    /**
     * Validates if a speed multiplier is within acceptable bounds.
     *
     * @param multiplier The speed multiplier to validate
     * @return true if valid, false otherwise
     */
    public boolean isValid(float multiplier) {
        return multiplier >= config.getMinSpeed() && multiplier <= config.getMaxSpeed();
    }

    /**
     * Clamps a speed multiplier to acceptable bounds.
     *
     * @param multiplier The speed multiplier to clamp
     * @return The clamped value
     */
    public float clamp(float multiplier) {
        return Math.max(config.getMinSpeed(), Math.min(config.getMaxSpeed(), multiplier));
    }

    /**
     * Rounds a speed value to avoid floating-point precision issues.
     *
     * @param speed The speed to round
     * @return The rounded speed
     */
    public float roundSpeed(float speed) {
        return Math.round(speed * SpeedConstants.SPEED_ROUNDING_FACTOR) / SpeedConstants.SPEED_ROUNDING_FACTOR;
    }

    /**
     * Checks if two speeds are effectively equal within tolerance.
     *
     * @param speed1 First speed
     * @param speed2 Second speed
     * @return true if speeds are equal within tolerance
     */
    public boolean areSpeedsEqual(float speed1, float speed2) {
        return Math.abs(speed1 - speed2) < SpeedConstants.FLOAT_TOLERANCE;
    }

    /**
     * Gets a validation error message for an invalid speed.
     *
     * @param multiplier The invalid multiplier
     * @return Error message describing the validation failure
     */
    public String getValidationError(float multiplier) {
        if (multiplier < config.getMinSpeed()) {
            return "Speed multiplier must be at least " + config.getMinSpeed() + "!";
        }
        if (multiplier > config.getMaxSpeed()) {
            return "Speed multiplier must not exceed " + config.getMaxSpeed() + "!";
        }
        return "Speed multiplier must be between " + config.getMinSpeed() + " and " + config.getMaxSpeed() + "!";
    }
}
