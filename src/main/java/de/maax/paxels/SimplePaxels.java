package de.maax.paxels;

import de.maax.paxels.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(SimplePaxels.MODID)
public class SimplePaxels {
    public static final String MODID = "paxels";

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PAXELS_TAB =
            CREATIVE_MODE_TABS.register("paxels_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.paxels"))
                    .icon(() -> new ItemStack(ModItems.NETHERITE_PAXEL.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.WOODEN_PAXEL.get());
                        output.accept(ModItems.STONE_PAXEL.get());
                        output.accept(ModItems.GOLDEN_PAXEL.get());
                        output.accept(ModItems.IRON_PAXEL.get());
                        output.accept(ModItems.DIAMOND_PAXEL.get());
                        output.accept(ModItems.NETHERITE_PAXEL.get());
                    })
                    .build());

    public SimplePaxels(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
