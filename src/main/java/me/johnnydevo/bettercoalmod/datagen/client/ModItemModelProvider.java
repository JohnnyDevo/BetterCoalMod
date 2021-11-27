package me.johnnydevo.bettercoalmod.datagen.client;


import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, BetterCoalMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ModNames.MATTER_COMPRESSOR, modLoc("block/" + ModNames.MATTER_COMPRESSOR));
        withExistingParent(ModNames.MATTER_DECOMPRESSOR, modLoc("block/" + ModNames.MATTER_DECOMPRESSOR));
        withExistingParent(ModNames.MATTER_RECOMPRESSOR, modLoc("block/" + ModNames.MATTER_RECOMPRESSOR));

        withExistingParent(ModNames.COMPRESSED_COAL_BLOCK, modLoc("block/" + ModNames.COMPRESSED_COAL_BLOCK));
        withExistingParent(ModNames.HIGH_QUALITY_CARBON_BLOCK, modLoc("block/" + ModNames.HIGH_QUALITY_CARBON_BLOCK));

        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        getBuilder(ModNames.COMPRESSED_COAL).parent(itemGenerated).texture("layer0", "item/" + ModNames.COMPRESSED_COAL);
        getBuilder(ModNames.HIGH_QUALITY_CARBON).parent(itemGenerated).texture("layer0", "item/" + ModNames.HIGH_QUALITY_CARBON);
    }
}
