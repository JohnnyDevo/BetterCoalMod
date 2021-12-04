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
        getBuilder(ModNames.CARBON_ROD).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_ROD);
        getBuilder(ModNames.CARBON_FIBER).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_FIBER);

        getBuilder(ModNames.CARBON_SWORD).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_SWORD);
        getBuilder(ModNames.CARBON_SHOVEL).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_SHOVEL);
        getBuilder(ModNames.CARBON_PICKAXE).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_PICKAXE);
        getBuilder(ModNames.CARBON_AXE).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_AXE);
        getBuilder(ModNames.CARBON_HOE).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_HOE);

        //couldn't figure out how to automatically generate bow, doesn't seem to exist in base game
        //getBuilder(ModNames.CARBON_BOW).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_BOW);
        getBuilder(ModNames.CARBON_BOW + "_pulling_0").parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_BOW + "_pulling_0");
        getBuilder(ModNames.CARBON_BOW + "_pulling_1").parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_BOW + "_pulling_1");
        getBuilder(ModNames.CARBON_BOW + "_pulling_2").parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_BOW + "_pulling_2");

        getBuilder(ModNames.CARBON_CHESTPLATE).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_CHESTPLATE);
        getBuilder(ModNames.CARBON_HELMET).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_HELMET);
        getBuilder(ModNames.CARBON_BOOTS).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_BOOTS);
        getBuilder(ModNames.CARBON_LEGS).parent(itemGenerated).texture("layer0", "item/" + ModNames.CARBON_LEGS);
    }
}
