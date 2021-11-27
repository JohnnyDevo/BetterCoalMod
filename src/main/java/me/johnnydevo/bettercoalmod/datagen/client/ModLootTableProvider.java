package me.johnnydevo.bettercoalmod.datagen.client;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {
        map.forEach((param1, param2) -> LootTableManager.validate(validationTracker, param1, param2));
    }

    public static class ModBlockLootTables extends BlockLootTables {
        @Override
        protected void addTables() {
            dropSelf(ModBlocks.COMPRESSED_COAL_BLOCK.get());
            dropSelf(ModBlocks.HIGH_QUALITY_CARBON_BLOCK.get());

            dropSelf(ModBlocks.MATTER_COMPRESSOR.get());
            dropSelf(ModBlocks.MATTER_DECOMPRESSOR.get());
            dropSelf(ModBlocks.MATTER_RECOMPRESSOR.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get)
                    .collect(Collectors.toList());
        }
    }
}
