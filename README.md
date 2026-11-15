# Flarial V2

Android companion app prototype for Minecraft Bedrock.

## v0.1 prototype features
- Purple/dark Flarial-inspired original interface
- Home screen with PLAY, detected official Minecraft package version, Setting, and Tentang
- Import picker for `.mcaddon`, `.mcpack`, `.zip`, and `.mcworld`
- GitHub Actions workflow to build a debug APK

## Important compatibility notes
- This is a companion app prototype, not an injected Minecraft client.
- Android package/version detection currently checks the official package ID `com.mojang.minecraftpe`.
- Third-party Minecraft builds may use different package IDs and may not expose version details. Add support for their package IDs only after they are known.
- Import uses Android's document picker and asks Android to open the selected file with a compatible app. Whether Minecraft accepts it depends on the installed Minecraft build, Android version, file association, and storage restrictions.
- The PLAY button launches the official package when detected. Other builds are not launched automatically in this starter version.

## Build
Push this project to the `main` branch or run the `Build Flarial V2 APK` workflow manually from GitHub Actions. Download the artifact named `Flarial-V2-v0.1-debug`.
