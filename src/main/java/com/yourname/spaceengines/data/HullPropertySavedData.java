package com.yourname.spaceengines.data;

import com.yourname.spaceengines.SpaceEnginesMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;

public class HullPropertySavedData extends SavedData {
    private static final String DATA_KEY = SpaceEnginesMod.MODID + "_hull_properties";

    private final Set<Long> coatedBlocks = new HashSet<>();
    private final Set<Long> sealedBlocks = new HashSet<>();
    private float recycledAirPool;

    public static HullPropertySavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(HullPropertySavedData::new, HullPropertySavedData::load),
                DATA_KEY
        );
    }

    public static HullPropertySavedData load(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        HullPropertySavedData data = new HullPropertySavedData();
        ListTag coated = tag.getList("Coated", Tag.TAG_LONG);
        for (int i = 0; i < coated.size(); i++) {
            data.coatedBlocks.add(((LongTag) coated.get(i)).getAsLong());
        }

        ListTag sealed = tag.getList("Sealed", Tag.TAG_LONG);
        for (int i = 0; i < sealed.size(); i++) {
            data.sealedBlocks.add(((LongTag) sealed.get(i)).getAsLong());
        }

        data.recycledAirPool = tag.getFloat("RecycledAirPool");
        return data;
    }

    public void setCoated(BlockPos pos, boolean coated) {
        if (coated) {
            coatedBlocks.add(pos.asLong());
        } else {
            coatedBlocks.remove(pos.asLong());
        }
        setDirty();
    }

    public void setSealed(BlockPos pos, boolean sealed) {
        if (sealed) {
            sealedBlocks.add(pos.asLong());
        } else {
            sealedBlocks.remove(pos.asLong());
        }
        setDirty();
    }

    public boolean isCoated(BlockPos pos) {
        return coatedBlocks.contains(pos.asLong());
    }

    public boolean isSealed(BlockPos pos) {
        return sealedBlocks.contains(pos.asLong());
    }

    public void addRecycledAir(float amount) {
        recycledAirPool += amount;
        setDirty();
    }

    public float getRecycledAirPool() {
        return recycledAirPool;
    }

    @Override
    public CompoundTag save(CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        ListTag coated = new ListTag();
        for (long value : coatedBlocks) {
            coated.add(LongTag.valueOf(value));
        }
        tag.put("Coated", coated);

        ListTag sealed = new ListTag();
        for (long value : sealedBlocks) {
            sealed.add(LongTag.valueOf(value));
        }
        tag.put("Sealed", sealed);
        tag.putFloat("RecycledAirPool", recycledAirPool);
        return tag;
    }
}