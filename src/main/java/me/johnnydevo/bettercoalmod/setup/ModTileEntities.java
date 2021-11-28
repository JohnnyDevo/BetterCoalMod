package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.blocks.mattercompressor.MatterCompressorTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BetterCoalMod.MOD_ID);

    public static final RegistryObject<TileEntityType<MatterCompressorTile>> MATTER_COMPRESSOR = TILE_ENTITIES.register(ModNames.MATTER_COMPRESSOR, () -> TileEntityType.Builder.of(MatterCompressorTile::new, ModBlocks.MATTER_COMPRESSOR.get()).build(null));
}
