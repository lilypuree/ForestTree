package lilypuree.forest_tree.common.trees.block.trees;

import lilypuree.forest_tree.core.registry.FeatureRegistry;
import lilypuree.forest_tree.common.trees.customization.CustomSaplingTile;
import lilypuree.forest_tree.common.world.trees.gen.feature.TreeConfig;
import lilypuree.forest_tree.common.world.trees.gen.feature.TreeGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Random;

//A Copy of AdvancedTree, with slight differences
public class CustomTree {

    private TreeGenerator treeGenerator;

    public CustomTree(TreeGenerator treeGeneratorIn) {
        this.treeGenerator = treeGeneratorIn;
    }

    public ConfiguredFeature<TreeConfig, ?> getTreeFeature(Random randomIn, boolean canPlace, int age) {
//        return FeatureRegistry.CUSTOM.withGenerator(treeGenerator).withConfiguration(new TreeConfig.Builder(ModSpecies.PINE).age(age).build());
        return null;
    }

    public boolean place(IWorld worldIn, ChunkGenerator<?> generatorIn, BlockPos posIn, BlockState stateIn, Random randomIn, int age) {
        ConfiguredFeature<TreeConfig, ?> configuredFeature = this.getTreeFeature(randomIn, canPlaceTree(worldIn, posIn), age);
        if (configuredFeature == null) {
            return false;
        } else {
            TileEntity tileEntity = worldIn.getTileEntity(posIn);
            CompoundNBT treeData = new CompoundNBT();
            if (tileEntity instanceof CustomSaplingTile) {
                tileEntity.write(treeData);
            }

            worldIn.setBlockState(posIn, Blocks.AIR.getDefaultState(), 4);

//            ((AdvancedTreeFeatureConfig)configuredFeature.config).forcePlacement();
            if (configuredFeature.place(worldIn, generatorIn, randomIn, posIn)) {
                return true;
            } else {
                worldIn.setBlockState(posIn, stateIn, 4);
                TileEntity newTileEntity = worldIn.getTileEntity(posIn);
                if (newTileEntity instanceof CustomSaplingTile) {
                    newTileEntity.read(treeData);
                }
                return false;
            }
        }
    }

    private boolean canPlaceTree(IWorld worldIn, BlockPos posIn) {
        for (BlockPos blockPos : BlockPos.Mutable.getAllInBoxMutable(posIn.down().north(2).west(2), posIn.up().south(2).east(2))) {
            if (worldIn.getBlockState(blockPos).isIn(BlockTags.FLOWERS)) {
                return true;
            }
        }
        return false;
    }
}
