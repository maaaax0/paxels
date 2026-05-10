package de.maax.paxels.item;

import de.maax.paxels.SimplePaxels;
import de.maax.paxels.item.custom.PaxelItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ModItems {
    private static final TagKey<Item> COPPER_TOOL_MATERIALS =
            TagKey.create(Registries.ITEM, SimplePaxels.id("copper_tool_materials"));
    private static final ToolMaterial COPPER_MATERIAL =
            new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 190, 5.0F, 1.0F, 13, COPPER_TOOL_MATERIALS);

    public static final Item WOODEN_PAXEL =
            registerPaxel("wooden_paxel", ToolMaterial.WOOD, 7.0F, -3.0F);

    public static final Item STONE_PAXEL =
            registerPaxel("stone_paxel", ToolMaterial.STONE, 8.0F, -3.0F);

    public static final Item GOLDEN_PAXEL =
            registerPaxel("golden_paxel", ToolMaterial.GOLD, 7.0F, -2.8F);

    public static final Item COPPER_PAXEL =
            registerPaxel("copper_paxel", COPPER_MATERIAL, 7.0F, -2.9F);

    public static final Item IRON_PAXEL =
            registerPaxel("iron_paxel", ToolMaterial.IRON, 7.0F, -2.9F);

    public static final Item DIAMOND_PAXEL =
            registerPaxel("diamond_paxel", ToolMaterial.DIAMOND, 6.0F, -2.8F);

    public static final Item NETHERITE_PAXEL =
            registerPaxel("netherite_paxel", ToolMaterial.NETHERITE, 6.0F, -2.8F);

    private static Item registerPaxel(String name, ToolMaterial material, float attackDamageModifier, float attackSpeedModifier) {
        return Registry.register(
                BuiltInRegistries.ITEM,
                SimplePaxels.id(name),
                new PaxelItem(
                        material,
                        attackDamageModifier,
                        attackSpeedModifier,
                        new Item.Properties().setId(
                                net.minecraft.resources.ResourceKey.create(
                                        net.minecraft.core.registries.Registries.ITEM,
                                        SimplePaxels.id(name)
                                )
                        )
                )
        );
    }

    public static void register() {
    }
}
