package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntitiesInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BetterCoalMod.MOD_ID);
}
