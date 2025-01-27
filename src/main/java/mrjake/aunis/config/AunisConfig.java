package mrjake.aunis.config;

import mrjake.aunis.block.AunisBlocks;
import mrjake.aunis.renderer.biomes.BiomeOverlayEnum;
import mrjake.aunis.util.ItemMetaPair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Config(modid = "aunis", name = "aunis") // + Aunis.Version)
public class AunisConfig {

    @Name("Stargate size")
    @RequiresWorldRestart
    public static StargateSizeEnum stargateSize = StargateSizeEnum.MEDIUM;

    @Name("Check for updates")
    public static boolean enableAutoUpdater = true;

    @Name("Stargate config options")
    public static StargateConfig stargateConfig = new StargateConfig();

    @Name("Dialing/Incoming options")
    public static DialingConfig dialingConfig = new DialingConfig();

    @Name("Iris/shield config options")
    public static IrisConfig irisConfig = new IrisConfig();

    @Name("DHD config options")
    public static DHDConfig dhdConfig = new DHDConfig();

    @Name("Transport rings options")
    public static RingsConfig ringsConfig = new RingsConfig();

    @Name("Power draw options")
    public static PowerConfig powerConfig = new PowerConfig();

    @Name("Debug options")
    public static DebugConfig debugConfig = new DebugConfig();

    @Name("Developer options")
    public static DevConfig devConfig = new DevConfig();

    @Name("Mysterious Page options")
    public static MysteriousConfig mysteriousConfig = new MysteriousConfig();

    @Name("Notebook Pages options")
    public static NoteBookOptions notebookOptions = new NoteBookOptions();

    @Name("AutoClose options")
    public static AutoCloseConfig autoCloseConfig = new AutoCloseConfig();

    @Name("Open time limit config")
    public static OpenLimitConfig openLimitConfig = new OpenLimitConfig();

    @Name("Beamer options")
    public static BeamerConfig beamerConfig = new BeamerConfig();

    @Name("Audio/Video")
    public static AudioVideoConfig avConfig = new AudioVideoConfig();

    @Name("WorldGen config")
    public static WorldGenConfig worldgenConfig = new WorldGenConfig();

    @Name("MainMenu config")
    public static MainMenuConfig mainMenuConfig = new MainMenuConfig();

    @Name("Integrations config")
    public static IntegrationsConfig integrationsConfig = new IntegrationsConfig();

    @Name("Random incoming config")
    public static RandomIncomingConfig randomIncoming = new RandomIncomingConfig();

    @Name("Recipes options")
    public static RecipesConfig recipesConfig = new RecipesConfig();

    public static class StargateConfig {
        @Name("Orlin's gate max open count")
        @RangeInt(min = 0)
        public int stargateOrlinMaxOpenCount = 2;

        @Name("Universe dialer max horizontal reach radius")
        @RangeInt(min = 0, max = 64)
        public int universeDialerReach = 10;

        @Name("Universe dialer nearby radius")
        public int universeGateNearbyReach = 1024;

        @Name("Enable opening last chevron while dialing with dhd")
        @Comment({
                "Enable opening last chevron while dialing milkyway gate with dhd"
        })
        public boolean dhdLastOpen = true;

        @Name("Disable animated Event Horizon")
        @Comment({
                "Changing this option will require you to reload resources manually.",
                "Just press F3+Q once in-game."
        })
        public boolean disableAnimatedEventHorizon = false;

        @Name("Temperature threshold for frosty overlay")
        @Comment({
                "Below this biome temperature the gate will receive frosty texture.",
                "Set to negative value to disable."
        })
        public float frostyTemperatureThreshold = 0.1f;

        // ---------------------------------------------------------------------------------------
        // Kawoosh blocks

        @Name("Kawoosh invincible blocks")
        @Comment({
                "Format: \"modid:blockid[:meta]\", for example: ",
                "\"minecraft:wool:7\"",
                "\"minecraft:stone\""
        })
        public String[] kawooshInvincibleBlocks = {};

        private List<IBlockState> cachedInvincibleBlocks = null;

        public boolean canKawooshDestroyBlock(IBlockState state) {
            if (state.getBlock() == AunisBlocks.IRIS_BLOCK) return false;
            if (cachedInvincibleBlocks == null) {
                cachedInvincibleBlocks = BlockMetaParser.parseConfig(kawooshInvincibleBlocks);
            }

            return !cachedInvincibleBlocks.contains(state);
        }


        // ---------------------------------------------------------------------------------------
        // Jungle biomes
        @Name("Biome overlay biome matches")
        @SuppressWarnings("serial")
        @Comment({
                "This check comes last (after block is directly under sky (except Nether) and temperature is high enough).",
                "You can disable the temperature check by setting it to a negative value.",
                "Format: \"modid:biomename\", for example: ",
                "\"minecraft:dark_forest\"",
                "\"minecraft:forest\""
        })
        public Map<String, String[]> biomeMatches = new HashMap<String, String[]>() {
            {
                put(BiomeOverlayEnum.NORMAL.toString(), new String[]{});
                put(BiomeOverlayEnum.FROST.toString(), new String[]{});
                put(BiomeOverlayEnum.MOSSY.toString(), new String[]{"minecraft:jungle", "minecraft:jungle_hills", "minecraft:jungle_edge", "minecraft:mutated_jungle", "minecraft:mutated_jungle_edge"});
                put(BiomeOverlayEnum.AGED.toString(), new String[]{});
                put(BiomeOverlayEnum.SOOTY.toString(), new String[]{"minecraft:hell"});
            }
        };

        private Map<Biome, BiomeOverlayEnum> cachedBiomeMatchesReverse = null;

        private void genBiomeOverrideBiomeCache() {
            cachedBiomeMatchesReverse = new HashMap<>();

            for (Map.Entry<String, String[]> entry : biomeMatches.entrySet()) {
                List<Biome> parsedList = BiomeParser.parseConfig(entry.getValue());
                BiomeOverlayEnum biomeOverlay = BiomeOverlayEnum.fromString(entry.getKey());

                for (Biome biome : parsedList) {
                    cachedBiomeMatchesReverse.put(biome, biomeOverlay);
                }
            }
        }

        public Map<Biome, BiomeOverlayEnum> getBiomeOverrideBiomes() {
            if (cachedBiomeMatchesReverse == null) {
                genBiomeOverrideBiomeCache();
            }

            return cachedBiomeMatchesReverse;
        }


        // ---------------------------------------------------------------------------------------
        // Biome overlay override blocks

        @Name("Biome overlay override blocks")
        @SuppressWarnings("serial")
        @Comment({
                "Format: \"modid:blockid[:meta]\", for example: ",
                "\"minecraft:wool:7\"",
                "\"minecraft:stone\""
        })
        public Map<String, String[]> biomeOverrideBlocks = new HashMap<String, String[]>() {
            {
                put(BiomeOverlayEnum.NORMAL.toString(), new String[]{"minecraft:stone"});
                put(BiomeOverlayEnum.FROST.toString(), new String[]{"minecraft:ice"});
                put(BiomeOverlayEnum.MOSSY.toString(), new String[]{"minecraft:vine"});
                put(BiomeOverlayEnum.AGED.toString(), new String[]{"minecraft:cobblestone"});
                put(BiomeOverlayEnum.SOOTY.toString(), new String[]{"minecraft:coal_block"});
            }
        };

        private Map<BiomeOverlayEnum, List<ItemMetaPair>> cachedBiomeOverrideBlocks = null;
        private Map<ItemMetaPair, BiomeOverlayEnum> cachedBiomeOverrideBlocksReverse = null;

        private void genBiomeOverrideCache() {
            cachedBiomeOverrideBlocks = new HashMap<>();
            cachedBiomeOverrideBlocksReverse = new HashMap<>();

            for (Map.Entry<String, String[]> entry : biomeOverrideBlocks.entrySet()) {
                List<ItemMetaPair> parsedList = ItemMetaParser.parseConfig(entry.getValue());
                BiomeOverlayEnum biomeOverlay = BiomeOverlayEnum.fromString(entry.getKey());

                cachedBiomeOverrideBlocks.put(biomeOverlay, parsedList);

                for (ItemMetaPair stack : parsedList) {
                    cachedBiomeOverrideBlocksReverse.put(stack, biomeOverlay);
                }
            }
        }

        public Map<BiomeOverlayEnum, List<ItemMetaPair>> getBiomeOverrideBlocks() {
            if (cachedBiomeOverrideBlocks == null) {
                genBiomeOverrideCache();
            }

            return cachedBiomeOverrideBlocks;
        }

        public Map<ItemMetaPair, BiomeOverlayEnum> getBiomeOverrideItemMetaPairs() {
            if (cachedBiomeOverrideBlocksReverse == null) {
                genBiomeOverrideCache();
            }

            return cachedBiomeOverrideBlocksReverse;
        }

    }

    public static class DialingConfig {

        @Name("Allow incoming animations")
        @Comment({
                "If the incoming animations of gates generate issues, set it to false",
        })
        public boolean allowIncomingAnimations = true;

        @Name("Connect to dialing gate")
        @Comment({
                "If target gate is dialing and this option is set to true,",
                "the target gate stop dialing and open incoming wormhole.",
                "If this is set to false and the dialed gate dialing address,",
                "the connection will not established.",
                "If it cause issues, set it to false.",
        })
        public boolean allowConnectToDialing = true;

        @Name("Use 8 chevrons between MW and PG gates")
        @Comment({
                "Change this to true, if you want to use 8 chevrons between pegasus and milkyway gates"
        })
        public boolean pegAndMilkUseEightChevrons = true;

        @Name("Need only 7 symbols between Uni gates")
        @Comment({
                "If u want to dial UNI-UNI only with seven symbols (interdimensional for example), set this to true"
        })
        public boolean useStrictSevenSymbolsUniGate = false;

        @Name("Faster MilkyWay gate computer dial")
        @Comment({
                "Speed up dialing with computer on MW gate"
        })
        public boolean fasterMWGateDial = false;

    }

    public static class IrisConfig {
        @Name("Iris kills at destination")
        @Comment("If set to 'false' player get killed by iris on entering event horizon")
        public boolean killAtDestination = true;

        @Name("Titanium iris durability")
        @Comment({
                "Durability of Titanium iris",
                "set it to 0, if u want to make it unbreakable"
        })
        @RangeInt(min = 0)
        public int titaniumIrisDurability = 500;

        @Name("Trinium iris durability")
        @Comment({
                "Durability of Trinium iris",
                "set it to 0, if u want to make it unbreakable"
        })
        @RangeInt(min = 0)
        public int triniumIrisDurability = 1000;

        @Name("Shield power draw")
        @Comment({
                "Energy/tick used for make shield closed"
        })
        @RangeInt(min = 0)
        public int shieldPowerDraw = 500;

        @Name("Allow creative bypass")
        @Comment({
                "Set it to true, if u want to bypass",
                "shield/iris damage by creative gamemode"
        })
        public boolean allowCreative = false;

        @Name("Maximum iris code length")
        @RangeInt(min = 0, max = 32)
        public int irisCodeLength = 9;

        @Name("Can iris destroy blocks")
        public boolean irisDestroysBlocks = false;

        @Name("Unbreaking chance per level")
        @Comment({"0 - disables unbreaking on iris", "100 - unbreaking makes iris unbreakable"})
        @RangeInt(min = 0, max = 100)
        public int unbreakingChance = 10;
    }

    public static class PowerConfig {
        @Name("Stargate's internal buffer size")
        @RangeInt(min = 0)
        public int stargateEnergyStorage = 71280000;

        @Name("Stargate's max power throughput")
        @RangeInt(min = 1)
        public int stargateMaxEnergyTransfer = 26360;

        @Name("Stargate wormhole open power draw")
        @RangeInt(min = 0)
        public int openingBlockToEnergyRatio = 4608;

        @Name("Stargate wormhole sustain power draw")
        @RangeInt(min = 0)
        public int keepAliveBlockToEnergyRatioPerTick = 2;

        @Name("Stargate instability threshold")
        @Comment({
                "Seconds of energy left before gate becomes unstable",
        })
        @RangeInt(min = 1)
        public int instabilitySeconds = 20;

        @Name("Orlin's gate energy multiplier")
        @RangeDouble(min = 0)
        public double stargateOrlinEnergyMul = 2.0;

        @Name("Universe gate energy multiplier")
        @RangeDouble(min = 0)
        public double stargateUniverseEnergyMul = 1.5;

        @Name("Capacitors supported by Universe gates")
        public int universeCapacitors = 0;

        @Name("ZPM Capacity")
        @RangeInt(min = 0)
        public int zpmEnergyStorage = 2000000000;
    }

    public static class RingsConfig {
        @Name("Rings range's radius horizontal")
        @RangeInt(min = 1, max = 256)
        public int rangeFlat = 25;

        @Name("Rings vertical reach")
        @RangeInt(min = 1, max = 256)
        public int rangeVertical = 256;

        @Name("Ignore rings check for blocks to replace")
        public boolean ignoreObstructionCheck = false;
    }

    public static class DHDConfig {
        @Name("DHD range's radius horizontal")
        @RangeInt(min = 1)
        public int rangeFlat = 10;

        @Name("DHD range's radius vertical")
        @RangeInt(min = 1)
        public int rangeVertical = 5;

        @Name("Pegasus DHD do dial animation")
        @Comment({
                "Disable this, to disable animation when dial gate with DHD (pegasus)"
        })
        public boolean animatePegDHDDial = true;

        @Name("DHD's max fluid capacity")
        @RangeInt(min = 1)
        public int fluidCapacity = 60000;

        @Name("Energy per 1mB Naquadah")
        @RangeInt(min = 1)
        public int energyPerNaquadah = 10240;

        @Name("Generation multiplier")
        @RangeInt(min = 1)
        @Comment({
                "Energy per 1mB is multiplied by this",
                "Consumed mB/t is equal to this"
        })
        public int powerGenerationMultiplier = 1;

        @Name("Cold fusion reactor activation energy level")
        @RangeDouble(min = 0, max = 1)
        public double activationLevel = 0.4;

        @Name("Cold fusion reactor deactivation energy level")
        @RangeDouble(min = 0, max = 1)
        public double deactivationLevel = 0.98;
    }

    public static class DebugConfig {
        @Name("Check gate merge")
        public boolean checkGateMerge = true;

        @Name("Render bounding boxes")
        public boolean renderBoundingBoxes = false;

        @Name("Render whole kawoosh bounding box")
        public boolean renderWholeKawooshBoundingBox = false;

        @Name("Render invisible blocks")
        public boolean renderInvisibleBlocks = false;

        @Name("Show loading textures in log")
        public boolean logTexturesLoading = false;
    }

    public static class MysteriousConfig {
        @Name("Max overworld XZ-coords generation")
        @RangeInt(min = 1, max = 30000000)
        public int maxOverworldCoords = 30000;

        @Name("Min overworld XZ-coords generation")
        @RangeInt(min = 1, max = 30000000)
        public int minOverworldCoords = 15000;

        @Name("Chance of despawning DHD")
        @RangeDouble(min = 0, max = 1)
        public double despawnDhdChance = 0.05;

        @Name("Mysterious page cooldown")
        @RangeInt(min = 0)
        public int pageCooldown = 40;
    }

    public static class NoteBookOptions {

        @Name("Notebook page Glyph transparency")
        @RangeDouble(min = 0, max = 1)
        public double glyphTransparency = 0.75;

        @Name("Enable hint when dialing on DHDs with notebook page")
        public boolean enablePageHint = true;

        @Name("Notebook Page offset")
        @Comment({
                "Greater values render the Page more to the center of the screen, smaller render it closer to the borders.",
                "0 - for standard 16:9 (default),",
                "0.2 - for 4:3.",
        })
        public float pageNarrowing = 0;
    }

    public static class AutoCloseConfig {
        @Name("Autoclose enabled")
        public boolean autocloseEnabled = true;

        @Name("Seconds to autoclose with no players nearby")
        @RangeInt(min = 1, max = 300)
        public int secondsToAutoclose = 5;
    }

    public static class OpenLimitConfig {
        @Name("Maximum seconds of gate should be open")
        @Comment({
                "(in seconds (2280 = 38 minutes))"
        })
        @RangeInt(min = 5, max = 3000)
        public int maxOpenedSeconds = 240;

        @Name("What happens after the open time reach this time")
        @Comment({
                "U can use: closeGate, drawMorePower"
        })
        public String maxOpenedWhat = "drawMorePower";

        @Name("Enable max open time?")
        public boolean maxOpenedEnabled = true;

        @Name("Power draw after opened time limit")
        @RangeInt(min = 0, max = 50000)
        public int maxOpenedPowerDrawAfterLimit = 10000;
    }

    public static class BeamerConfig {
        @Name("Fluid buffer capacity")
        @RangeInt(min = 1)
        public int fluidCapacity = 60000;

        @Name("Energy buffer capacity")
        @RangeInt(min = 1)
        public int energyCapacity = 17820000;

        @Name("Energy buffer max transfer")
        @RangeInt(min = 1)
        public int energyTransfer = 26360;

        @Name("Fluid max transfer")
        @RangeInt(min = 1)
        public int fluidTransfer = 100;

        @Name("Item max transfer")
        @RangeInt(min = 1)
        public int itemTransfer = 4;

        @Name("Max gate-beamer distance")
        public int reach = 10;

        @Name("Should the beam be responsive to fluid color")
        public boolean enableFluidBeamColorization = true;

        @Name("Interval of signals being send to OC about transfers (in ticks)")
        @RangeInt(min = 1)
        public int signalIntervalTicks = 20;
    }

    public static class AudioVideoConfig {
        @Name("Aunis volume")
        @RangeDouble(min = 0, max = 1)
        public float volume = 1;
    }

    public static class WorldGenConfig {
        @Name("Enable Naquadah ore generation")
        @Comment({
                "Do you want to spawn naquadah ores in the Nether?",
        })
        public boolean naquadahEnable = true;

        @Name("Naquadah vein size")
        public int naquadahVeinSize = 8;

        @Name("Naquadah max veins in chunk")
        public int naquadahMaxVeinInChunk = 16;

        @Name("Enable Trinium ore generation")
        @Comment({
                "Do you want to spawn trinium ores in the End?",
        })
        public boolean triniumEnabled = true;

        @Name("Trinium vein size")
        public int triniumVeinSize = 2;

        @Name("Trinium max veins in chunk")
        public int triniumMaxVeinInChunk = 4;

        @Name("Enable Titanium ore generation")
        @Comment({
                "Do you want to spawn naquadah ores in the Overworld?",
        })
        public boolean titaniumEnable = true;

        @Name("Titanium vein size")
        public int titaniumVeinSize = 4;

        @Name("Titanium max veins in chunk")
        public int titaniumMaxVeinInChunk = 8;

        @Name("Chance of generating gate in overworld")
        @RangeInt(min = 0, max = 100)
        @Comment({
                "Set this to 0 to disable generation of gates in overworld."
        })
        public int chanceOfGateWorld = 1;
        @Name("Chance of generating gate in nether")
        @RangeInt(min = 0, max = 100)
        @Comment({
                "Set this to 0 to disable generation of gates in nether."
        })
        public int chanceOfGateNether = 1;
        /*@Name("Chance of generating gate in overworld")
        @RangeInt(min = 0, max = 100)
        @Comment({
                "Set this to 0 to disable generation of gates in overworld."
        })
        public int chanceOfGateEnd = 1;*/
    }

    public static class MainMenuConfig {
        @Name("Disable Aunis main menu - WARNING: Requires reloading")
        @Comment({
                "Disables showing custom main menu",
        })
        public boolean disableAunisMainMenu = false;

        @Name("Disable custom positions of buttons")
        @Comment({
                "Disables custom positions of buttons in main menu",
        })
        public boolean disablePosButtons = false;

        @Name("Enable debug mode")
        public boolean debugMode = false;

        @Name("Enable event horizon in mainmenu")
        @Comment({
                "Do you want gate activation when starting game?",
        })
        public boolean enableEventHorizon = true;

        @Name("Enable changing gate overlay")
        @Comment({
                "Enable/disable random gate overlays in main menu",
        })
        public boolean changingGateOverlay = true;

        @Name("Enable gate rotation")
        @Comment({
                "Should ring of gate be static in main menu?",
        })
        public boolean gateRotation = true;

        @Name("Play music in main menu")
        public boolean playMusic = true;
    }

    public static class IntegrationsConfig {
        @Name("Enable Tinkers' Construct integration")
        public boolean tConstructIntegration = true;

        @Name("Enable Open Computers integration")
        public boolean ocIntegration = true;

        @Name("Enable Thermal Expansion integration")
        public boolean tExpansionIntegration = true;
    }

    public static class RandomIncomingConfig {

        @Name("Enable random incoming wormholes")
        @Comment({
                "Enable random incoming wormholes generator"
        })
        public boolean enableRandomIncoming = true;

        @Name("Chance of spawning")
        @Comment({
                "10 = 1%"
        })
        @RangeInt(min = 1, max = 100)
        public int chance = 1;

        @Name("Entities to spawn")
        @SuppressWarnings("serial")
        @Comment({
                "Format: \"modid:entityid\", for example: ",
                "\"minecraft:zombie\"",
                "\"minecraft:creeper\""
        })
        public String[] entitiesToSpawn = {
                "minecraft:zombie",
                "minecraft:skeleton"
        };
    }

    public static class DevConfig {
        @Name("Enable peg gates with myst pages")
        public boolean pegGatesMyst = false;

        @Name("test")
        public boolean test = true;
    }

    public static class RecipesConfig {

        @Name("Convert thermal recipes to normal ones")
        @Comment({
                "When Thermal expansion is not loaded, then",
                "register convert Thermal recipes into",
                "normal ones.",
                "WARNING! - Requires reloading!"
        })
        public boolean convertThermal = true;

        @Name("Allow bypass thermal recipes")
        @Comment({
                "Even when thermal expansion is loaded, register",
                "converted recipes.",
                "WARNING! - Requires reloading!"
        })
        public boolean bypassThermal = false;
    }

    public static void resetCache() {
        stargateConfig.cachedInvincibleBlocks = null;
        stargateConfig.cachedBiomeMatchesReverse = null;
        stargateConfig.cachedBiomeOverrideBlocks = null;
        stargateConfig.cachedBiomeOverrideBlocksReverse = null;
    }
}
