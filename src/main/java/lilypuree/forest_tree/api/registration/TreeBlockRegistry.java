package lilypuree.forest_tree.api.registration;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.api.genera.FoliageCategory;
import lilypuree.forest_tree.api.genera.WoodCategory;
import lilypuree.forest_tree.common.trees.block.BranchBlock;
import lilypuree.forest_tree.common.trees.block.LeavesSlabBlock;
import lilypuree.forest_tree.common.trees.block.StumpBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TreeBlockRegistry {

    public static List<WoodCategory> woodCategories = new ArrayList<>();
    public static List<FoliageCategory> foliageCategories = new ArrayList<>();
    public static Map<FoliageCategory, RegistryObject<LeavesSlabBlock>> leavesSlabBlocks = new HashMap<>();
    public static Map<WoodCategory, RegistryObject<StumpBlock>> stumpBlocks = new HashMap<>();
    public static Map<WoodCategory, Map<Vec3i, RegistryObject<BranchBlock>>> branchBlocks = new HashMap<>();
    public static Map<WoodCategory, Map<Vec3i, RegistryObject<BranchBlock>>> branchEndBlocks = new HashMap<>();

    public static void registerWoodCategory(DeferredRegister<Block> blockRegisterer, DeferredRegister<Item> itemRegisterer, WoodCategory category) {
        Map<Vec3i, RegistryObject<BranchBlock>> woodMap = new HashMap<>();
        Map<Vec3i, RegistryObject<BranchBlock>> woodEndMap = new HashMap<>();
        Item.Properties itemProp = new Item.Properties().group(Registration.ITEM_GROUP);

        RegistryObject<StumpBlock> stumpBlock = blockRegisterer.register(category.getName() + "_stump", () -> new StumpBlock(category.getStumpProperties(), category));
        itemRegisterer.register(stumpBlock.getId().getPath(), () -> new BlockItem(stumpBlock.get(), itemProp));

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    Vec3i sourceDir = new Vec3i(x, y, z);
                    String name = category.getName() + "_branch_" + x + "_" + y + "_" + z;
                    String endName = category.getName() + "_branch_end_" + x + "_" + y + "_" + z;

                    RegistryObject<BranchBlock> branchBlock = blockRegisterer.register(name, () -> new BranchBlock(category.getBranchProperties(), category).setSourceOffset(sourceDir).setEnd(false));
                    RegistryObject<BranchBlock> branchEndBlock = blockRegisterer.register(endName, () -> new BranchBlock(category.getBranchProperties(), category).setSourceOffset(sourceDir).setEnd(true));

                    itemRegisterer.register(branchBlock.getId().getPath(), () -> new BlockItem(branchBlock.get(), itemProp));
                    itemRegisterer.register(branchEndBlock.getId().getPath(), () -> new BlockItem(branchEndBlock.get(), itemProp));

                    woodMap.put(sourceDir, branchBlock);
                    woodEndMap.put(sourceDir, branchEndBlock);
                }
            }
        }
        woodCategories.add(category);
        stumpBlocks.put(category, stumpBlock);
        branchBlocks.put(category, woodMap);
        branchEndBlocks.put(category, woodEndMap);
    }

    public static void registerFoliageCategory(DeferredRegister<Block> blockRegisterer, DeferredRegister<Item> itemRegisterer, FoliageCategory category) {
        Item.Properties itemProp = new Item.Properties().group(Registration.ITEM_GROUP);

        if (category.hasLeafBlock()) {
            RegistryObject<LeavesSlabBlock> leavesSlabBlock = blockRegisterer.register(category.getName() + "_leaves_slab", () -> new LeavesSlabBlock(category.getLeavesProperties()));
            itemRegisterer.register(leavesSlabBlock.getId().getPath(), () -> new BlockItem(leavesSlabBlock.get(), itemProp));
            leavesSlabBlocks.put(category, leavesSlabBlock);
        }

        foliageCategories.add(category);
    }

    public static BranchBlock getBranchBlock(Vec3i sourcePos, boolean isEnd, WoodCategory category) {
        if (sourcePos.equals(Vec3i.NULL_VECTOR)) {
            return null;
        }
        if (isEnd) {
            return getBranchEndBlock(sourcePos, category);
        } else
            return getBranchBlock(sourcePos, category);
    }

    public static BranchBlock getBranchBlock(Vec3i sourcePos, WoodCategory category) {
        return branchBlocks.get(category).get(sourcePos).get();
    }

    public static BranchBlock getBranchEndBlock(Vec3i sourcePos, WoodCategory category) {
        return branchEndBlocks.get(category).get(sourcePos).get();
    }

    public static StumpBlock getStumpBlock(WoodCategory category) {
        return stumpBlocks.get(category).get();
    }

    public static void forAllBranchEnds(Consumer<BranchBlock> action){
        branchEndBlocks.values().forEach(map ->{
            map.values().forEach(regObject -> action.accept(regObject.get()));
        });
    }
}
