package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.blocks.mattercompressor.MatterCompressorContainer;
import me.johnnydevo.bettercoalmod.blocks.mattercompressor.MatterCompressorScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BetterCoalMod.MOD_ID);

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.register(MATTER_COMPRESSOR.get(), MatterCompressorScreen::new);
    }

    public static final RegistryObject<ContainerType<MatterCompressorContainer>> MATTER_COMPRESSOR = CONTAINERS.register(ModNames.MATTER_COMPRESSOR, () -> IForgeContainerType.create(MatterCompressorContainer::new));
}
