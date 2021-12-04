package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.items.CarbonBow;
import me.johnnydevo.bettercoalmod.items.ModArmorMaterial;
import me.johnnydevo.bettercoalmod.items.ModItemTier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.system.CallbackI;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterCoalMod.MOD_ID);

    public static final RegistryObject<BlockItem> MATTER_COMPRESSOR_ITEM = ITEMS.register(ModNames.MATTER_COMPRESSOR, () -> new BlockItem(ModBlocks.MATTER_COMPRESSOR.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> MATTER_DECOMPRESSOR_ITEM = ITEMS.register(ModNames.MATTER_DECOMPRESSOR, () -> new BlockItem(ModBlocks.MATTER_DECOMPRESSOR.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> MATTER_RECOMPRESSOR_ITEM = ITEMS.register(ModNames.MATTER_RECOMPRESSOR, () -> new BlockItem(ModBlocks.MATTER_RECOMPRESSOR.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<BlockItem> COMPRESSED_COAL_BLOCK_ITEM = ITEMS.register(ModNames.COMPRESSED_COAL_BLOCK, () -> new BlockItem(ModBlocks.COMPRESSED_COAL_BLOCK.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> HIGH_QUALITY_CARBON_BLOCK_ITEM = ITEMS.register(ModNames.HIGH_QUALITY_CARBON_BLOCK, () -> new BlockItem(ModBlocks.HIGH_QUALITY_CARBON_BLOCK.get(), new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Item> COMPRESSED_COAL = ITEMS.register(ModNames.COMPRESSED_COAL, () -> new Item(new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> HIGH_QUALITY_CARBON = ITEMS.register(ModNames.HIGH_QUALITY_CARBON, () -> new Item(new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> CARBON_ROD = ITEMS.register(ModNames.CARBON_ROD, () -> new Item(new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> CARBON_FIBER = ITEMS.register(ModNames.CARBON_FIBER, () -> new Item(new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_MATERIALS)));

    public static final RegistryObject<Item> CARBON_SWORD = ITEMS.register(ModNames.CARBON_SWORD, () -> new SwordItem(ModItemTier.CARBON, 3, -2.4F, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CARBON_SHOVEL = ITEMS.register(ModNames.CARBON_SHOVEL, () -> new ShovelItem(ModItemTier.CARBON, 1.5F, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> CARBON_PICKAXE = ITEMS.register(ModNames.CARBON_PICKAXE, () -> new PickaxeItem(ModItemTier.CARBON, 1, -2.8F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> CARBON_AXE = ITEMS.register(ModNames.CARBON_AXE, () -> new AxeItem(ModItemTier.CARBON, 5.0F, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> CARBON_HOE = ITEMS.register(ModNames.CARBON_HOE, () -> new HoeItem(ModItemTier.CARBON, -3, 0.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> CARBON_BOW = ITEMS.register(ModNames.CARBON_BOW, () -> new CarbonBow(new Item.Properties().tab(ItemGroup.TAB_COMBAT).durability(1082)));

    public static final RegistryObject<Item> CARBON_CHESTPLATE = ITEMS.register(ModNames.CARBON_CHESTPLATE, () -> new ArmorItem(ModArmorMaterial.CARBON, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CARBON_HELMET = ITEMS.register(ModNames.CARBON_HELMET, () -> new ArmorItem(ModArmorMaterial.CARBON, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CARBON_LEGGINGS = ITEMS.register(ModNames.CARBON_LEGS, () -> new ArmorItem(ModArmorMaterial.CARBON, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CARBON_BOOTS = ITEMS.register(ModNames.CARBON_BOOTS, () -> new ArmorItem(ModArmorMaterial.CARBON, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
}
