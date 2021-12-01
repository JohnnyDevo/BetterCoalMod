package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.blocks.mattercompressor.MatterCompressorTile;
import me.johnnydevo.bettercoalmod.blocks.matterrecompressor.MatterRecompressorTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BetterCoalMod.MOD_ID);

    public static final RegistryObject<TileEntityType<MatterCompressorTile>> MATTER_COMPRESSOR = TILE_ENTITIES.register(ModNames.MATTER_COMPRESSOR, () -> TileEntityType.Builder.of(MatterCompressorTile::new, ModBlocks.MATTER_COMPRESSOR.get()).build(null));
    public static final RegistryObject<TileEntityType<MatterRecompressorTile>> MATTER_RECOMPRESSOR = TILE_ENTITIES.register(ModNames.MATTER_RECOMPRESSOR, () -> TileEntityType.Builder.of(MatterRecompressorTile::new, ModBlocks.MATTER_RECOMPRESSOR.get()).build(null));
}
