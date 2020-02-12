package lilypuree.forest_tree.setup;

import com.google.common.collect.ImmutableMap;
import lilypuree.forest_tree.blocks.LeavesSlabBlock;
import lilypuree.forest_tree.blocks.LeavesStairsBlock;
import lilypuree.forest_tree.blocks.LeavesTrapDoorBlock;
import lilypuree.forest_tree.blocks.TreeTile;
import lilypuree.forest_tree.datagen.types.ThicknessTypes;
import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
import lilypuree.forest_tree.datagen.types.WoodTypes;
import lilypuree.forest_tree.items.GraftingToolItem;
import lilypuree.forest_tree.items.TreeItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static lilypuree.forest_tree.ForestTree.MODID;

public class Registration {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = new DeferredRegister<>(ForgeRegistries.PROFESSIONS, MODID);
    public static final DeferredRegister<PointOfInterestType> POIS = new DeferredRegister<>(ForgeRegistries.POI_TYPES, MODID);


    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        BLOCKS.register(modEventBus);
        ENTITIES.register(modEventBus);
        PROFESSIONS.register(modEventBus);
        POIS.register(modEventBus);
    }

    public static final ImmutableMap<String, RegistryObject<Block>> TREE_BLOCKS;
    public static final ImmutableMap<String, RegistryObject<Item>> TREE_BLOCK_ITEMS;

    private static Item.Properties timber = new Item.Properties().group(ItemGroup.MATERIALS);
    public static final RegistryObject<GraftingToolItem> GRAFTING_TOOL = ITEMS.register("grafting_tool", () -> new GraftingToolItem((new Item.Properties()).maxDamage(238).group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> TREE_EXTRACT = ITEMS.register("tree_extract", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> TREE_ESSENCE = ITEMS.register("tree_essence", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> ACACIA_TIMBER = ITEMS.register("acacia_timber", () -> new Item(timber));
    public static final RegistryObject<Item> BIRCH_TIMBER = ITEMS.register("birch_timber", () -> new Item(timber));
    public static final RegistryObject<Item> DARK_OAK_TIMBER = ITEMS.register("dark_oak_timber", () -> new Item(timber));
    public static final RegistryObject<Item> JUNGLE_TIMBER = ITEMS.register("jungle_timber", () -> new Item(timber));
    public static final RegistryObject<Item> OAK_TIMBER = ITEMS.register("oak_timber", () -> new Item(timber));
    public static final RegistryObject<Item> SPRUCE_TIMBER = ITEMS.register("spruce_timber", () -> new Item(timber));


    public static final RegistryObject<TileEntityType<TreeTile>> TREE_TILE;


    static {
        ImmutableMap.Builder<String, RegistryObject<Block>> blockBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, RegistryObject<Item>> itemBuilder = ImmutableMap.builder();

        Block.Properties woodBlockProperty = Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD);
        Block.Properties thinWoodBlockProperty = Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(1.2F).sound(SoundType.WOOD);
        Block.Properties stumpBlockProperty = Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(30.0F, 6.0F).sound(SoundType.WOOD);
        Item.Properties woodBlockItemProperty = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS);
        Item.Properties treeItemProperty = new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1);

        for (WoodTypes wood : WoodTypes.values()) {
            for (ThicknessTypes thickness : ThicknessTypes.values()) {
                for (TreeBlockTypes type : TreeBlockTypes.values()) {
                    try {

                        Class<?> clazz = Class.forName("lilypuree.forest_tree.blocks.tree_blocks." + StringUtils.capitalize(thickness.toString()) + "Tree" + StringUtils.capitalize(type.toString()) + "Block");
                        Constructor<?> cons = clazz.getConstructor(Block.Properties.class);

                        Block block = (Block) cons.newInstance((type == TreeBlockTypes.STUMP) ? stumpBlockProperty : ((thickness == ThicknessTypes.THIN) ? thinWoodBlockProperty : woodBlockProperty));
                        String name = wood + "_" + thickness + "_" + type;
                        blockBuilder.put(name.toUpperCase(), BLOCKS.register(name, () -> block));
                        itemBuilder.put(name.toUpperCase() + "_ITEM", ITEMS.register(name, () -> new BlockItem(block, woodBlockItemProperty)));

                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (WoodTypes leaf : WoodTypes.values()) {
            String name = leaf + "_" + "leaves";
            Block leafSlab = new LeavesSlabBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid());
            Block leafStair = new LeavesStairsBlock(Blocks.OAK_LEAVES::getDefaultState, Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).sound(SoundType.PLANT).notSolid());
            Block leafTrapDoor = new LeavesTrapDoorBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).sound(SoundType.PLANT).notSolid());
            blockBuilder.put((name + "_slab").toUpperCase(), BLOCKS.register(name + "_slab", () -> leafSlab));
            blockBuilder.put((name + "_stairs").toUpperCase(), BLOCKS.register(name + "_stairs", () -> leafStair));
            blockBuilder.put((name + "_trapdoor").toUpperCase(), BLOCKS.register(name + "_trapdoor", () -> leafTrapDoor));
            itemBuilder.put((name + "_slab_item").toUpperCase(), ITEMS.register(name + "_slab", () -> new BlockItem(leafSlab, woodBlockItemProperty)));
            itemBuilder.put((name + "_stairs_item").toUpperCase(), ITEMS.register(name + "_stairs", () -> new BlockItem(leafStair, woodBlockItemProperty)));
            itemBuilder.put((name + "_trapdoor_item").toUpperCase(), ITEMS.register(name + "_trapdoor", () -> new BlockItem(leafTrapDoor, woodBlockItemProperty)));
        }

        TREE_BLOCKS = blockBuilder.build();

        for (WoodTypes wood : WoodTypes.values()) {
            for (ThicknessTypes thickness : ThicknessTypes.values()) {
                String name = wood + "_" + thickness + "_tree";
                itemBuilder.put(name.toUpperCase(), ITEMS.register(name, () -> new TreeItem(treeItemProperty, TREE_BLOCKS.get((wood + "_" + thickness + "_stump").toUpperCase()))));
            }
        }

        TREE_BLOCK_ITEMS = itemBuilder.build();

        TREE_TILE = TILE_ENTITIES.register("tree", () -> TileEntityType.Builder.create(TreeTile::new,
                TREE_BLOCKS.get("OAK_THICKEST_STUMP").get(), TREE_BLOCKS.get("OAK_THICK_STUMP").get(), TREE_BLOCKS.get("OAK_THIN_STUMP").get(),
                TREE_BLOCKS.get("SPRUCE_THICKEST_STUMP").get(), TREE_BLOCKS.get("SPRUCE_THICK_STUMP").get(), TREE_BLOCKS.get("SPRUCE_THIN_STUMP").get(),
                TREE_BLOCKS.get("JUNGLE_THICKEST_STUMP").get(), TREE_BLOCKS.get("JUNGLE_THICK_STUMP").get(), TREE_BLOCKS.get("JUNGLE_THIN_STUMP").get(),
                TREE_BLOCKS.get("BIRCH_THICKEST_STUMP").get(), TREE_BLOCKS.get("BIRCH_THICK_STUMP").get(), TREE_BLOCKS.get("BIRCH_THIN_STUMP").get(),
                TREE_BLOCKS.get("ACACIA_THICKEST_STUMP").get(), TREE_BLOCKS.get("ACACIA_THICK_STUMP").get(), TREE_BLOCKS.get("ACACIA_THIN_STUMP").get(),
                TREE_BLOCKS.get("DARK_OAK_THICKEST_STUMP").get(), TREE_BLOCKS.get("DARK_OAK_THICK_STUMP").get(), TREE_BLOCKS.get("DARK_OAK_THIN_STUMP").get()).build(null));
    }

    public static Block getTreeBlock(WoodTypes wood, ThicknessTypes thickness, TreeBlockTypes type, boolean isLeaf, String leafType) {
        if (!isLeaf) {
            return TREE_BLOCKS.get((wood + "_" + thickness + "_" + type).toUpperCase()).get();
        } else {
            if (leafType.equals("slab"))
                return TREE_BLOCKS.get((wood + "_leaves_slab").toUpperCase()).get();
            else if (leafType.equals("stairs"))
                return TREE_BLOCKS.get((wood + "_leaves_stairs").toUpperCase()).get();
            else if (leafType.equals("trapdoor"))
                return TREE_BLOCKS.get((wood + "_leaves_trapdoor").toUpperCase()).get();
        }

        return Blocks.AIR;
    }

    public static Block getTreeBlock(WoodTypes wood, ThicknessTypes thickness, TreeBlockTypes type) {
        return getTreeBlock(wood, thickness, type, false, "");
    }

    public static Block getLeafBlock(WoodTypes wood, String leafType) {
        return getTreeBlock(wood, null, null, true, leafType);
    }

    public static Block getLeafBlock(WoodTypes wood, int leafType) {
        if (leafType == 1)
            return getLeafBlock(wood, "slab");
        else if (leafType == 2)
            return getLeafBlock(wood, "stairs");
        else
            return getLeafBlock(wood, "trapdoor");
    }

    public static Item getTreeItem(WoodTypes wood, ThicknessTypes thickness) {
        return TREE_BLOCK_ITEMS.get((wood + "_" + thickness + "_tree").toUpperCase()).get();
    }

    public static Item getTreeBlockItem(WoodTypes wood, ThicknessTypes thickness, TreeBlockTypes type) {
        return TREE_BLOCK_ITEMS.get((wood + "_" + thickness + "_" + type + "_item").toUpperCase()).get();
    }


    public static Item getLeafBlockItem(WoodTypes wood, String blockType) {
        return TREE_BLOCK_ITEMS.get((wood + "_leaves_" + blockType + "_item").toUpperCase()).get();
    }

    public static Item getTimber(WoodTypes wood) {
        switch (wood) {
            case OAK:
                return OAK_TIMBER.get();
            case DARK_OAK:
                return DARK_OAK_TIMBER.get();
            case JUNGLE:
                return JUNGLE_TIMBER.get();
            case ACACIA:
                return ACACIA_TIMBER.get();
            case BIRCH:
                return BIRCH_TIMBER.get();
            case SPRUCE:
                return SPRUCE_TIMBER.get();
        }
        return OAK_TIMBER.get();
    }
}

