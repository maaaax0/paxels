package de.maax.paxels.item;

import de.maax.paxels.SimplePaxels;
import de.maax.paxels.item.custom.PaxelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(SimplePaxels.MODID);

    public static final DeferredItem<Item> WOODEN_PAXEL =
            registerPaxel("wooden_paxel", Tiers.WOOD, 7.0F, -3.0F);

    public static final DeferredItem<Item> STONE_PAXEL =
            registerPaxel("stone_paxel", Tiers.STONE, 8.0F, -3.0F);

    public static final DeferredItem<Item> GOLDEN_PAXEL =
            registerPaxel("golden_paxel", Tiers.GOLD, 7.0F, -2.8F);

    public static final DeferredItem<Item> IRON_PAXEL =
            registerPaxel("iron_paxel", Tiers.IRON, 7.0F, -2.9F);

    public static final DeferredItem<Item> DIAMOND_PAXEL =
            registerPaxel("diamond_paxel", Tiers.DIAMOND, 6.0F, -2.8F);

    public static final DeferredItem<Item> NETHERITE_PAXEL =
            registerPaxel("netherite_paxel", Tiers.NETHERITE, 6.0F, -2.8F);

    private static DeferredItem<Item> registerPaxel(String name, Tier tier, float attackDamageModifier, float attackSpeedModifier) {
        return ITEMS.register(name,
                () -> new PaxelItem(
                        tier,
                        attackDamageModifier,
                        attackSpeedModifier,
                        new Item.Properties()
                )
        );
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
