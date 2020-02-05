package lilypuree.forest_tree.blocks;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lilypuree.forest_tree.blocks.properties.BranchType;
import lilypuree.forest_tree.blocks.properties.ForestTreeProperties;
import lilypuree.forest_tree.blocks.properties.LeafSlabType;
import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
import lilypuree.forest_tree.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TreeBranch extends Block implements IWaterLoggable, ITreeBlock {

    public static final EnumProperty<LeafSlabType> LEAVES_SLAB_TYPE = ForestTreeProperties.LEAVES_SLAB_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<BranchType> CONNECTION = ForestTreeProperties.CONNECTION;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty RIGHT_BRANCH = ForestTreeProperties.RIGHT_BRANCH;
    public static final BooleanProperty LEFT_BRANCH = ForestTreeProperties.LEFT_BRANCH;
    public static final BooleanProperty MAIN_BRANCH = ForestTreeProperties.MAIN_BRANCH;
    private final Object2IntMap<BlockState> field_223008_i = new Object2IntOpenHashMap<>();
    protected final VoxelShape[] shapes;
    protected final float nodeWidth;
    protected final float extensionWidth;

    public TreeBranch(float nodeWidth, float extensionWidth, Block.Properties properties) {
        super(properties);
        this.nodeWidth = nodeWidth;
        this.extensionWidth = extensionWidth;
        this.shapes = this.makeShapes(nodeWidth, extensionWidth);
    }


    protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth) {
        float f = 8.0F - nodeWidth;
        float f1 = 8.0F + nodeWidth;
        float f2 = 8.0F - extensionWidth;
        float f3 = 8.0F + extensionWidth;
        VoxelShape ns = Block.makeCuboidShape(f, f, 0.0D, f1, f1, 16.0D);
        VoxelShape ew = Block.makeCuboidShape(0.0D, f, f, 16.0D, f1, f1);
        VoxelShape u = Block.makeCuboidShape(f, f, f, f1, 16.0D, f1);
        VoxelShape n = Block.makeCuboidShape(f2, f2, 0.0D, f3, f3, f);
        VoxelShape s = Block.makeCuboidShape(f2, f2, f1, f3, f3, 16.0D);
        VoxelShape e = Block.makeCuboidShape(f1, f2, f2, 16.0D, f3, f3);
        VoxelShape w = Block.makeCuboidShape(0.0D, f2, f2, f, f3, f3);


        VoxelShape[] ns_branch_shapes = new VoxelShape[]{ns, VoxelShapes.or(ns, e), VoxelShapes.or(ns, w), VoxelShapes.or(ns, e, w)};
        VoxelShape[] ns_branch_with_connection = Arrays.stream(ns_branch_shapes).map(p -> VoxelShapes.or(p, u)).toArray(VoxelShape[]::new);

        VoxelShape[] ew_branch_shapes = new VoxelShape[]{ew, VoxelShapes.or(ew, s), VoxelShapes.or(ew, n), VoxelShapes.or(ew, n, s)};
        VoxelShape[] ew_branch_with_connection = Arrays.stream(ew_branch_shapes).map(p -> VoxelShapes.or(p, u)).toArray(VoxelShape[]::new);

        VoxelShape[] avoxelshape = ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(new VoxelShape[]{u}, ns_branch_shapes), ew_branch_shapes), ns_branch_with_connection), ew_branch_with_connection);

        return avoxelshape;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.shapes[this.getIndex(state)];
    }

    protected int getIndex(BlockState state) {
        return this.field_223008_i.computeIntIfAbsent(state, (blockState) -> {
            int i = 0;
            if (blockState.get(MAIN_BRANCH)) {
                if (blockState.get(CONNECTION) != BranchType.NONE) {
                    i += 8;
                }
                switch (blockState.get(FACING)) {
                    case NORTH:
                    case SOUTH:
                        i++;
                        break;
                    case EAST:
                    case WEST:
                        i += 5;
                        break;
                }
                if (blockState.get(RIGHT_BRANCH)) {
                    i++;
                    if (blockState.get(FACING) == Direction.SOUTH || blockState.get(FACING) == Direction.WEST) {
                        i++;
                    }
                }
                if (blockState.get(LEFT_BRANCH)) {
                    i++;
                    if (blockState.get(FACING) == Direction.EAST || blockState.get(FACING) == Direction.NORTH) {
                        i++;
                    }
                }
            }
            return i;
        });
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState block, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult rayTraceResult) {
        Direction facing = getHitSide(block, pos, rayTraceResult);
        if (playerIn.getHeldItem(handIn).getItem() == Registration.GRAFTING_TOOL.get()) {
            if (block.get(ForestTreeProperties.MAIN_BRANCH)) {
                if (!worldIn.isRemote()) {
                    if (facing == Direction.DOWN) {
                        worldIn.setBlockState(pos, block.with(CONNECTION, BranchType.UP).with(MAIN_BRANCH, false).with(RIGHT_BRANCH, false).with(LEFT_BRANCH, false));
                    } else if (facing == Direction.UP) {
                        worldIn.setBlockState(pos, block.cycle(CONNECTION));
                    } else if (facing == block.get(FACING).rotateY()) {
                        worldIn.setBlockState(pos, block.cycle(RIGHT_BRANCH));
                    } else if (facing == block.get(FACING).rotateYCCW()) {
                        worldIn.setBlockState(pos, block.cycle(LEFT_BRANCH));
                    }
                }
                return ActionResultType.SUCCESS;
            } else if (facing == block.get(FACING).getOpposite()) {
                if (!worldIn.isRemote()) worldIn.setBlockState(pos, block.with(MAIN_BRANCH, true));
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(block, worldIn, pos, playerIn, handIn, rayTraceResult);
    }


    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CONNECTION, FACING, MAIN_BRANCH, RIGHT_BRANCH, LEFT_BRANCH, WATERLOGGED, LEAVES_SLAB_TYPE);
    }

    @Override
    public TreeBlockTypes getTreeBlockType() {
        return TreeBlockTypes.BRANCH;
    }

    public Direction getHitSide(BlockState block, BlockPos pos, BlockRayTraceResult rayTraceResult) {
        Vec3d hit = rayTraceResult.getHitVec();
        double x = hit.x - pos.getX();
        double y = hit.y - pos.getY();
        double z = hit.z - pos.getZ();
        double f = 0.5D - (double) nodeWidth / 16;
        double f1 = 0.5D + (double) nodeWidth / 16;
        if (block.get(MAIN_BRANCH)) {
            if (y < 1.0D && y > f1) {
                return Direction.UP;
            } else if (x > 0.0D && x < f) {
                return Direction.WEST;
            } else if (x < 1.0D && x > f1) {
                return Direction.EAST;
            } else if (z > 0.0D && z < f) {
                return Direction.NORTH;
            } else if (z < 1.0D && z > f1) {
                return Direction.SOUTH;
            } else if (y > 0.0D && y < f){
                return Direction.DOWN;
            }
        }
        return rayTraceResult.getFace();
    }


    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 20;
    }
}
