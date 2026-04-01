# Create Utilities (Unofficial Port) - Porting Notes

## Summary
This repository has been ported from Forge `1.20.1` to NeoForge `1.21.1` and updated to target Create `6.0.9`.

Port maintainer:
- `SmartStreamLabs`

Original project attribution:
- Original mod: `Create Utilities`
- Original author: `Duquee_`
- Original license: `MIT License`

The port keeps the original mod id `createutilities` for compatibility and changes the display name to `Create Utilities (Unofficial Port)`.

## What Changed
- Replaced the old ForgeGradle build with a NeoForge ModDevGradle build.
- Switched project metadata generation to `neoforge.mods.toml`.
- Updated the project to Java `21`.
- Updated external dependencies to:
  - NeoForge `21.1.222`
  - Minecraft `1.21.1`
  - Create `6.0.9-215`
  - Ponder `1.0.81+mc1.21.1`
  - Flywheel `1.0.6`
  - Registrate `MC1.21-1.3.0+67`
- Preserved the original mod id and gameplay-facing content paths.

## NeoForge 1.21.1 Port Work
- Migrated Forge bootstrap/event wiring to NeoForge event buses.
- Replaced Forge networking with NeoForge custom payload registration and clientbound payload handlers.
- Migrated block entity serialization to registry-aware `HolderLookup.Provider` APIs.
- Migrated saved data loading/saving to the new `SavedData.Factory` flow.
- Reworked menu client buffer handling to `RegistryFriendlyByteBuf`.
- Registered item/fluid/energy capabilities through NeoForge capability registration events.
- Updated block interaction hooks to modern `useWithoutItem` and `useItemOn` paths where required.
- Added 1.21 block `MapCodec` implementations to custom blocks.
- Updated rendering code for 1.21 vertex/model/skull API changes.

## Create 6.0.9 Compatibility Work
- Kept Create, Ponder, Flywheel, and Registrate as external Gradle dependencies.
- Updated Create addon networking away from removed `SimplePacketBase`.
- Updated mounted storage registration and type wiring for current Create APIs.
- Updated Create/NeoForge fluid, item, and energy integration points.
- Kept the original blocks, items, mounted storage, ponder scenes, recipes, loot tables, tags, and assets intact where possible.

## Validation Performed
- `gradlew build` completed successfully.
- `gradlew compileJava` completed successfully.
- `gradlew runServer --nogui` reached server startup completion with the mod loaded alongside Create, Ponder, and Flywheel.

Observed server log note:
- The first server boot reported a temporary `server.properties` file lookup warning before creating the run directory state, then continued and reached `Done`. The final startup completed successfully.

## Known Limitations
- Dedicated server boot was validated.
- Full in-game manual gameplay verification, client visual verification, and multiplayer join validation were not exercised interactively in this environment.
- There is still one non-failing deprecation warning from a Registrate layer registration call in `CUBlocks`; it does not block build or startup.

## Changed Files
- `build.gradle`
- `gradle.properties`
- `gradle/wrapper/gradle-wrapper.properties`
- `settings.gradle`
- `README.md`
- `PORTING_NOTES.md`
- `src/main/resources/pack.mcmeta`
- `src/main/templates/META-INF/neoforge.mods.toml`
- `src/main/java/me/duquee/createutilities/CreateUtilities.java`
- `src/main/java/me/duquee/createutilities/CreateUtilitiesClient.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/battery/VoidBattery.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/battery/VoidBatteryBlock.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/battery/VoidBatteryData.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/battery/VoidBatteryTileEntity.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/chest/VoidChestBlock.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/chest/VoidChestContainer.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/chest/VoidChestInventoriesData.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/chest/VoidChestTileEntity.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/motor/VoidMotorBlock.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/motor/VoidMotorNetworkHandler.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/tank/VoidTank.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/tank/VoidTankBlock.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/tank/VoidTankRenderer.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/tank/VoidTanksData.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/tank/VoidTankTileEntity.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/VoidLinkBehaviour.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/VoidStorageData.java`
- `src/main/java/me/duquee/createutilities/blocks/voidtypes/VoidTileRenderer.java`
- `src/main/java/me/duquee/createutilities/events/ClientEvents.java`
- `src/main/java/me/duquee/createutilities/events/CommonEvents.java`
- `src/main/java/me/duquee/createutilities/mountedstorage/CUMountedStorages.java`
- `src/main/java/me/duquee/createutilities/mountedstorage/VoidChestMountedStorageType.java`
- `src/main/java/me/duquee/createutilities/networking/CUPackets.java`
- `src/main/java/me/duquee/createutilities/networking/packets/VoidBatteryUpdatePacket.java`
- `src/main/java/me/duquee/createutilities/networking/packets/VoidTankUpdatePacket.java`
- `src/main/java/me/duquee/createutilities/ponder/CUPonders.java`
- `src/main/java/me/duquee/createutilities/ponder/VoidScenes.java`
- `src/main/java/me/duquee/createutilities/tabs/CUCreativeTabs.java`
- `src/main/java/me/duquee/createutilities/voidlink/VoidLinkHandler.java`
- `src/main/java/me/duquee/createutilities/voidlink/VoidLinkRenderer.java`
