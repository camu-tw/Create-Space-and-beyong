package com.yourname.spaceengines;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class SpaceEnginesConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue VACUUM_ALTITUDE_Y = BUILDER
            .comment("Altitude where oxygen density begins falling off.")
            .defineInRange("environment.vacuumAltitudeY", 300, -64, 1024);

    public static final ModConfigSpec.IntValue ORBIT_TRANSITION_Y = BUILDER
            .comment("World height that should trigger the orbit transition.")
            .defineInRange("environment.orbitTransitionY", 384, -64, 1024);

    public static final ModConfigSpec.DoubleValue OXYGEN_DENSITY_FALLOFF = BUILDER
            .comment("Multiplier applied to oxygen density loss above the vacuum altitude.")
            .defineInRange("environment.oxygenDensityFalloff", 0.01D, 0.0D, 1.0D);

    public static final ModConfigSpec.DoubleValue ENGINE_MIN_COOLANT_FLOW = BUILDER
            .comment("Base coolant flow required before engines overheat more quickly.")
            .defineInRange("engine.minCoolantFlow", 1.0D, 0.0D, 64.0D);

    public static final ModConfigSpec.DoubleValue ENGINE_OVERHEAT_TEMPERATURE = BUILDER
            .comment("Temperature threshold where engines enter the stall/cooldown state.")
            .defineInRange("engine.overheatTemperature", 100.0D, 1.0D, 10000.0D);

    public static final ModConfigSpec.IntValue ENGINE_COOLDOWN_TICKS = BUILDER
            .comment("Cooldown duration after an engine explosion-pop stall.")
            .defineInRange("engine.cooldownTicks", 200, 0, 20000);

    public static final ModConfigSpec.DoubleValue ENGINE_AFR_MIN = BUILDER
            .comment("Lower bound for the acceptable air-to-fuel ratio.")
            .defineInRange("engine.afrMin", 8.0D, 0.1D, 64.0D);

    public static final ModConfigSpec.DoubleValue ENGINE_AFR_MAX = BUILDER
            .comment("Upper bound for the acceptable air-to-fuel ratio.")
            .defineInRange("engine.afrMax", 18.0D, 0.1D, 64.0D);

    public static final ModConfigSpec.DoubleValue NOZZLE_OPTIMAL_LOW_PRESSURE = BUILDER
            .comment("Lower pressure bound for efficient nozzle operation.")
            .defineInRange("nozzle.optimalLowPressure", 0.15D, 0.0D, 1.0D);

    public static final ModConfigSpec.DoubleValue NOZZLE_OPTIMAL_HIGH_PRESSURE = BUILDER
            .comment("Upper pressure bound for efficient nozzle operation.")
            .defineInRange("nozzle.optimalHighPressure", 0.75D, 0.0D, 1.0D);

    public static final ModConfigSpec.DoubleValue SHIELD_MAX_TEMPERATURE = BUILDER
            .comment("Maximum temperature a thermal shield can survive.")
            .defineInRange("thermalShield.maxTemperature", 1400.0D, 1.0D, 20000.0D);

    public static final ModConfigSpec.DoubleValue AIR_RECYCLER_RPM_EFFICIENCY = BUILDER
            .comment("Air recycle output multiplier per RPM.")
            .defineInRange("airRecycler.rpmEfficiency", 0.25D, 0.0D, 100.0D);

    public static final ModConfigSpec.DoubleValue SEALED_INTERIOR_AIR_PER_BLOCK = BUILDER
            .comment("Air supply budget granted by each sealed interior block.")
            .defineInRange("thermalSeal.airPerBlock", 10.0D, 0.0D, 1000.0D);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> COMPATIBLE_FLUID_TAGS = BUILDER
            .comment("Fluid tags that should be treated as supported by conduit systems.")
            .defineListAllowEmpty("compatibility.fluidTags", List.of("forge:fuels", "forge:water"), () -> "", SpaceEnginesConfig::validateResourceLocation);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private SpaceEnginesConfig() {
    }

    private static boolean validateResourceLocation(final Object value) {
                if (!(value instanceof String)) {
                        return false;
                }

                String text = (String) value;
                return ResourceLocation.tryParse(text) != null;
    }
}