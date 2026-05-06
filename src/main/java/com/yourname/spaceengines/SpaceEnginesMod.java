package com.yourname.spaceengines;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.yourname.spaceengines.block.AirRecyclerBlock;
import com.yourname.spaceengines.block.EngineBlock;
import com.yourname.spaceengines.block.NozzleBlock;
import com.yourname.spaceengines.block.SpatialLocatorBlock;
import com.yourname.spaceengines.block.ThermalShieldBlock;
import com.yourname.spaceengines.block.entity.AirRecyclerBlockEntity;
import com.yourname.spaceengines.block.entity.EngineBlockEntity;
import com.yourname.spaceengines.block.entity.SpatialLocatorBlockEntity;
import com.yourname.spaceengines.item.SolarCoatingItem;
import com.yourname.spaceengines.item.ThermalSealItem;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

@Mod(SpaceEnginesMod.MODID)
public class SpaceEnginesMod {
    public static final String MODID = "space_engines";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> ENGINE = BLOCKS.register("engine", () -> new EngineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5F, 10.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> NOZZLE = BLOCKS.register("nozzle", () -> new NozzleBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F, 12.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> FLUID_PIPE = BLOCKS.register("fluid_pipe", () -> new Block(BlockBehaviour.Properties.of().strength(2.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final DeferredBlock<Block> THERMAL_SHIELD = BLOCKS.register("thermal_shield", () -> new ThermalShieldBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 18.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().randomTicks()));
    public static final DeferredBlock<Block> SPATIAL_LOCATOR = BLOCKS.register("spatial_locator", () -> new SpatialLocatorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.5F, 8.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> AIR_RECYCLER = BLOCKS.register("air_recycler", () -> new AirRecyclerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.5F, 14.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredBlock<Block> INTERIOR_PANEL = BLOCKS.register("interior_panel", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 9.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> INTERIOR_FLOOR = BLOCKS.register("interior_floor", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(2.5F, 8.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> INTERIOR_WALL = BLOCKS.register("interior_wall", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(3.0F, 9.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> INTERIOR_LIGHT = BLOCKS.register("interior_light", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(1.5F, 5.0F).lightLevel(state -> 15).sound(SoundType.GLASS).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> THERMAL_SHIELD_SLAB = BLOCKS.register("thermal_shield_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0F, 18.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().randomTicks()));

    public static final DeferredItem<BlockItem> ENGINE_ITEM = ITEMS.registerSimpleBlockItem("engine", ENGINE);
    public static final DeferredItem<BlockItem> NOZZLE_ITEM = ITEMS.registerSimpleBlockItem("nozzle", NOZZLE);
    public static final DeferredItem<BlockItem> FLUID_PIPE_ITEM = ITEMS.registerSimpleBlockItem("fluid_pipe", FLUID_PIPE);
    public static final DeferredItem<BlockItem> THERMAL_SHIELD_ITEM = ITEMS.registerSimpleBlockItem("thermal_shield", THERMAL_SHIELD);
    public static final DeferredItem<BlockItem> SPATIAL_LOCATOR_ITEM = ITEMS.registerSimpleBlockItem("spatial_locator", SPATIAL_LOCATOR);
    public static final DeferredItem<BlockItem> AIR_RECYCLER_ITEM = ITEMS.registerSimpleBlockItem("air_recycler", AIR_RECYCLER);
    public static final DeferredItem<BlockItem> INTERIOR_PANEL_ITEM = ITEMS.registerSimpleBlockItem("interior_panel", INTERIOR_PANEL);
    public static final DeferredItem<BlockItem> INTERIOR_FLOOR_ITEM = ITEMS.registerSimpleBlockItem("interior_floor", INTERIOR_FLOOR);
    public static final DeferredItem<BlockItem> INTERIOR_WALL_ITEM = ITEMS.registerSimpleBlockItem("interior_wall", INTERIOR_WALL);
    public static final DeferredItem<BlockItem> INTERIOR_LIGHT_ITEM = ITEMS.registerSimpleBlockItem("interior_light", INTERIOR_LIGHT);
    public static final DeferredItem<BlockItem> THERMAL_SHIELD_SLAB_ITEM = ITEMS.registerSimpleBlockItem("thermal_shield_slab", THERMAL_SHIELD_SLAB);

    public static final DeferredItem<Item> SOLAR_COATING = ITEMS.register("solar_coating", () -> new SolarCoatingItem(new Item.Properties().durability(256).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> THERMAL_SEAL = ITEMS.register("thermal_seal", () -> new ThermalSealItem(new Item.Properties().durability(256).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> MAGNET_BOOTS_UPGRADE = ITEMS.registerSimpleItem("magnet_boots_upgrade");
    public static final DeferredItem<Item> COSMONAUT_HELMET = ITEMS.registerSimpleItem("cosmonaut_helmet");
    public static final DeferredItem<Item> COSMONAUT_CHESTPLATE = ITEMS.registerSimpleItem("cosmonaut_chestplate");
    public static final DeferredItem<Item> COSMONAUT_LEGGINGS = ITEMS.registerSimpleItem("cosmonaut_leggings");
    public static final DeferredItem<Item> COSMONAUT_BOOTS = ITEMS.registerSimpleItem("cosmonaut_boots");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ENGINE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("engine", () -> BlockEntityType.Builder.of(EngineBlockEntity::new, ENGINE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> SPATIAL_LOCATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("spatial_locator", () -> BlockEntityType.Builder.of(SpatialLocatorBlockEntity::new, SPATIAL_LOCATOR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> AIR_RECYCLER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("air_recycler", () -> BlockEntityType.Builder.of(AirRecyclerBlockEntity::new, AIR_RECYCLER.get()).build(null));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("space_engines", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.space_engines"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ENGINE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ENGINE_ITEM.get());
                output.accept(NOZZLE_ITEM.get());
                output.accept(FLUID_PIPE_ITEM.get());
                output.accept(THERMAL_SHIELD_ITEM.get());
                output.accept(THERMAL_SHIELD_SLAB_ITEM.get());
                output.accept(SPATIAL_LOCATOR_ITEM.get());
                output.accept(AIR_RECYCLER_ITEM.get());
                output.accept(INTERIOR_PANEL_ITEM.get());
                output.accept(INTERIOR_FLOOR_ITEM.get());
                output.accept(INTERIOR_WALL_ITEM.get());
                output.accept(INTERIOR_LIGHT_ITEM.get());
                output.accept(SOLAR_COATING.get());
                output.accept(THERMAL_SEAL.get());
                output.accept(MAGNET_BOOTS_UPGRADE.get());
                output.accept(COSMONAUT_HELMET.get());
                output.accept(COSMONAUT_CHESTPLATE.get());
                output.accept(COSMONAUT_LEGGINGS.get());
                output.accept(COSMONAUT_BOOTS.get());
            }).build());

    public SpaceEnginesMod(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreativeTabs);
        modContainer.registerConfig(ModConfig.Type.COMMON, SpaceEnginesConfig.SPEC, "spaceengines-common.toml");

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // NOTE: Tier sorting registration removed for compatibility with this NeoForge version.
        });

        LOGGER.info("Space Engines common setup complete");
        LOGGER.info("Vacuum altitude threshold: {}", SpaceEnginesConfig.VACUUM_ALTITUDE_Y.get());
        LOGGER.info("Create loaded: {}", ModList.get().isLoaded("create"));
        LOGGER.info("Create Aeronautics loaded: {}", ModList.get().isLoaded("aeronautics"));
    }

    private void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ENGINE_ITEM);
            event.accept(NOZZLE_ITEM);
            event.accept(FLUID_PIPE_ITEM);
            event.accept(THERMAL_SHIELD_ITEM);
            event.accept(THERMAL_SHIELD_SLAB_ITEM);
            event.accept(SPATIAL_LOCATOR_ITEM);
            event.accept(AIR_RECYCLER_ITEM);
            event.accept(INTERIOR_PANEL_ITEM);
            event.accept(INTERIOR_FLOOR_ITEM);
            event.accept(INTERIOR_WALL_ITEM);
            event.accept(INTERIOR_LIGHT_ITEM);
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(SOLAR_COATING);
            event.accept(THERMAL_SEAL);
            event.accept(MAGNET_BOOTS_UPGRADE);
            event.accept(COSMONAUT_HELMET);
            event.accept(COSMONAUT_CHESTPLATE);
            event.accept(COSMONAUT_LEGGINGS);
            event.accept(COSMONAUT_BOOTS);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Space Engines server starting");
    }
}