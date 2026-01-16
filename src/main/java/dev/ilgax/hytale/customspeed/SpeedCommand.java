package dev.ilgax.hytale.customspeed;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;

public class SpeedCommand extends AbstractPlayerCommand {
    private final CustomSpeedPlugin plugin;
    private final RequiredArg<Float> multiplierArg;

    public SpeedCommand(CustomSpeedPlugin plugin) {
        super("speed", "Adjusts the game speed dilation.");
        this.plugin = plugin;
        this.requirePermission("customspeed.command.speed");
        
        // Variant for /speed (no args) -> show current speed
        this.addUsageVariant(new GetSpeedVariant());
        
        // Variant for /speed toggle
        this.addSubCommand(new ToggleSpeedSubCommand());
        
        // Variant for /speed reset
        this.addSubCommand(new ResetSpeedSubCommand());
        
        // Variant for /speed increase (i)
        this.addSubCommand(new IncreaseSpeedSubCommand());
        
        // Variant for /speed decrease (d)
        this.addSubCommand(new DecreaseSpeedSubCommand());
        
        // Main usage: /speed <multiplier>
        this.multiplierArg = this.withRequiredArg("multiplier", "Speed multiplier (e.g. 0.5)", ArgTypes.FLOAT);
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        float multiplier = context.get(this.multiplierArg);
        if (!plugin.getValidator().isValid(multiplier)) {
            String error = plugin.getValidator().getValidationError(multiplier);
            context.sendMessage(Message.raw(error));
            return;
        }
        plugin.setSpeedMultiplier(multiplier);
    }

    private class GetSpeedVariant extends AbstractPlayerCommand {
        GetSpeedVariant() {
            super("Shows the current game speed.");
        }
        @Override
        protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            float current = plugin.getSpeedConfig().getCurrentSpeed();
            String message = NotificationService.formatSpeedMessage(current, "Current game speed:");
            context.sendMessage(Message.raw(message));
        }
    }

    private class ToggleSpeedSubCommand extends AbstractPlayerCommand {
        ToggleSpeedSubCommand() {
            super("toggle", "Toggles between normal and custom speed.");
        }
        @Override
        protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            plugin.toggleSpeed();
        }
    }

    private class ResetSpeedSubCommand extends AbstractPlayerCommand {
        ResetSpeedSubCommand() {
            super("reset", "Resets the game speed to normal (1.0x).");
        }
        @Override
        protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            plugin.setSpeedMultiplier(1.0f);
        }
    }

    private class IncreaseSpeedSubCommand extends AbstractPlayerCommand {
        private final RequiredArg<Float> amountArg;

        IncreaseSpeedSubCommand() {
            super("increase", "Increases the game speed.");
            this.addAliases("i");

            // Variant 1: No arguments - use default increment
            this.addUsageVariant(new IncreaseDefaultVariant());

            // Variant 2: With amount argument
            this.amountArg = this.withRequiredArg("amount", "Amount to increase by", ArgTypes.FLOAT);
        }

        @Override
        protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            float amount = context.get(this.amountArg);
            try {
                float newSpeed = plugin.adjustSpeed(amount);

                if (plugin.getValidator().areSpeedsEqual(newSpeed, plugin.getSpeedConfig().getMaxSpeed())) {
                    context.sendMessage(Message.raw("Speed capped at " + plugin.getSpeedConfig().getMaxSpeed() + "x"));
                }
            } catch (IllegalStateException e) {
                context.sendMessage(Message.raw("Failed to increase speed: " + e.getMessage()));
            }
        }

        private class IncreaseDefaultVariant extends AbstractPlayerCommand {
            IncreaseDefaultVariant() {
                super("Increases the game speed by default amount.");
            }

            @Override
            protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
                try {
                    float newSpeed = plugin.adjustSpeed(SpeedConstants.DEFAULT_ADJUSTMENT_DELTA);

                    if (plugin.getValidator().areSpeedsEqual(newSpeed, plugin.getSpeedConfig().getMaxSpeed())) {
                        context.sendMessage(Message.raw("Speed capped at " + plugin.getSpeedConfig().getMaxSpeed() + "x"));
                    }
                } catch (IllegalStateException e) {
                    context.sendMessage(Message.raw("Failed to increase speed: " + e.getMessage()));
                }
            }
        }
    }

    private class DecreaseSpeedSubCommand extends AbstractPlayerCommand {
        private final RequiredArg<Float> amountArg;

        DecreaseSpeedSubCommand() {
            super("decrease", "Decreases the game speed.");
            this.addAliases("d");

            // Variant 1: No arguments - use default decrement
            this.addUsageVariant(new DecreaseDefaultVariant());

            // Variant 2: With amount argument
            this.amountArg = this.withRequiredArg("amount", "Amount to decrease by", ArgTypes.FLOAT);
        }

        @Override
        protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            float amount = context.get(this.amountArg);
            try {
                float newSpeed = plugin.adjustSpeed(-amount);

                if (plugin.getValidator().areSpeedsEqual(newSpeed, plugin.getSpeedConfig().getMinSpeed())) {
                    context.sendMessage(Message.raw("Speed capped at " + plugin.getSpeedConfig().getMinSpeed() + "x"));
                }
            } catch (IllegalStateException e) {
                context.sendMessage(Message.raw("Failed to decrease speed: " + e.getMessage()));
            }
        }

        private class DecreaseDefaultVariant extends AbstractPlayerCommand {
            DecreaseDefaultVariant() {
                super("Decreases the game speed by default amount.");
            }

            @Override
            protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
                try {
                    float newSpeed = plugin.adjustSpeed(-SpeedConstants.DEFAULT_ADJUSTMENT_DELTA);

                    if (plugin.getValidator().areSpeedsEqual(newSpeed, plugin.getSpeedConfig().getMinSpeed())) {
                        context.sendMessage(Message.raw("Speed capped at " + plugin.getSpeedConfig().getMinSpeed() + "x"));
                    }
                } catch (IllegalStateException e) {
                    context.sendMessage(Message.raw("Failed to decrease speed: " + e.getMessage()));
                }
            }
        }
    }
}