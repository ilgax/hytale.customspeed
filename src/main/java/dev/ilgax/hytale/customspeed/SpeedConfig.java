package dev.ilgax.hytale.customspeed;

import com.hypixel.hytale.codec.builder.BuilderCodec;

public class SpeedConfig {
    // Configuration constants
    private float minSpeed = 0.01f;
    private float maxSpeed = 10.0f;

    // The speed currently applied to the world
    private float currentSpeed = 1.0f;
    // The speed to toggle to when using /speed toggle
    private float toggleTargetSpeed = 0.5f;

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getToggleTargetSpeed() {
        return toggleTargetSpeed;
    }

    public void setToggleTargetSpeed(float toggleTargetSpeed) {
        this.toggleTargetSpeed = toggleTargetSpeed;
    }

    public float getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(float minSpeed) {
        this.minSpeed = minSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public static final BuilderCodec<SpeedConfig> CODEC = BuilderCodec.builder(SpeedConfig.class, SpeedConfig::new)
            .append(new com.hypixel.hytale.codec.KeyedCodec<>("MinSpeed", com.hypixel.hytale.codec.Codec.FLOAT), SpeedConfig::setMinSpeed, SpeedConfig::getMinSpeed).add()
            .append(new com.hypixel.hytale.codec.KeyedCodec<>("MaxSpeed", com.hypixel.hytale.codec.Codec.FLOAT), SpeedConfig::setMaxSpeed, SpeedConfig::getMaxSpeed).add()
            .append(new com.hypixel.hytale.codec.KeyedCodec<>("CurrentSpeed", com.hypixel.hytale.codec.Codec.FLOAT), SpeedConfig::setCurrentSpeed, SpeedConfig::getCurrentSpeed).add()
            .append(new com.hypixel.hytale.codec.KeyedCodec<>("ToggleTargetSpeed", com.hypixel.hytale.codec.Codec.FLOAT), SpeedConfig::setToggleTargetSpeed, SpeedConfig::getToggleTargetSpeed).add()
            .build();
}
