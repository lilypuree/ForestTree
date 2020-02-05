package lilypuree.forest_tree.blocks;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IShearable;

import java.util.Random;

public class LeavesSlabBlock extends SlabBlock implements IShearable, ILeafBlock{
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE_1_7;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public LeavesSlabBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(super.getDefaultState().with(DISTANCE, 7).with(PERSISTENT, Boolean.FALSE));
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.get(DISTANCE) == 7 && !state.get(PERSISTENT);
    }



    @Override
    public void randomTick(BlockState block, World worldIn, BlockPos pos, Random randomIn) {
        if (!block.get(PERSISTENT) && block.get(DISTANCE) == 7) {
            spawnDrops(block, worldIn, pos);
            worldIn.removeBlock(pos, false);
        }
    }

    @Override
    public void tick(BlockState block, World worldIn, BlockPos pos, Random random) {
        worldIn.setBlockState(pos, updateDistance(block, worldIn, pos), 3);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        stateIn = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        int i = getDistance(facingState) + 1;
        if (i != 1 || stateIn.get(DISTANCE) != i) {
            worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
        }

        return stateIn;
    }

    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }



    private static BlockState updateDistance(BlockState block, IWorld worldIn, BlockPos pos) {
        int i = 7;

        try (BlockPos.PooledMutableBlockPos pooledMutable = BlockPos.PooledMutableBlockPos.retain()) {
            for(Direction direction : Direction.values()) {
                pooledMutable.setPos(pos).move(direction);
                i = Math.min(i, getDistance(worldIn.getBlockState(pooledMutable)) + 1);
                if (i == 1) {
                    break;
                }
            }
        }

        return block.with(DISTANCE, Integer.valueOf(i));
    }

    private static int getDistance(BlockState neighbor) {
        if (BlockTags.LOGS.contains(neighbor.getBlock())) {
            return 0;
        } else {
            return neighbor.getBlock() instanceof LeavesBlock ? neighbor.get(DISTANCE) : 7;
        }
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.isRainingAt(pos.up())) {
            if (rand.nextInt(15) == 1) {
                BlockPos blockpos = pos.down();
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (!blockstate.isSolid() || !blockstate.func_224755_d(worldIn, blockpos, Direction.UP)) {
                    double d0 = (double)((float)pos.getX() + rand.nextFloat());
                    double d1 = (double)pos.getY() - 0.05D;
                    double d2 = (double)((float)pos.getZ() + rand.nextFloat());
                    worldIn.addParticle(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public boolean causesSuffocation(BlockState block, IBlockReader blockReader, BlockPos pos) {
        return false;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(DISTANCE, PERSISTENT);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        assert state != null;
        return updateDistance(state.with(PERSISTENT, Boolean.valueOf(true)), context.getWorld(), context.getPos());
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

    public BlockRenderLayer getRenderLayer() {
        return  BlockRenderLayer.CUTOUT_MIPPED;
    }
}
