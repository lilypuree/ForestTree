package lilypuree.forest_tree.blocks_old;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LeavesTrapDoorBlock extends LeavesBlock implements ILeafBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape EAST_OPEN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_OPEN_AABB = Block.makeCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_OPEN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_OPEN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape BOTTOM_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    protected static final VoxelShape TOP_AABB = Block.makeCuboidShape(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public LeavesTrapDoorBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(super.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(OPEN, Boolean.valueOf(false)).with(HALF, Half.BOTTOM).with(POWERED, Boolean.valueOf(false)).with(WATERLOGGED, Boolean.valueOf(false)));
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (!state.get(OPEN)) {
            return state.get(HALF) == Half.TOP ? TOP_AABB : BOTTOM_AABB;
        } else {
            switch ((Direction) state.get(HORIZONTAL_FACING)) {
                case NORTH:
                default:
                    return NORTH_OPEN_AABB;
                case SOUTH:
                    return SOUTH_OPEN_AABB;
                case WEST:
                    return WEST_OPEN_AABB;
                case EAST:
                    return EAST_OPEN_AABB;
            }
        }
    }


    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        switch (type) {
            case LAND:
                return state.get(OPEN);
            case WATER:
                return state.get(WATERLOGGED);
            case AIR:
                return state.get(OPEN);
            default:
                return false;
        }
    }


    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if (this.material == Material.IRON) {
            return ActionResultType.PASS;
        } else {
            state = state.cycle(OPEN);
            worldIn.setBlockState(pos, state, 2);
            if (state.get(WATERLOGGED)) {
                worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
            }

            this.playSound(player, worldIn, pos, state.get(OPEN));
            return ActionResultType.SUCCESS;
        }
    }

    protected void playSound(@Nullable PlayerEntity player, World worldIn, BlockPos pos, boolean p_185731_4_) {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_GRASS_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            boolean flag = worldIn.isBlockPowered(pos);
            if (flag != state.get(POWERED)) {
                if (state.get(OPEN) != flag) {
                    state = state.with(OPEN, Boolean.valueOf(flag));
                    this.playSound((PlayerEntity) null, worldIn, pos, flag);
                }

                worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag)), 2);
                if (state.get(WATERLOGGED)) {
                    worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
                }
            }

        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = super.getStateForPlacement(context);
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        Direction direction = context.getFace();
        if (!context.replacingClickedOnBlock() && direction.getAxis().isHorizontal()) {
            blockstate = blockstate.with(HORIZONTAL_FACING, direction).with(HALF, context.getHitVec().y - (double) context.getPos().getY() > 0.5D ? Half.TOP : Half.BOTTOM);
        } else {
            blockstate = blockstate.with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(HALF, direction == Direction.UP ? Half.BOTTOM : Half.TOP);
        }

        if (context.getWorld().isBlockPowered(context.getPos())) {
            blockstate = blockstate.with(OPEN, Boolean.valueOf(true)).with(POWERED, Boolean.valueOf(true));
        }

        return blockstate.with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(HORIZONTAL_FACING, OPEN, HALF, POWERED, WATERLOGGED);
    }

    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }


    @Override
    public boolean isLadder(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, net.minecraft.entity.LivingEntity entity) {
        if (state.get(OPEN)) {
            BlockState down = world.getBlockState(pos.down());
            if (down.getBlock() == net.minecraft.block.Blocks.LADDER)
                return down.get(LadderBlock.FACING) == state.get(HORIZONTAL_FACING);
        }
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }
}
