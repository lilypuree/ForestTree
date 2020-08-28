package lilypuree.forest_tree;

import lilypuree.forest_tree.api.genera.ForestTreeTypes;
import lilypuree.forest_tree.api.genera.VanillaTypes;
import lilypuree.forest_tree.api.registration.TreeBlockRegistry;
import lilypuree.forest_tree.common.shrubs.block.MultipleFlowerBlock;
import lilypuree.forest_tree.common.shrubs.block.MultipleFlowerTile;
import lilypuree.forest_tree.common.trees.block.PalmCrownBlock;
import lilypuree.forest_tree.common.trees.customization.*;
import lilypuree.forest_tree.common.trees.items.CustomSaplingItem;
import lilypuree.forest_tree.common.trees.items.GraftingToolItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static lilypuree.forest_tree.ForestTree.MODID;

public class Registration {

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registration.GRAFTING_TOOL.get());
        }
    };

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MODID);
    public static final DeferredRegister<PointOfInterestType> POIS = new DeferredRegister<>(ForgeRegistries.POI_TYPES, MODID);

    public static void register() {
//        TreeBlockCategories.register();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        for(VanillaTypes category : VanillaTypes.values()){
            TreeBlockRegistry.registerWoodCategory(BLOCKS, ITEMS, category);
            TreeBlockRegistry.registerFoliageCategory(BLOCKS, ITEMS, category);
        }
        for (ForestTreeTypes category : ForestTreeTypes.values()){
            TreeBlockRegistry.registerWoodCategory(BLOCKS, ITEMS, category);
            TreeBlockRegistry.registerFoliageCategory(BLOCKS, ITEMS, category);
        }

        ITEMS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        BLOCKS.register(modEventBus);
        ENTITIES.register(modEventBus);
        CONTAINERS.register(modEventBus);
        POIS.register(modEventBus);
    }

    //    public static final RegistryObject<AdvancedSaplingBlock> OAK_SAPLING = BLOCKS.register("oak_sapling", ()->new AdvancedSaplingBlock(new AdvancedOakTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0f).sound(SoundType.PLANT)));
//    public static final RegistryObject<AdvancedSaplingBlock> PINE_SAPLING = BLOCKS.register("fir_sapling", ()->new AdvancedSaplingBlock(new PineTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0f).sound(SoundType.PLANT)));
//    public static final RegistryObject<AdvancedSaplingBlock> PLACEBO_SAPLING = BLOCKS.register("sapling", () -> new AdvancedSaplingBlock(new CustomTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0f).sound(SoundType.PLANT)));
    public static final RegistryObject<CustomSaplingBlock> CUSTOM_SAPLING = BLOCKS.register("custom_sapling", () -> new CustomSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0f).sound(SoundType.PLANT)));
    public static final RegistryObject<Item> CUSTOM_SAPLING_ITEM = ITEMS.register("custom_sapling", () -> new CustomSaplingItem(CUSTOM_SAPLING.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<CustomSaplingTile>> CUSTOM_SAPLING_TILE = TILE_ENTITIES.register("custom_sapling", () -> TileEntityType.Builder.create(CustomSaplingTile::new, CUSTOM_SAPLING.get()).build(null));

    public static final RegistryObject<Block> TREE_DESIGNER_BLOCK = BLOCKS.register("tree_designer", () -> new TreeDesignerBlock(Block.Properties.create(Material.EARTH)));
    public static final RegistryObject<Item> TREE_DESIGNER_ITEM = ITEMS.register("tree_designer", () -> new BlockItem(TREE_DESIGNER_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<TreeDesignerTile>> TREE_DESIGNER_TILE = TILE_ENTITIES.register("tree_designer", () -> TileEntityType.Builder.create(TreeDesignerTile::new, TREE_DESIGNER_BLOCK.get()).build(null));
    public static final RegistryObject<ContainerType<TreeDesignerContainer>> TREE_DESIGNER_CONTAINER = CONTAINERS.register("tree_designer", () -> IForgeContainerType.create((TreeDesignerContainer::getClientContainer)));

    public static final RegistryObject<Block> MULTIPLE_FLOWER_BLOCK = BLOCKS.register("multiple_flower", () -> new MultipleFlowerBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0f).sound(SoundType.PLANT)));
    public static final RegistryObject<Item> MULTIPLE_FLOWER_ITEM = ITEMS.register("multiple_flower", () -> new BlockItem(MULTIPLE_FLOWER_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<MultipleFlowerTile>> MULTIPLE_FLOWER_TILE = TILE_ENTITIES.register("multiple_flower", () -> TileEntityType.Builder.create(MultipleFlowerTile::new, MULTIPLE_FLOWER_BLOCK.get()).build(null));


    public static final RegistryObject<Block> PALM_CROWN = BLOCKS.register("palm_crown", () -> new PalmCrownBlock(Block.Properties.create(Material.WOOD).notSolid()));
    //    public static final RegistryObject<Block> PALM_LOG = BLOCKS.register("palm_log", () -> new LogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)));
//    public static final RegistryObject<Block> PALM_SAPLING = BLOCKS.register("palm_sapling", () -> new PalmCrownBlock(Block.Properties.create(Material.WOOD).notSolid()));
//    public static final RegistryObject<Item> PALM_SAPLING_ITEM = ITEMS.register("palm_sapling", () -> new BlockItem(PALM_CROWN.get(), new Item.Properties().group(ITEM_GROUP)));
//    public static final RegistryObject<Item> PALM_LOG_ITEM = ITEMS.register("palm_log", () -> new BlockItem(PALM_CROWN.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<Item> PALM_CROWN_ITEM = ITEMS.register("palm_crown", () -> new BlockItem(PALM_CROWN.get(), new Item.Properties().group(ITEM_GROUP)));

    //    private static Item.Properties timber = new Item.Properties().group(ItemGroup.MATERIALS);
    public static final RegistryObject<GraftingToolItem> GRAFTING_TOOL = ITEMS.register("grafting_tool", () -> new GraftingToolItem((new Item.Properties()).maxDamage(238).group(ITEM_GROUP)));
//    public static final RegistryObject<Item> TREE_EXTRACT = ITEMS.register("tree_extract", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
//    public static final RegistryObject<Item> TREE_ESSENCE = ITEMS.register("tree_essence", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
//    public static final RegistryObject<Item> ACACIA_TIMBER = ITEMS.register("acacia_timber", () -> new Item(timber));
//    public static final RegistryObject<Item> BIRCH_TIMBER = ITEMS.register("birch_timber", () -> new Item(timber));
//    public static final RegistryObject<Item> DARK_OAK_TIMBER = ITEMS.register("dark_oak_timber", () -> new Item(timber));
//    public static final RegistryObject<Item> JUNGLE_TIMBER = ITEMS.register("jungle_timber", () -> new Item(timber));
//    public static final RegistryObject<Item> OAK_TIMBER = ITEMS.register("oak_timber", () -> new Item(timber));
//    public static final RegistryObject<Item> SPRUCE_TIMBER = ITEMS.register("spruce_timber", () -> new Item(timber));
//
//
//    public static final RegistryObject<TileEntityType<TreeTile>> TREE_TILE;

}

