package lilypuree.forest_tree.datagen;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.api.genera.*;
import lilypuree.forest_tree.api.registration.TreeBlockRegistry;
import lilypuree.forest_tree.common.trees.block.BranchBlock;
import lilypuree.forest_tree.common.trees.block.StumpBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

//    public void treeBlockItem(WoodTypes wood, ThicknessTypes thickness, TreeBlockTypes type) {
//        getBuilder(wood + "_" + thickness + "_" + type)
//                .parent(new ModelFile.UncheckedModelFile(modLoc("block/"+wood+"_" + thickness + "_" + type+"_inventory")))
//                .texture("#trunk", mcLoc("block/" + wood + "_log")).texture("#top", mcLoc("block/" + wood + "_log_top"));
//    }
//
//    public void treeItem(WoodTypes wood, ThicknessTypes thickness) {
//        getBuilder(wood + "_" + thickness + "_tree")
//                .parent(new ModelFile.UncheckedModelFile( mcLoc("item/generated")))
//                .texture("layer0",modLoc("item/"+wood+"_"+thickness+"_tree") );
//    }

    @Override
    protected void registerModels() {
//        for (WoodTypes wood : WoodTypes.values()) {
//            for (ThicknessTypes thickness : ThicknessTypes.values()) {
//                for (TreeBlockTypes types : TreeBlockTypes.values()) {
//                    treeBlockItem(wood, thickness, types);
//                }
////                treeItem(wood, thickness);
//            }
////            singleTexture(wood+"_timber", mcLoc("item/generated"), modLoc("item/"+wood+"_timber"));
//        }
        TreeBlockRegistry.woodCategories.forEach(woodCategory -> {
            TreeBlockRegistry.branchBlocks.get(woodCategory).values().forEach(regObject -> branchItem(regObject.get()));
            TreeBlockRegistry.branchEndBlocks.get(woodCategory).values().forEach(regObject -> branchItem(regObject.get()));
            StumpBlock block = TreeBlockRegistry.stumpBlocks.get(woodCategory).get();
            getBuilder(woodCategory.getName() + "_stump").parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", woodCategory.getTruncatedTexturePath());
        });
        TreeBlockRegistry.foliageCategories.stream().filter(FoliageCategory::hasLeafBlock).forEach(foliageCategory -> {
            getBuilder(TreeBlockRegistry.leavesSlabBlocks.get(foliageCategory).getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + foliageCategory.getName() + "_leaves_slab")));
        });
    }

    public void branchItem(BranchBlock block) {
        Vec3i v = block.getSourceOffset();
        String name = block.getWoodCategory().getName() + "_branch_" + (block.isEnd() ? "end_" : "") + v.getX() + "_" + v.getY() + "_" + v.getZ();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", block.getWoodCategory().getTruncatedTexturePath());
    }

    @Override
    public String getName() {
        return "Tree Forest Item Models";
    }
}
