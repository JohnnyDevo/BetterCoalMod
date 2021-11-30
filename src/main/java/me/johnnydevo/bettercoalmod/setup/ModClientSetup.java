package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BetterCoalMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientSetup {
    @SubscribeEvent
    public static void setModelProperties(FMLClientSetupEvent event) {
        ItemModelsProperties.register(ModItems.CARBON_BOW.get(), new ResourceLocation("pull"), (itemstack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != itemstack ? 0.0F : (float) (itemstack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemModelsProperties.register(ModItems.CARBON_BOW.get(), new ResourceLocation("pulling"),
                (itemstack, world, entity) -> entity != null && entity.isUsingItem() && entity.getUseItem() == itemstack ? 1.0F : 0.0F);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModContainers.registerScreens(event);
    }
}
