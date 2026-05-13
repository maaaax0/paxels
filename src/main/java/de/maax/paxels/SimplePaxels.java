package de.maax.paxels;

import de.maax.paxels.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SimplePaxels implements ModInitializer {
    public static final String MODID = "paxels";

    public static final ResourceKey<CreativeModeTab> PAXELS_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, id("paxels_tab"));

    public static final CreativeModeTab PAXELS_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.paxels"))
            .icon(() -> new ItemStack(ModItems.NETHERITE_PAXEL))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.WOODEN_PAXEL);
                output.accept(ModItems.STONE_PAXEL);
                output.accept(ModItems.IRON_PAXEL);
                output.accept(ModItems.GOLDEN_PAXEL);
                output.accept(ModItems.DIAMOND_PAXEL);
                output.accept(ModItems.NETHERITE_PAXEL);
            })
            .build();

    @Override
    public void onInitialize() {
        ModItems.register();
        net.minecraft.core.Registry.register(
                net.minecraft.core.registries.BuiltInRegistries.CREATIVE_MODE_TAB,
                PAXELS_TAB_KEY,
                PAXELS_TAB
        );
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }
}
