package com.yourname.spaceengines.block.entity;

import com.yourname.spaceengines.SpaceEnginesConfig;
import com.yourname.spaceengines.SpaceEnginesMod;
import com.yourname.spaceengines.block.EngineBlock;
import com.yourname.spaceengines.block.NozzleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EngineBlockEntity extends BlockEntity {
    private float temperature;
    private int cooldownTicks;
    private boolean stalled;
    private boolean hasNozzle;

    public EngineBlockEntity(BlockPos pos, BlockState blockState) {
        super(SpaceEnginesMod.ENGINE_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EngineBlockEntity blockEntity) {
        int redstone = level.getBestNeighborSignal(pos);
        float throttle = redstone / 15.0f;
        blockEntity.hasNozzle = blockEntity.scanNozzle(level, pos);

        if (blockEntity.cooldownTicks > 0) {
            blockEntity.cooldownTicks--;
            blockEntity.temperature = Math.max(0.0f, blockEntity.temperature - 0.6f);
            if (blockEntity.cooldownTicks == 0) {
                blockEntity.stalled = false;
            }
        } else {
            float oxygenDensity = blockEntity.estimateOxygenDensity(pos.getY());
            boolean hasWaterAdjacency = blockEntity.scanWaterAdjacency(level, pos);
            float heatRise = throttle * (1.5f + (1.0f - oxygenDensity));
            float cooling = hasWaterAdjacency ? 1.4f : 0.2f;

            if (!blockEntity.hasNozzle) {
                heatRise += 0.8f;
            }

            blockEntity.temperature = Math.max(0.0f, blockEntity.temperature + heatRise - cooling);
            if (blockEntity.temperature >= SpaceEnginesConfig.ENGINE_OVERHEAT_TEMPERATURE.get().floatValue()) {
                blockEntity.stalled = true;
                blockEntity.cooldownTicks = SpaceEnginesConfig.ENGINE_COOLDOWN_TICKS.get();
                blockEntity.temperature = SpaceEnginesConfig.ENGINE_OVERHEAT_TEMPERATURE.get().floatValue() * 0.8f;
            }
        }

        int heatLevel = Math.min(4, Math.max(0, (int) (blockEntity.temperature / 25.0f)));
        if (state.getValue(EngineBlock.HEAT_LEVEL) != heatLevel) {
            level.setBlock(pos, state.setValue(EngineBlock.HEAT_LEVEL, heatLevel), 3);
        }

        blockEntity.setChanged();
    }

    private boolean scanNozzle(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (level.getBlockState(pos.relative(direction)).getBlock() instanceof NozzleBlock) {
                return true;
            }
        }
        return false;
    }

    private boolean scanWaterAdjacency(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (!level.getFluidState(pos.relative(direction)).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private float estimateOxygenDensity(int y) {
        int threshold = SpaceEnginesConfig.VACUUM_ALTITUDE_Y.get();
        if (y <= threshold) {
            return 1.0f;
        }
        double deficit = (y - threshold) * SpaceEnginesConfig.OXYGEN_DENSITY_FALLOFF.get();
        return (float) Math.max(0.0d, 1.0d - deficit);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putFloat("Temperature", temperature);
        tag.putInt("CooldownTicks", cooldownTicks);
        tag.putBoolean("Stalled", stalled);
        tag.putBoolean("HasNozzle", hasNozzle);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        temperature = tag.getFloat("Temperature");
        cooldownTicks = tag.getInt("CooldownTicks");
        stalled = tag.getBoolean("Stalled");
        hasNozzle = tag.getBoolean("HasNozzle");
    }

    public float getTemperature() {
        return temperature;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public boolean isStalled() {
        return stalled;
    }

    public boolean hasNozzle() {
        return hasNozzle;
    }
}