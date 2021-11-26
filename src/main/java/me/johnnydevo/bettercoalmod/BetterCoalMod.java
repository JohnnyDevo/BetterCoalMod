package me.johnnydevo.bettercoalmod;

import me.johnnydevo.bettercoalmod.setup.BlocksInit;
import me.johnnydevo.bettercoalmod.setup.ItemsInit;
import me.johnnydevo.bettercoalmod.setup.TileEntitiesInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BetterCoalMod.MOD_ID)
public class BetterCoalMod
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "bettercoalmod";

    public BetterCoalMod() {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlocksInit.BLOCKS.register(modEventBus);
        ItemsInit.ITEMS.register(modEventBus);
        TileEntitiesInit.TILE_ENTITIES.register(modEventBus);
    }
}
