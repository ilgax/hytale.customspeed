package dev.ilgax.hytale.customspeed;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import java.util.logging.Level;

public class CustomSpeedPlugin extends JavaPlugin {
    private final Config<SpeedConfig> config;
    private SpeedConfig speedConfig;
    private SpeedValidator validator;

    public CustomSpeedPlugin(JavaPluginInit init) {
        super(init);
        // Register config in constructor (State is NONE)
        this.config = this.withConfig("config", SpeedConfig.CODEC);
    }

    @Override
    protected void setup() {
        // Retrieve the loaded config (available in setup)
        this.speedConfig = this.config.get();

        // Validate and fix config if needed
        validateAndFixConfig();

        this.validator = new SpeedValidator(this.speedConfig);

        // Register commands
        this.getCommandRegistry().registerCommand(new SpeedCommand(this));
    }

    @Override
    protected void start() {
        this.getLogger().at(Level.INFO).log("CustomSpeed v%s enabled. Initial speed: %f",
            PluginVersion.getVersion(), speedConfig.getCurrentSpeed());

        // Apply saved speed
        applySpeed(speedConfig.getCurrentSpeed());
    }

    public boolean setSpeedMultiplier(float multiplier) {
        // Validate speed
        if (!validator.isValid(multiplier)) {
            this.getLogger().at(Level.WARNING).log("Attempted to set invalid speed: %f", multiplier);
            return false; // Invalid
        }

        speedConfig.setCurrentSpeed(multiplier);

        // Update toggle target if not normal speed
        if (!validator.areSpeedsEqual(multiplier, SpeedConstants.NORMAL_SPEED)) {
            speedConfig.setToggleTargetSpeed(multiplier);
        }

        saveConfig();

        if (!applySpeed(multiplier)) {
            // Rollback config change if apply failed
            this.getLogger().at(Level.WARNING).log("Failed to apply speed, rolling back config");
            return false;
        }

        NotificationService.notifySpeedChange(multiplier, "set to");
        return true;
    }

    /**
     * Adjusts the speed by a delta and returns the new clamped speed.
     *
     * @param delta The amount to adjust the speed by (can be positive or negative)
     * @return The new speed value, or the current speed if adjustment failed
     * @throws IllegalStateException if the speed adjustment fails
     */
    public float adjustSpeed(float delta) {
        float current = speedConfig.getCurrentSpeed();
        float target = validator.clamp(current + delta);

        // Round to avoid floating point precision issues
        target = validator.roundSpeed(target);

        if (!setSpeedMultiplier(target)) {
            this.getLogger().at(Level.SEVERE).log("Failed to adjust speed by %f from %f to %f",
                delta, current, target);
            throw new IllegalStateException("Failed to adjust speed from " + current + " to " + target);
        }

        return target;
    }
    
    public void toggleSpeed() {
        float current = speedConfig.getCurrentSpeed();
        float target;

        // If at normal speed, switch to toggle target; otherwise, return to normal
        if (validator.areSpeedsEqual(current, SpeedConstants.NORMAL_SPEED)) {
            target = speedConfig.getToggleTargetSpeed();

            // Edge case: If toggle target is also normal speed (first time toggle), use default slow-mo
            if (validator.areSpeedsEqual(target, SpeedConstants.NORMAL_SPEED)) {
                target = SpeedConstants.DEFAULT_TOGGLE_SPEED;
                speedConfig.setToggleTargetSpeed(target);
            }
        } else {
            target = SpeedConstants.NORMAL_SPEED;
        }

        speedConfig.setCurrentSpeed(target);
        saveConfig();
        applySpeed(target);

        NotificationService.notifySpeedChange(target, "toggled to");
    }
    
    public SpeedConfig getSpeedConfig() {
        return speedConfig;
    }

    public SpeedValidator getValidator() {
        return validator;
    }

    private void validateAndFixConfig() {
        boolean configChanged = false;

        // Validate min/max speed bounds
        if (speedConfig.getMinSpeed() <= 0 || speedConfig.getMinSpeed() > speedConfig.getMaxSpeed()) {
            this.getLogger().at(Level.WARNING).log("Invalid minSpeed in config (%f), resetting to 0.01",
                speedConfig.getMinSpeed());
            speedConfig.setMinSpeed(0.01f);
            configChanged = true;
        }

        if (speedConfig.getMaxSpeed() <= speedConfig.getMinSpeed() || speedConfig.getMaxSpeed() > 100.0f) {
            this.getLogger().at(Level.WARNING).log("Invalid maxSpeed in config (%f), resetting to 10.0",
                speedConfig.getMaxSpeed());
            speedConfig.setMaxSpeed(10.0f);
            configChanged = true;
        }

        // Validate current speed is within bounds
        if (speedConfig.getCurrentSpeed() < speedConfig.getMinSpeed() ||
            speedConfig.getCurrentSpeed() > speedConfig.getMaxSpeed()) {
            this.getLogger().at(Level.WARNING).log("Current speed (%f) out of bounds, resetting to 1.0",
                speedConfig.getCurrentSpeed());
            speedConfig.setCurrentSpeed(SpeedConstants.NORMAL_SPEED);
            configChanged = true;
        }

        // Validate toggle target speed
        if (speedConfig.getToggleTargetSpeed() < speedConfig.getMinSpeed() ||
            speedConfig.getToggleTargetSpeed() > speedConfig.getMaxSpeed()) {
            this.getLogger().at(Level.WARNING).log("Toggle target speed (%f) out of bounds, resetting to 0.5",
                speedConfig.getToggleTargetSpeed());
            speedConfig.setToggleTargetSpeed(SpeedConstants.DEFAULT_TOGGLE_SPEED);
            configChanged = true;
        }

        if (configChanged) {
            this.getLogger().at(Level.INFO).log("Config was corrected, saving changes");
            saveConfig();
        }
    }

    private void saveConfig() {
        try {
            this.config.save();
        } catch (Exception e) {
            this.getLogger().at(Level.SEVERE).withCause(e).log("Failed to save config");
            NotificationService.notifyError("Failed to save configuration! Settings may not persist.");
        }
    }

    private boolean applySpeed(float targetMultiplier) {
        try {
            String command = "time dilation " + targetMultiplier;
            this.getLogger().at(Level.FINE).log("Executing command: %s", command);

            // handleCommand returns CompletableFuture<Void>, we assume success if no exception thrown
            HytaleServer.get().getCommandManager().handleCommand(ConsoleSender.INSTANCE, command);

            this.getLogger().at(Level.FINE).log("Successfully applied speed: %f", targetMultiplier);
            return true;
        } catch (Exception e) {
            this.getLogger().at(Level.SEVERE).withCause(e).log("Failed to apply custom speed multiplier");
            NotificationService.notifyError("Failed to apply game speed! Check server logs.");
            return false;
        }
    }
}