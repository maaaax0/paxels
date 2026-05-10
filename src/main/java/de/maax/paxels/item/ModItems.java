package de.maax.paxels.item;

import de.maax.paxels.SimplePaxels;
import de.maax.paxels.item.custom.PaxelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(SimplePaxels.MODID);

    public static final DeferredItem<Item> WOODEN_PAXEL =
            registerPaxel("wooden_paxel", ToolMaterial.WOOD, 7.0F, -3.0F);

    public static final DeferredItem<Item> STONE_PAXEL =
            registerPaxel("stone_paxel", ToolMaterial.STONE, 8.0F, -3.0F);

    public static final DeferredItem<Item> GOLDEN_PAXEL =
            registerPaxel("golden_paxel", ToolMaterial.GOLD, 7.0F, -2.8F);

    public static final DeferredItem<Item> COPPER_PAXEL =
            registerPaxel("copper_paxel", ToolMaterial.COPPER, 7.0F, -2.9F);

    public static final DeferredItem<Item> IRON_PAXEL =
            registerPaxel("iron_paxel", ToolMaterial.IRON, 7.0F, -2.9F);

    public static final DeferredItem<Item> DIAMOND_PAXEL =
            registerPaxel("diamond_paxel", ToolMaterial.DIAMOND, 6.0F, -2.8F);

    public static final DeferredItem<Item> NETHERITE_PAXEL =
            registerPaxel("netherite_paxel", ToolMaterial.NETHERITE, 6.0F, -2.8F);

    private static DeferredItem<Item> registerPaxel(String name, ToolMaterial material, float attackDamageModifier, float attackSpeedModifier) {
        return ITEMS.register(name,
                () -> new PaxelItem(
                        material,
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
