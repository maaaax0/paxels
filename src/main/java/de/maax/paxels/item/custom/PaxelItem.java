package de.maax.paxels.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;

public class PaxelItem extends Item {
    private final Tier tier;

    public PaxelItem(Tier tier, float attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(properties
                .durability(tier.getUses())
                .attributes(DiggerItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier))
                .component(DataComponents.TOOL, createPaxelTool(tier))
        );
        this.tier = tier;
    }

    private static Tool createPaxelTool(Tier tier) {
        return new Tool(
                List.of(
                        Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_PICKAXE, tier.getSpeed()),
                        Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_AXE, tier.getSpeed()),
                        Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_SHOVEL, tier.getSpeed())
                ),
                1.0F,
                1
        );
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility)
                || itemAbility == ItemAbilities.AXE_STRIP
                || itemAbility == ItemAbilities.HOE_TILL;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return this.tier.getRepairIngredient().test(repairCandidate) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);

        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        }

        ItemAbility action = context.getPlayer() != null && context.getPlayer().isShiftKeyDown()
                ? ItemAbilities.HOE_TILL
                : ItemAbilities.SHOVEL_FLATTEN;

        BlockState modifiedState = clickedState.getToolModifiedState(context, action, false);

        if (modifiedState != null && level.getBlockState(clickedPos.above()).isAir()) {
            level.playSound(
                    context.getPlayer(),
                    clickedPos,
                    action == ItemAbilities.HOE_TILL ? SoundEvents.HOE_TILL : SoundEvents.SHOVEL_FLATTEN,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            if (!level.isClientSide) {
                level.setBlock(clickedPos, modifiedState, 11);

                if (context.getPlayer() != null) {
                    context.getItemInHand().hurtAndBreak(
                            1,
                            context.getPlayer(),
                            context.getPlayer().getEquipmentSlotForItem(context.getItemInHand())
                    );
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        BlockState strippedState = clickedState.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false);

        if (strippedState != null) {
            level.playSound(
                    context.getPlayer(),
                    clickedPos,
                    SoundEvents.AXE_STRIP,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            if (!level.isClientSide) {
                level.setBlock(clickedPos, strippedState, 11);

                if (context.getPlayer() != null) {
                    context.getItemInHand().hurtAndBreak(
                            1,
                            context.getPlayer(),
                            context.getPlayer().getEquipmentSlotForItem(context.getItemInHand())
                    );
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
