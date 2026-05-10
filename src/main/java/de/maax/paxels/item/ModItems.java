package de.maax.paxels.item;

import de.maax.paxels.SimplePaxels;
import de.maax.paxels.item.custom.PaxelItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class ModItems {
    public static final Item WOODEN_PAXEL =
            registerPaxel("wooden_paxel", Tiers.WOOD, 7.0F, -3.0F);

    public static final Item STONE_PAXEL =
            registerPaxel("stone_paxel", Tiers.STONE, 8.0F, -3.0F);

    public static final Item GOLDEN_PAXEL =
            registerPaxel("golden_paxel", Tiers.GOLD, 7.0F, -2.8F);

    public static final Item IRON_PAXEL =
            registerPaxel("iron_paxel", Tiers.IRON, 7.0F, -2.9F);

    public static final Item DIAMOND_PAXEL =
            registerPaxel("diamond_paxel", Tiers.DIAMOND, 6.0F, -2.8F);

    public static final Item NETHERITE_PAXEL =
            registerPaxel("netherite_paxel", Tiers.NETHERITE, 6.0F, -2.8F);

    private static Item registerPaxel(String name, Tier material, float attackDamageModifier, float attackSpeedModifier) {
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
