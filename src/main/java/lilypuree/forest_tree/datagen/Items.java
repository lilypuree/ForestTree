package lilypuree.forest_tree.datagen;

import lilypuree.forest_tree.datagen.types.ThicknessTypes;
import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
import lilypuree.forest_tree.datagen.types.WoodTypes;
import lilypuree.forest_tree.setup.Registration;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    public void treeBlockItem(WoodTypes wood, ThicknessTypes thickness, TreeBlockTypes type) {
        getBuilder(wood + "_" + thickness + "_" + type)
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + wood + "_" + thickness + "_" + type + "_inventory")))
                .texture("#trunk", mcLoc("block/" + wood + "_log")).texture("#top", mcLoc("block/" + wood + "_log_top"));
    }

    public void treeItem(WoodTypes wood, ThicknessTypes thickness) {
        singleTexture(wood + "_" + thickness + "_tree", mcLoc("item/generated"), mcLoc("block/oak_sapling"));

    }

    @Override
    protected void registerModels() {
        for (WoodTypes wood : WoodTypes.values()) {
            for (ThicknessTypes thickness : ThicknessTypes.values()) {
                for (TreeBlockTypes types : TreeBlockTypes.values()) {
                    treeBlockItem(wood, thickness, types);
                }
                treeItem(wood, thickness);
            }
        }
        getBuilder(Registration.GRAFTING_TOOL.getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", mcLoc("item/shears"));
        for (WoodTypes leaf : WoodTypes.values()) {
            getBuilder(Registration.TREE_BLOCK_ITEMS.get((leaf + "_leaves_slab").toUpperCase() + "_ITEM").getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + leaf + "_leaves_slab")));
            getBuilder(Registration.TREE_BLOCK_ITEMS.get((leaf + "_leaves_stairs").toUpperCase() + "_ITEM").getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + leaf + "_leaves_stairs")));
            getBuilder(Registration.TREE_BLOCK_ITEMS.get((leaf + "_leaves_trapdoor").toUpperCase() + "_ITEM").getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + leaf + "_leaves_trapdoor_bottom")));
        }
    }


    @Override
    public String getName() {
        return "Tree Forest Item Models";
    }
}
