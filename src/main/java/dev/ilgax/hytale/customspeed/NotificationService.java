package dev.ilgax.hytale.customspeed;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.util.NotificationUtil;

public class NotificationService {
    /**
     * Formats a speed value as a percentage string.
     *
     * @param speed The speed multiplier
     * @return Formatted percentage string
     */
    public static String formatSpeedAsPercentage(float speed) {
        int percent = Math.round(speed * SpeedConstants.SPEED_ROUNDING_FACTOR);
        return percent + "%";
    }

    /**
     * Formats a complete speed message with multiplier and percentage.
     *
     * @param speed The speed multiplier
     * @param prefix Message prefix (e.g., "Game Speed set to", "Current game speed:")
     * @return Formatted message string
     */
    public static String formatSpeedMessage(float speed, String prefix) {
        return prefix + " " + speed + "x (" + formatSpeedAsPercentage(speed) + ")";
    }

    /**
     * Sends a speed change notification to all players.
     *
     * @param speed The new speed value
     * @param action The action performed (e.g., "set to", "toggled to")
     */
    public static void notifySpeedChange(float speed, String action) {
        String message = formatSpeedMessage(speed, "Game Speed " + action);
        NotificationUtil.sendNotificationToUniverse(Message.raw(message));
    }

    /**
     * Sends an error notification to all players.
     *
     * @param errorMessage The error message to display
     */
    public static void notifyError(String errorMessage) {
        NotificationUtil.sendNotificationToUniverse(Message.raw(errorMessage));
    }

    /**
     * Sends a warning notification to all players.
     *
     * @param warningMessage The warning message to display
     */
    public static void notifyWarning(String warningMessage) {
        NotificationUtil.sendNotificationToUniverse(Message.raw(warningMessage));
    }
}
