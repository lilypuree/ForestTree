package lilypuree.forest_tree.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.Random;
import java.util.function.Supplier;

public class LeavesStairBlock extends StairsBlock implements IShearable, ILeafBlock{
    public LeavesStairBlock(Supplier<BlockState> state, Block.Properties properties){
        super(state, properties);
    }

    @Override
    public boolean causesSuffocation(BlockState p_229869_1_, IBlockReader p_229869_2_, BlockPos p_229869_3_) {
        return false;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.isRainingAt(pos.up())) {
            if (rand.nextInt(15) == 1) {
                BlockPos blockpos = pos.down();
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (!blockstate.isSolid() || !blockstate.isSolidSide(worldIn, blockpos, Direction.UP)) {
                    double d0 = (double)((float)pos.getX() + rand.nextFloat());
                    double d1 = (double)pos.getY() - 0.05D;
                    double d2 = (double)((float)pos.getZ() + rand.nextFloat());
                    worldIn.addParticle(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        super.animateTick(stateIn, worldIn, pos, rand);
    }

    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    @Override
    public boolean isFoliage(BlockState state, IWorldReader world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 60;
    }
}
