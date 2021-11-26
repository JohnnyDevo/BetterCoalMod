package me.johnnydevo.bettercoalmod.datagen;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.datagen.client.ModBlockStateProvider;
import me.johnnydevo.bettercoalmod.datagen.client.ModBlockTagsProvider;
import me.johnnydevo.bettercoalmod.datagen.client.ModItemModelProvider;
import me.johnnydevo.bettercoalmod.datagen.client.ModItemTagsProvider;
import net.minecraft.data.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = BetterCoalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
//            generator.addProvider(new RecipeProvider(generator));
//            generator.addProvider(new LootTableProvider(generator));
        }
        if (event.includeClient()) {
            ExistingFileHelper helper = event.getExistingFileHelper();
            generator.addProvider(new ModBlockStateProvider(generator, helper));
            generator.addProvider(new ModItemModelProvider(generator, helper));

            ModBlockTagsProvider blockTags = new ModBlockTagsProvider(generator, helper);
            generator.addProvider(blockTags);
            generator.addProvider(new ModItemTagsProvider(generator, blockTags, helper));
        }
    }
}