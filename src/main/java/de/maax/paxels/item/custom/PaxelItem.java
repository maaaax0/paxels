package de.maax.paxels.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockState;

public class PaxelItem extends DiggerItem {
    private final Tier material;

    public PaxelItem(Tier material, float attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(attackDamageModifier, attackSpeedModifier, material, BlockTags.MINEABLE_WITH_PICKAXE, properties);
        this.material = material;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return isMineable(state) ? material.getSpeed() : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        if (!isMineable(state)) {
            return false;
        }
        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return material.getLevel() >= 3;
        }
        if (state.is(BlockTags.NEEDS_IRON_TOOL)) {
            return material.getLevel() >= 2;
        }
        if (state.is(BlockTags.NEEDS_STONE_TOOL)) {
            return material.getLevel() >= 1;
        }
        return true;
    }

    private static boolean isMineable(BlockBehaviour.BlockStateBase state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE)
                || state.is(BlockTags.MINEABLE_WITH_AXE)
                || state.is(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    @Override
    public int getEnchantmentValue() {
        return material.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return material.getRepairIngredient().test(repairCandidate);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);

        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        }

        boolean canModifyGround = level.getBlockState(clickedPos.above()).isAir();

        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown() && till(context, clickedState, canModifyGround)) {
            return InteractionResult.SUCCESS;
        }

        BlockState flattenedState = getFlattenedState(clickedState, canModifyGround);

        if (flattenedState != null) {
            level.playSound(
                    context.getPlayer(),
                    clickedPos,
                    SoundEvents.SHOVEL_FLATTEN,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            if (!level.isClientSide) {
                level.setBlock(clickedPos, flattenedState, 11);

                if (context.getPlayer() != null) {
                    context.getItemInHand().hurtAndBreak(
                            1,
                            context.getPlayer(),
                            player -> player.broadcastBreakEvent(context.getHand())
                    );
                }
            }

            return InteractionResult.SUCCESS;
        }

        BlockState strippedState = getStrippedState(clickedState);

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
                            player -> player.broadcastBreakEvent(context.getHand())
                    );
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static BlockState getFlattenedState(BlockState state, boolean canModifyGround) {
        return canModifyGround ? ShovelItem.FLATTENABLES.get(state.getBlock()) : null;
    }

    private static boolean till(UseOnContext context, BlockState state, boolean canModifyGround) {
        if (!canModifyGround) {
            return false;
        }

        var tillingAction = HoeItem.TILLABLES.get(state.getBlock());
        if (tillingAction == null || !tillingAction.getFirst().test(context)) {
            return false;
        }

        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        level.playSound(context.getPlayer(), clickedPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (!level.isClientSide) {
            tillingAction.getSecond().accept(context);

            if (context.getPlayer() != null) {
                context.getItemInHand().hurtAndBreak(
                        1,
                        context.getPlayer(),
                        player -> player.broadcastBreakEvent(context.getHand())
                );
            }
        }

        return true;
    }

    private static BlockState getStrippedState(BlockState state) {
        net.minecraft.world.level.block.Block strippedBlock = AxeItem.STRIPPABLES.get(state.getBlock());
        if (strippedBlock == null) {
            return null;
        }

        BlockState strippedState = strippedBlock.defaultBlockState();
        if (state.hasProperty(BlockStateProperties.AXIS) && strippedState.hasProperty(BlockStateProperties.AXIS)) {
            strippedState = strippedState.setValue(BlockStateProperties.AXIS, state.getValue(BlockStateProperties.AXIS));
        }

        return strippedState;
    }
}
