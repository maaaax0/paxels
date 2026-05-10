package de.maax.paxels.item.custom;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PaxelItem extends Item {
    public PaxelItem(ToolMaterial material, float attackDamageModifier, float attackSpeedModifier, Item.Properties properties) {
        super(properties
                .durability(material.durability())
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue())
                .attributes(createAttributes(material, attackDamageModifier, attackSpeedModifier))
                .component(DataComponents.TOOL, createPaxelTool(material))
        );
    }

    private static Tool createPaxelTool(ToolMaterial material) {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);

        return new Tool(
                List.of(
                        Tool.Rule.deniesDrops(blocks.getOrThrow(material.incorrectBlocksForDrops())),
                        Tool.Rule.minesAndDrops(blocks.getOrThrow(BlockTags.MINEABLE_WITH_PICKAXE), material.speed()),
                        Tool.Rule.minesAndDrops(blocks.getOrThrow(BlockTags.MINEABLE_WITH_AXE), material.speed()),
                        Tool.Rule.minesAndDrops(blocks.getOrThrow(BlockTags.MINEABLE_WITH_SHOVEL), material.speed())
                ),
                1.0F,
                1,
                true
        );
    }

    private static ItemAttributeModifiers createAttributes(ToolMaterial material, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, attackDamage + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, EquipmentSlot.MAINHAND);
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
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
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

            if (!level.isClientSide()) {
                level.setBlock(clickedPos, flattenedState, 11);

                if (context.getPlayer() != null) {
                    context.getItemInHand().hurtAndBreak(
                            1,
                            context.getPlayer(),
                            context.getPlayer().getEquipmentSlotForItem(context.getItemInHand())
                    );
                }
            }

            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
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

            if (!level.isClientSide()) {
                level.setBlock(clickedPos, strippedState, 11);

                if (context.getPlayer() != null) {
                    context.getItemInHand().hurtAndBreak(
                            1,
                            context.getPlayer(),
                            context.getPlayer().getEquipmentSlotForItem(context.getItemInHand())
                    );
                }
            }

            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
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

        if (!level.isClientSide()) {
            tillingAction.getSecond().accept(context);

            if (context.getPlayer() != null) {
                context.getItemInHand().hurtAndBreak(
                        1,
                        context.getPlayer(),
                        context.getPlayer().getEquipmentSlotForItem(context.getItemInHand())
                );
            }
        }

        return true;
    }

    private static BlockState getStrippedState(BlockState state) {
        Block strippedBlock = AxeItem.STRIPPABLES.get(state.getBlock());
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
