package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.blocks.mattercompressor.MatterCompressorContainer;
import me.johnnydevo.bettercoalmod.blocks.mattercompressor.MatterCompressorScreen;
import me.johnnydevo.bettercoalmod.blocks.matterrecompressor.MatterRecompressorContainer;
import me.johnnydevo.bettercoalmod.blocks.matterrecompressor.MatterRecompressorScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        ScreenManager.register(MATTER_RECOMPRESSOR.get(), MatterRecompressorScreen::new);
    }

    public static final RegistryObject<ContainerType<MatterCompressorContainer>> MATTER_COMPRESSOR = CONTAINERS.register(ModNames.MATTER_COMPRESSOR, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        IIntArray fields = new IntArray(data.readByte());
        return new MatterCompressorContainer(windowId, world, pos, inv, inv.player, fields);
    }));

    public static final RegistryObject<ContainerType<MatterRecompressorContainer>> MATTER_RECOMPRESSOR = CONTAINERS.register(ModNames.MATTER_RECOMPRESSOR, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        IIntArray fields = new IntArray(data.readByte());
        return new MatterRecompressorContainer(windowId, world, pos, inv, inv.player, fields);
    }));
}
