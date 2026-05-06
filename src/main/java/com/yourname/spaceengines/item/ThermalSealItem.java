package com.yourname.spaceengines.item;

import com.yourname.spaceengines.data.HullPropertySavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ThermalSealItem extends Item {
    public ThermalSealItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        BlockPos pos = context.getClickedPos();
        HullPropertySavedData data = HullPropertySavedData.get(serverLevel);
        data.setSealed(pos, true);

        ItemStack stack = context.getItemInHand();
        stack.hurtAndBreak(1, context.getPlayer(), context.getPlayer() == null ? null : context.getPlayer().getEquipmentSlotForItem(stack));
        return InteractionResult.CONSUME;
    }
}