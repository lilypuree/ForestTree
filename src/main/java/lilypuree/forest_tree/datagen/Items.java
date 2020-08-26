package lilypuree.forest_tree.datagen;

import lilypuree.forest_tree.datagen.types.ThicknessTypes;
import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
import lilypuree.forest_tree.datagen.types.WoodTypes;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.TreeBlocks;
import lilypuree.forest_tree.trees.species.ModSpecies;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
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
        for (Species species : ModSpecies.allSpecies()) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;
                        String name = species.getName() + "_branch_" + x + "_" + y + "_" + z;
                        String endName = species.getName() + "_branch_end_" + x + "_" + y + "_" + z;

                        Vec3i sourceDir = new Vec3i(x, y, z);

                        getBuilder(name).parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                                .texture("layer0", species.getTruncatedTexturePath());
                        getBuilder(endName).parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                                .texture("layer0", species.getTruncatedTexturePath());
                        getBuilder(species.getName() + "_stump").parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                                .texture("layer0", species.getTruncatedTexturePath());
                    }
                }
            }
//            getBuilder(Registration.LEAVES_SLAB_BLOCKS.get(species).getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + species.getName() + "_leaves_slab")));
        }

    }


    @Override
    public String getName() {
        return "Tree Forest Item Models";
    }
}
