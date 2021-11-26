package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.BetterCoalMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterCoalMod.MOD_ID);

    public static final RegistryObject<BlockItem> MATTER_COMPRESSOR_ITEM = ITEMS.register(ModNames.MATTER_COMPRESSOR, () -> new BlockItem(BlocksInit.MATTER_COMPRESSOR.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> MATTER_DECOMPRESSOR_ITEM = ITEMS.register(ModNames.MATTER_DECOMPRESSOR, () -> new BlockItem(BlocksInit.MATTER_DECOMPRESSOR.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> MATTER_RECOMPRESSOR_ITEM = ITEMS.register(ModNames.MATTER_RECOMPRESSOR, () -> new BlockItem(BlocksInit.MATTER_RECOMPRESSOR.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> COMPRESSED_COAL_BLOCK_ITEM = ITEMS.register(ModNames.COMPRESSED_COAL_BLOCK, () -> new BlockItem(BlocksInit.COMPRESSED_COAL_BLOCK.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Item> COMPRESSED_COAL = ITEMS.register(ModNames.COMPRESSED_COAL, () -> new Item(new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_MATERIALS)));
}
