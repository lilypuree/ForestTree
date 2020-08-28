package lilypuree.forest_tree.common.world.trees.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraftforge.common.IPlantable;

//Most methods here are just copied from Feature/AbstractTreeFeature.
public class PlacementHelper {

    public static boolean canBeReplacedByLogs(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (worldIn instanceof IWorldReader) {
            return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLogs((IWorldReader) worldIn, pos));
        }
        return worldIn.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return state.isAir() || state.isIn(BlockTags.LEAVES) || isDirt(block) || block.isIn(BlockTags.LOGS) || block.isIn(BlockTags.SAPLINGS) || block == Blocks.VINE;
        });
    }

    protected static boolean isDirt(Block blockIn) {
        return net.minecraftforge.common.Tags.Blocks.DIRT.contains(blockIn);
    }

    protected static boolean isDirt(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        return worldIn.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return isDirt(block) && block != Blocks.GRASS_BLOCK && block != Blocks.MYCELIUM;
        });
    }

    @Deprecated //Forge: moved to isSoilOrFarm
    protected static boolean isDirtOrGrassBlockOrFarmland(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        return worldIn.hasBlockState(pos, (state) -> {
            Block block = state.getBlock();
            return isDirt(block) || block == Blocks.GRASS_BLOCK || block == Blocks.FARMLAND;
        });
    }

    protected static boolean canPlantSapling(IWorldGenerationBaseReader reader, BlockPos pos, IPlantable sapling) {
        if (!(reader instanceof IBlockReader) || sapling == null)
            return isDirtOrGrassBlockOrFarmland(reader, pos);
        return reader.hasBlockState(pos, state -> state.canSustainPlant((IBlockReader) reader, pos, Direction.UP, sapling));
    }

    public static boolean isVine(IWorldGenerationBaseReader worldIn, BlockPos posIn) {
        return worldIn.hasBlockState(posIn, (state) -> {
            return state.getBlock() == Blocks.VINE;
        });
    }

    public static boolean isAirOrLeaves(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (worldIn instanceof IWorldReader) // FORGE: Redirect to state method when possible
            return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((IWorldReader) worldIn, pos));
        return worldIn.hasBlockState(pos, (state) -> {
            return state.isAir() || state.isIn(BlockTags.LEAVES);
        });
    }

    public static boolean isTallPlants(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> state.getMaterial() == Material.TALL_PLANTS);
    }

    public static boolean isWater(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        return worldIn.hasBlockState(pos, (state) -> state.getBlock() == Blocks.WATER);
    }
}
