package lilypuree.forest_tree.datagen;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.trees.TreeBlocks;
import lilypuree.forest_tree.trees.species.ModSpecies;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.generators.*;

import java.io.IOException;

public class BlockStates extends BlockStateProvider {

    private BranchModelProvider branchModels;

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ForestTree.MODID, exFileHelper);
        branchModels = new BranchModelProvider(gen, ForestTree.MODID, exFileHelper) {
            @Override
            public String getName() {
                return this.getName();
            }

            @Override
            protected void registerModels() {
            }
        };
    }
//
//    public ModelFile treePartModel(WoodTypes wood, ThicknessTypes thickness, PartTypes type) {
//        return addTreeTextures(models().getBuilder(wood + "_" + thickness + "_part_" + type).parent(partModel(thickness, type)), wood);
//    }
//
//    public ModelFile treeInventoryModel(WoodTypes wood, ThicknessTypes thickness, TreeBlockTypes type) {
//        return addTreeTextures(models().getBuilder(wood + "_" + thickness + "_" + type + "_inventory").parent(inventoryModel(thickness, type)), wood);
//    }
//
//    private ModelBuilder<?> addTreeTextures(ModelBuilder<?> builder, WoodTypes wood) {
//        return builder.texture("trunk", mcLoc("block/" + wood + "_log")).texture("top", mcLoc("block/" + wood + "_log_top"));
//    }
//
//    public ModelFile partModel(ThicknessTypes thickness, PartTypes type) {
//        ModelBuilder<?> base = models().getBuilder(thickness + "_part_" + type).texture("particle", "#trunk");
//        float f1 = 8.0F - thickness.getWidth();
//        float f2 = 8.0F + thickness.getWidth();
//        switch (type) {
//            case CENTER:
//                return base.element().from(f1, 0, f1).to(f2, 16.0F, f2).allFaces(this::setFaceBuilderTextures).end();
//            case SIDE:
//                return base.element().from(f1, 0, f1).to(f2, f1, f2).allFaces(this::setFaceBuilderTextures).end();
//            case UP:
//                return base.element().from(f1 + 0.02F, 0, f1).to(f2 - 0.02F, 11.32F, f2).rotation().origin(8, 0, 8).angle(-45.0F).axis(Direction.Axis.X).end().allFaces(this::setFaceBuilderTextures).end();
//            case STUMP:
//                return base.element().from(f1, 0, f1).to(f2, thickness.getWidth() * 1.5F, f2).allFaces(this::setFaceBuilderTextures).end();
//
//            case ROOT:
//                return base.element().from(f1 - 1F, 0, f1 - 1F).to(6.0F, thickness.getWidth(), 6.0F).textureAll("#trunk").end();
//
//        }
//        return base;
//    }
//
//    private void setFaceBuilderTextures(Direction dir, ModelBuilder<?>.ElementBuilder.FaceBuilder faceBuilder){
//        if (dir.getHorizontalIndex() < 0) {
//            faceBuilder.texture("#top");
//        } else {
//            faceBuilder.texture("#trunk");
//        }
//    }
//
//    public ModelFile inventoryModel(ThicknessTypes thickness, TreeBlockTypes type) {
//        float f1 = 8.0F - thickness.getWidth();
//        float f2 = 8.0F + thickness.getWidth();
//        ModelBuilder<?> base = models().withExistingParent(thickness + "_" + type + "_inventory", mcLoc("block/block")).transforms()
//                .transform(ModelBuilder.Perspective.GUI).rotation(30, 135, 0).scale(0.625F, 0.625F, 0.625F).translation(0, 0, 0).end()
//                .transform(ModelBuilder.Perspective.FIXED).rotation(0, 90, 0).scale(0.5F, 0.5F, 0.5F).translation(0, 0, 0).end().end()
//                .ao(false).texture("particle", "#trunk");
//        switch (type) {
//            case TRUNK:
//                return base.element().from(f1, 0.0F, f1).to(f2, 16.0F, f2).allFaces((dir, faceBuilder) -> {
//                    if (dir.getHorizontalIndex() < 0) {
//                        faceBuilder.texture("#top");
//                    } else {
//                        faceBuilder.texture("#trunk");
//                    }
//                }).end().element().from(f1, f1, 0.0F).to(f2, f2, f1).allFaces((dir, faceBuilder) -> faceBuilder.texture("#trunk")).end();
//            case BRANCH:
//                return base.element().from(f1, f1, 0.0F).to(f2, f2, 16.0F).allFaces((dir, faceBuilder) -> {
//                    if (dir == Direction.NORTH || dir == Direction.SOUTH) {
//                        faceBuilder.texture("#top");
//                    } else {
//                        faceBuilder.texture("#trunk");
//                    }
//                }).end().element().from(f2, f1 + 2.0F, f1 + 2.0F).to(16.0F, f2 - 2.0F, f2 - 2.0F).allFaces((dir, faceBuilder) -> faceBuilder.texture("#trunk")).end();
//            case STUMP:
//                return base.element().from(f1, 0.0F, f1).to(f2, thickness.getWidth() * 1.5F, f2).allFaces((dir, faceBuilder) -> {
//                    if (dir.getHorizontalIndex() < 0) {
//                        faceBuilder.texture("#top");
//                    } else {
//                        faceBuilder.texture("#trunk");
//                    }
//                }).end().element().from(f1 - 1F, 0, f1 - 1F).to(6.0F, thickness.getWidth(), 6.0F).textureAll("#trunk").end()
//                        .element().from(f1 - 1F, 0, 8.0F).to(6.0F, thickness.getWidth(), f1 + 1F).textureAll("#trunk").end()
//                        .element().from(8.0F, 0, f1 - 1F).to(f1 + 1F, thickness.getWidth(), 6.0F).textureAll("#trunk").end()
//                        .element().from(8.0F, 0, 8.0F).to(f1 + 1.0F, thickness.getWidth(), f1 + 1.0F).textureAll("#trunk").end();
//        }
//        return base;
//    }
//
//    public ModelFile leafSlab(String name, ResourceLocation sideBottomTopTexture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/leaf_slab")))
//                .texture("side", sideBottomTopTexture)
//                .texture("bottom", sideBottomTopTexture)
//                .texture("top", sideBottomTopTexture);
//    }
//
//    public ModelFile leafSlabTop(String name, ResourceLocation sideBottomTopTexture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/leaf_slab_top")))
//                .texture("side", sideBottomTopTexture)
//                .texture("bottom", sideBottomTopTexture)
//                .texture("top", sideBottomTopTexture);
//    }
//
//    public ModelFile leafStairs(String name, ResourceLocation sideBottomTopTexture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/leaf_stairs")))
//                .texture("side", sideBottomTopTexture)
//                .texture("bottom", sideBottomTopTexture)
//                .texture("top", sideBottomTopTexture);
//    }
//
//    public ModelFile leafStairsOuter(String name, ResourceLocation sideBottomTopTexture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/leaf_outer_stairs")))
//                .texture("side", sideBottomTopTexture)
//                .texture("bottom", sideBottomTopTexture)
//                .texture("top", sideBottomTopTexture);
//    }
//
//    public ModelFile leafStairsInner(String name, ResourceLocation sideBottomTopTexture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/leaf_inner_stairs")))
//                .texture("side", sideBottomTopTexture)
//                .texture("bottom", sideBottomTopTexture)
//                .texture("top", sideBottomTopTexture);
//    }
//
//    public ModelFile leafTrapdoorBottom(String name, ResourceLocation texture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/template_leaf_trapdoor_bottom")))
//                .texture("texture", texture);
//    }
//
//    public ModelFile leafTrapdoorTop(String name, ResourceLocation texture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/template_leaf_trapdoor_top")))
//                .texture("texture", texture);
//    }
//
//    public ModelFile leafTrapdoorOpen(String name, ResourceLocation texture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/template_leaf_trapdoor_open")))
//                .texture("texture", texture);
//    }
//
//    public ModelFile leafTrapdoorOrientableBottom(String name, ResourceLocation texture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/template_leaf_orientable_trapdoor_bottom")))
//                .texture("texture", texture);
//    }
//
//    public ModelFile leafTrapdoorOrientableTop(String name, ResourceLocation texture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/template_leaf_orientable_trapdoor_top")))
//                .texture("texture", texture);
//    }
//
//    public ModelFile leafTrapdoorOrientableOpen(String name, ResourceLocation texture) {
//        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/template_leaf_orientable_trapdoor_open")))
//                .texture("texture", texture);
//    }

//
//    public void treeBlock(WoodTypes wood, ThicknessTypes thickness, TreeBlockTypes type) {
//        treeInventoryModel(wood, thickness, type);
//        MultiPartBlockStateBuilder builder = getMultipartBuilder(Registration.TREE_BLOCKS.get((wood + "_" + thickness + "_" + type).toUpperCase()).get());
//        ModelFile centerModel = treePartModel(wood, thickness, PartTypes.CENTER);
//        ModelFile sideModel = treePartModel(wood, thickness, PartTypes.SIDE);
//        ModelFile upModel = treePartModel(wood, thickness, PartTypes.UP);
//        switch (type) {
//            case TRUNK:
//                builder.part().modelFile(centerModel).addModel().condition(BlockStateProperties.UP, true).end()
//                        .part().modelFile(sideModel).rotationX(90).rotationY(180).addModel().condition(ForestTreeProperties.NORTH_CONNECTION, BranchType.SIDE).end()
//                        .part().modelFile(sideModel).rotationX(90).rotationY(90).addModel().condition(ForestTreeProperties.WEST_CONNECTION, BranchType.SIDE).end()
//                        .part().modelFile(sideModel).rotationX(90).rotationY(0).addModel().condition(ForestTreeProperties.SOUTH_CONNECTION, BranchType.SIDE).end()
//                        .part().modelFile(sideModel).rotationX(90).rotationY(270).addModel().condition(ForestTreeProperties.EAST_CONNECTION, BranchType.SIDE).end()
//                        .part().modelFile(upModel).rotationY(0).addModel().condition(ForestTreeProperties.NORTH_CONNECTION, BranchType.UP).end()
//                        .part().modelFile(upModel).rotationY(270).addModel().condition(ForestTreeProperties.WEST_CONNECTION, BranchType.UP).end()
//                        .part().modelFile(upModel).rotationY(180).addModel().condition(ForestTreeProperties.SOUTH_CONNECTION, BranchType.UP).end()
//                        .part().modelFile(upModel).rotationY(90).addModel().condition(ForestTreeProperties.EAST_CONNECTION, BranchType.UP).end();
//                break;
//            case BRANCH:
//                ModelFile branchModel = treePartModel(wood, thickness.thinner(), PartTypes.SIDE);
//                builder.part().modelFile(centerModel).rotationX(90).addModel().condition(ForestTreeProperties.MAIN_BRANCH, true).condition(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH, Direction.SOUTH).end()
//                        .part().modelFile(centerModel).rotationX(90).rotationY(90).addModel().condition(ForestTreeProperties.MAIN_BRANCH, true).condition(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST, Direction.WEST).end()
//                        .part().modelFile(sideModel).rotationX(180).addModel().condition(ForestTreeProperties.CONNECTION, BranchType.SIDE).end()
//                        .part().modelFile(upModel).rotationX(180).rotationY(180).addModel().condition(ForestTreeProperties.CONNECTION, BranchType.UP).condition(BlockStateProperties.FACING, Direction.NORTH).end()
//                        .part().modelFile(upModel).rotationX(180).rotationY(90).addModel().condition(ForestTreeProperties.CONNECTION, BranchType.UP).condition(BlockStateProperties.FACING, Direction.WEST).end()
//                        .part().modelFile(upModel).rotationX(180).rotationY(0).addModel().condition(ForestTreeProperties.CONNECTION, BranchType.UP).condition(BlockStateProperties.FACING, Direction.SOUTH).end()
//                        .part().modelFile(upModel).rotationX(180).rotationY(270).addModel().condition(ForestTreeProperties.CONNECTION, BranchType.UP).condition(BlockStateProperties.FACING, Direction.EAST).end()
//                        .part().modelFile(branchModel).rotationX(90).rotationY(270).addModel().condition(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).condition(ForestTreeProperties.RIGHT_BRANCH, true).end()
//                        .part().modelFile(branchModel).rotationX(90).rotationY(270).addModel().condition(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).condition(ForestTreeProperties.LEFT_BRANCH, true).end()
//                        .part().modelFile(branchModel).rotationX(90).rotationY(90).addModel().condition(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).condition(ForestTreeProperties.LEFT_BRANCH, true).end()
//                        .part().modelFile(branchModel).rotationX(90).rotationY(90).addModel().condition(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).condition(ForestTreeProperties.RIGHT_BRANCH, true).end()
//                        .part().modelFile(branchModel).rotationX(90).rotationY(0).addModel().condition(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).condition(ForestTreeProperties.RIGHT_BRANCH, true).end()
//                        .part().modelFile(branchModel).rotationX(90).rotationY(0).addModel().condition(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).condition(ForestTreeProperties.LEFT_BRANCH, true).end()
//                        .part().modelFile(branchModel).rotationX(90).rotationY(180).addModel().condition(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST).condition(ForestTreeProperties.LEFT_BRANCH, true).end()
//                        .part().modelFile(branchModel).rotationX(90).rotationY(180).addModel().condition(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).condition(ForestTreeProperties.RIGHT_BRANCH, true).end();
//                break;
//            case STUMP:
//                ModelFile stumpModel = treePartModel(wood, thickness, PartTypes.STUMP);
//                ModelFile rootModel = treePartModel(wood, thickness, PartTypes.ROOT);
//                builder.part().modelFile(stumpModel).addModel().condition(ForestTreeProperties.STUMP, true).end()
//                        .part().modelFile(centerModel).addModel().condition(ForestTreeProperties.STUMP, false).end()
//                        .part().modelFile(rootModel).addModel().end()
//                        .part().modelFile(rootModel).rotationY(90).addModel().end()
//                        .part().modelFile(rootModel).rotationY(180).addModel().end()
//                        .part().modelFile(rootModel).rotationY(270).addModel().end();
//                break;
//        }
//
//    }

//
//    public void leafSlabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation sideBottomTopTexture) {
//        slabBlock(block, leafSlab(name(block), sideBottomTopTexture), leafSlabTop(name(block) + "_top", sideBottomTopTexture), models().getExistingFile(doubleslab));
//    }
//
//    public void leafStairsBlock(StairsBlock block, ResourceLocation texture) {
//        String name = block.getRegistryName().toString();
//        ModelFile stairs = leafStairs(name, texture);
//        ModelFile stairsInner = leafStairsInner(name + "_inner", texture);
//        ModelFile stairsOuter = leafStairsOuter(name + "_outer", texture);
//        stairsBlock(block, stairs, stairsInner, stairsOuter);
//    }
//
//    public void leafTrapdoorBlock(TrapDoorBlock block, ResourceLocation texture, boolean orientable) {
//        String baseName = block.getRegistryName().toString();
//        ModelFile bottom = orientable ? leafTrapdoorOrientableBottom(baseName + "_bottom", texture) : leafTrapdoorBottom(baseName + "_bottom", texture);
//        ModelFile top = orientable ? leafTrapdoorOrientableTop(baseName + "_top", texture) : leafTrapdoorTop(baseName + "_top", texture);
//        ModelFile open = orientable ? leafTrapdoorOrientableOpen(baseName + "_open", texture) : leafTrapdoorOpen(baseName + "_open", texture);
//        trapdoorBlock(block, bottom, top, open, orientable);
//    }


    private String name(Block block) {
        return block.getRegistryName().getPath();
    }

    @Override
    protected void registerStatesAndModels() {
        for (Species species : ModSpecies.allSpecies()) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        String name = species.getName() + "_branch_" + x + "_" + y + "_" + z;
                        String endName = species.getName() + "_branch_end_" + x + "_" + y + "_" + z;

                        Vec3i sourceDir = new Vec3i(x, y, z);

                        branchBlock(TreeBlocks.getBranchBlock(sourceDir, species),species, sourceDir, name);
                        branchBlock(TreeBlocks.getBranchEndBlock(sourceDir, species),species, sourceDir, endName);
                    }
                }
            }
        }
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        super.act(cache);
        registeredBlocks.clear();
        branchModels.generateAll(cache);
    }

    public void branchBlock(Block block, Species species, Vec3i source, String name) {
        ModelFile defaultModel = getBranchModel(species, name, source);
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(defaultModel).build());
    }

    public ModelFile getBranchModel(Species species, String name, Vec3i source) {
        return branchModels.getBuilder("block/branch/" + name).parent(new ModelFile.UncheckedModelFile(modLoc("block/branch/"+species.getName()+"_branch_base"))).source(source);
    }
}
