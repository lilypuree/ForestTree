package lilypuree.forest_tree.trees.block.trees;

import lilypuree.forest_tree.trees.world.gen_old.feature.AdvancedTreeFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class AdvancedTree {

    @Nullable
    protected abstract ConfiguredFeature<NoFeatureConfig, ?>getTreeFeature(Random randomIn, boolean canPlace, int age);

    public boolean place(IWorld worldIn, ChunkGenerator<?> generatorIn, BlockPos posIn, BlockState stateIn, Random randomIn, int ageIn){
        ConfiguredFeature<NoFeatureConfig, ?> configuredFeature = this.getTreeFeature(randomIn, canPlaceTree(worldIn,posIn) ,ageIn);
        if(configuredFeature == null){
            return false;
        }else {
            worldIn.setBlockState(posIn, Blocks.AIR.getDefaultState(), 4);

//            ((AdvancedTreeFeatureConfig)configuredFeature.config).forcePlacement();
            if(configuredFeature.place(worldIn,generatorIn,randomIn,posIn)){
                return true;
            }else {
                worldIn.setBlockState(posIn, stateIn, 4);
                return false;
            }
        }
    }

    private boolean canPlaceTree(IWorld worldIn, BlockPos posIn){
        for(BlockPos blockPos : BlockPos.Mutable.getAllInBoxMutable(posIn.down().north(2).west(2), posIn.up().south(2).east(2))){
            if(worldIn.getBlockState(blockPos).isIn(BlockTags.FLOWERS)){
                return true;
            }
        }
        return false;
    }
}
