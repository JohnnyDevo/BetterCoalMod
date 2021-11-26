package me.johnnydevo.bettercoalmod.datagen.client;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
        super(pGenerator, BetterCoalMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
