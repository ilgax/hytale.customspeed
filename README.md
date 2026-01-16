# CustomSpeed Mod for Hytale

CustomSpeed is a server-side plugin that allows players to dynamically adjust their local game speed. This is useful for accessibility (slowing the game down) or for increasing the challenge (speeding it up).

## Features

*   **Set Game Speed**: Use `/speed <multiplier>` to set the game time dilation.
    *   Example: `/speed 0.5` (Half speed / Slow motion)
    *   Example: `/speed 2.0` (Double speed / Fast motion)
    *   Example: `/speed 1.0` (Normal speed)
*   **Toggle Speed**: Use `/speed toggle` to quickly switch between normal speed and your last custom speed.
*   **Persistent Configuration**: Your preferred speed is saved and re-applied when the plugin loads.
*   **Notifications**: Visual confirmation when speed is changed.

## Installation

1.  **Build the Mod**:
    Run `.\gradlew.bat build` in the project directory. The output JAR will be in `build/libs/`.

2.  **Install**:
    Copy `hytale-customspeed-1.2.0.jar` to your Hytale `UserData/Mods` folder.
    *   Path example: `C:\Users\<You>\AppData\Roaming\Hytale\UserData\Mods`

3.  **Enable**:
    If the mod does not load, ensure it is enabled in your world's `config.json` or via the Hytale interface.

## Usage

*   **Command**: `/speed <value>` or `/speed toggle`
*   **Permissions**: Requires `customspeed.command.speed` (default for Operators/Singleplayer owner).

## FAQ

**Q: Can I use F1/F2 to change speed?**
A: As this is a server-side plugin, it cannot directly detect key presses like F1 or F2. You must use the chat commands. However, if Hytale supports client-side macros or keybindings in the future, you could bind `/speed toggle` to a key.

**Q: Does this work in Multiplayer?**
A: Yes, it is a server plugin. If installed on a dedicated server, it should allow players (with permission) to change the speed. Note that `time dilation` in Hytale is typically global for the server.

---
**Version**: 1.2.0
**Author**: ilgax
