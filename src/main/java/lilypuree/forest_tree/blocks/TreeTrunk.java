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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LeadItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
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

public class TreeTrunk extends Block implements IWaterLoggable, ITreeBlock {
//    public static final EnumProperty<LeafSlabType> LEAVES_SLAB_TYPE = ForestTreeProperties.LEAVES_SLAB_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<BranchType> NORTH_CONNECTION = ForestTreeProperties.NORTH_CONNECTION;
    public static final EnumProperty<BranchType> SOUTH_CONNECTION = ForestTreeProperties.SOUTH_CONNECTION;
    public static final EnumProperty<BranchType> EAST_CONNECTION = ForestTreeProperties.EAST_CONNECTION;
    public static final EnumProperty<BranchType> WEST_CONNECTION = ForestTreeProperties.WEST_CONNECTION;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    private final Object2IntMap<BlockState> field_223008_i = new Object2IntOpenHashMap<>();

    protected final VoxelShape[] shapesWithUp;
    protected final VoxelShape[] shapes;
    protected final float nodeWidth;
    protected final float extensionWidth;

    public TreeTrunk(float nodeWidth, float extensionWidth, Block.Properties properties) {
        super(properties);
        this.nodeWidth = nodeWidth;
        this.extensionWidth = extensionWidth;
        this.shapes = this.makeShapes(0.0F, extensionWidth);
        this.shapesWithUp = this.makeShapes(nodeWidth, extensionWidth);
    }

    protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth) {
        float f = 8.0F - nodeWidth;
        float f1 = 8.0F + nodeWidth;
        float f2 = 8.0F - extensionWidth;
        float f3 = 8.0F + extensionWidth;
        VoxelShape up = Block.makeCuboidShape((double) f, 0.0D, (double) f, (double) f1, (double) 16.0F, (double) f1);
        VoxelShape n = Block.makeCuboidShape((double) f2, (double) f2, 0.0D, (double) f3, (double) f3, (double) f3);
        VoxelShape s = Block.makeCuboidShape((double) f2, (double) f2, (double) f2, (double) f3, (double) f3, 16.0D);
        VoxelShape e = Block.makeCuboidShape(0.0D, (double) f2, (double) f2, (double) f3, (double) f3, (double) f3);
        VoxelShape w = Block.makeCuboidShape((double) f2, (double) f2, (double) f2, 16.0D, (double) f3, (double) f3);
        VoxelShape nw = VoxelShapes.or(n, w);
        VoxelShape se = VoxelShapes.or(s, e);
        VoxelShape[] avoxelshape = new VoxelShape[]{VoxelShapes.empty(), s, e, se, n, VoxelShapes.or(s, n), VoxelShapes.or(e, n), VoxelShapes.or(se, n), w, VoxelShapes.or(s, w), VoxelShapes.or(e, w), VoxelShapes.or(se, w), nw, VoxelShapes.or(s, nw), VoxelShapes.or(e, nw), VoxelShapes.or(se, nw)};

        for (int i = 0; i < 16; ++i) {
            avoxelshape[i] = VoxelShapes.or(up, avoxelshape[i]);
        }

        return avoxelshape;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(UP) ? this.shapesWithUp[this.getIndex(state)] : this.shapes[this.getIndex(state)];
    }

    private static int getMask(Direction facing) {
        return 1 << facing.getHorizontalIndex();
    }

    protected int getIndex(BlockState state) {
        return this.field_223008_i.computeIntIfAbsent(state, (blockState) -> {
            int i = 0;
            if (blockState.get(NORTH_CONNECTION) != BranchType.NONE) {
                i |= getMask(Direction.NORTH);
            }

            if (blockState.get(EAST_CONNECTION) != BranchType.NONE) {
                i |= getMask(Direction.EAST);
            }

            if (blockState.get(SOUTH_CONNECTION) != BranchType.NONE) {
                i |= getMask(Direction.SOUTH);
            }

            if (blockState.get(WEST_CONNECTION) != BranchType.NONE) {
                i |= getMask(Direction.WEST);
            }

            return i;
        });
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult rayTraceResult) {
        Direction facing = getHitSide(blockState, pos, rayTraceResult);
        if (playerIn.getHeldItem(handIn).getItem() == Registration.GRAFTING_TOOL.get()) {
            if (blockState.get(UP)) {
                if (facing == Direction.DOWN) {
                    return ActionResultType.PASS;
                } else if (!worldIn.isRemote()) {
                    switch (facing) {
                        case NORTH:
                            worldIn.setBlockState(pos, blockState.cycle(NORTH_CONNECTION));
                            break;
                        case SOUTH:
                            worldIn.setBlockState(pos, blockState.cycle(SOUTH_CONNECTION));
                            break;
                        case EAST:
                            worldIn.setBlockState(pos, blockState.cycle(EAST_CONNECTION));
                            break;
                        case WEST:
                            worldIn.setBlockState(pos, blockState.cycle(WEST_CONNECTION));
                            break;
                        case UP:
                            if (blockState.get(NORTH_CONNECTION) == BranchType.NONE && blockState.get(SOUTH_CONNECTION) == BranchType.NONE && blockState.get(EAST_CONNECTION) == BranchType.NONE && blockState.get(WEST_CONNECTION) == BranchType.NONE) {
                                return ActionResultType.PASS;
                            }
                            if (blockState.get(NORTH_CONNECTION) == BranchType.SIDE)
                                blockState = blockState.with(NORTH_CONNECTION, BranchType.UP);
                            if (blockState.get(EAST_CONNECTION) == BranchType.SIDE)
                                blockState = blockState.with(EAST_CONNECTION, BranchType.UP);
                            if (blockState.get(SOUTH_CONNECTION) == BranchType.SIDE)
                                blockState = blockState.with(SOUTH_CONNECTION, BranchType.UP);
                            if (blockState.get(WEST_CONNECTION) == BranchType.SIDE)
                                blockState = blockState.with(WEST_CONNECTION, BranchType.UP);
                            worldIn.setBlockState(pos, blockState.cycle(UP));

                            break;
                    }
                }
                return ActionResultType.SUCCESS;
            } else {
                if (facing == Direction.UP) {
                    if (!worldIn.isRemote()) {
                        worldIn.setBlockState(pos, blockState.cycle(UP));
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        if (worldIn.isRemote) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            return itemstack.getItem() == Items.LEAD ? ActionResultType.SUCCESS : ActionResultType.PASS;
        } else {
            return LeadItem.func_226641_a_(playerIn, worldIn, pos);
        }
    }


    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch (rot){
            case NONE:
                return state;
            case CLOCKWISE_90:
                return state.with(NORTH_CONNECTION, state.get(WEST_CONNECTION)).with(EAST_CONNECTION, state.get(NORTH_CONNECTION)).with(SOUTH_CONNECTION,state.get(SOUTH_CONNECTION)).with(WEST_CONNECTION, state.get(SOUTH_CONNECTION));
            case COUNTERCLOCKWISE_90:
                return state.with(NORTH_CONNECTION, state.get(EAST_CONNECTION)).with(EAST_CONNECTION, state.get(SOUTH_CONNECTION)).with(SOUTH_CONNECTION,state.get(WEST_CONNECTION)).with(WEST_CONNECTION, state.get(NORTH_CONNECTION));
            case CLOCKWISE_180:
                return state.with(NORTH_CONNECTION, state.get(SOUTH_CONNECTION)).with(EAST_CONNECTION, state.get(WEST_CONNECTION)).with(SOUTH_CONNECTION,state.get(NORTH_CONNECTION)).with(WEST_CONNECTION, state.get(EAST_CONNECTION));
        }
        return super.rotate(state, rot);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
//        builder.add(UP, NORTH_CONNECTION, EAST_CONNECTION, WEST_CONNECTION, SOUTH_CONNECTION, WATERLOGGED, LEAVES_SLAB_TYPE);
        builder.add(UP, NORTH_CONNECTION, EAST_CONNECTION, WEST_CONNECTION, SOUTH_CONNECTION, WATERLOGGED);
    }

    public Direction getHitSide(BlockState block, BlockPos pos, BlockRayTraceResult rayTraceResult) {
        Vec3d hit = rayTraceResult.getHitVec();
        double x = hit.x - pos.getX();
        double y = hit.y - pos.getY();
        double z = hit.z - pos.getZ();
        double f = 0.5D - (double) nodeWidth / 16;
        double f1 = 0.5D + (double) nodeWidth / 16;
        if (x > 0.0D && x < f) {
            return Direction.WEST;
        } else if (x < 1.0D && x > f1) {
            return Direction.EAST;
        } else if (z > 0.0D && z < f) {
            return Direction.NORTH;
        } else if (z < 1.0D && z > f1) {
            return Direction.SOUTH;
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
    @Override
    public TreeBlockTypes getTreeBlockType() {
        return TreeBlockTypes.TRUNK;
    }
}
