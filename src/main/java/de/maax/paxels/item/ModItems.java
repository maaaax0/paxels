package de.maax.paxels.item;

import de.maax.paxels.SimplePaxels;
import de.maax.paxels.item.custom.PaxelItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ModItems {
    public static final Item WOODEN_PAXEL =
            registerPaxel("wooden_paxel", ToolMaterial.WOOD, 7.0F, -3.0F);

    public static final Item STONE_PAXEL =
            registerPaxel("stone_paxel", ToolMaterial.STONE, 8.0F, -3.0F);

    public static final Item GOLDEN_PAXEL =
            registerPaxel("golden_paxel", ToolMaterial.GOLD, 7.0F, -2.8F);

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
                        new Item.Properties()
                )
        );
    }

    public static void register() {
    }
}
