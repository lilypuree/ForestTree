package lilypuree.forest_tree.common.trees.block;

import lilypuree.forest_tree.api.genera.WoodCategory;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BranchBlock extends Block {

    public static final IntegerProperty THICKNESS = ModBlockProperties.THICKNESS;
    public Map<Integer, VoxelShape> voxelShapes = new HashMap<>();
    public Map<Integer, VoxelShape> halfVoxelShapes = new HashMap<>();

    private boolean end = false;
    private Vec3i sourceOffset;
    private WoodCategory woodType;

    public BranchBlock(Properties properties, WoodCategory woodTypeIn) {
        super(properties);
        this.woodType = woodTypeIn;
        this.setDefaultState(this.getStateContainer().getBaseState().with(THICKNESS, 5));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(THICKNESS);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
        Entity looker = context.getEntity();
        if (looker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) looker;
            Vec3d start = player.getEyePosition(0);
            Vec3d look = player.getLook(0);
            Vec3d end = start.add(look.scale(6));
            return BranchVoxelShapes.getClosestShape(start, end, pos, state, blockReader);
        }
        return voxelShapes.computeIfAbsent(state.get(THICKNESS), division -> BranchVoxelShapes.getVoxelShapeForBranch(this, division));
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
//        return BranchVoxelShapes.getHalfShapeForDirection(sourceOffset);
        return halfVoxelShapes.computeIfAbsent(state.get(THICKNESS), division -> BranchVoxelShapes.getHalfVoxelShapeForBranch(this, division));
    }


    public BranchBlock setEnd(boolean isEnd) {
        this.end = isEnd;
        return this;
    }

    public boolean isEnd() {
        return end;
    }

    public BranchBlock setSourceOffset(int x, int y, int z) {
        return setSourceOffset(new Vec3i(x, y, z));
    }

    public BranchBlock setSourceOffset(Vec3i offset) {
        this.sourceOffset = offset;
        return this;
    }

    public Vec3i getSourceOffset() {
        return sourceOffset;
    }

    public WoodCategory getWoodCategory() {
        return woodType;
    }


    public BlockState getSourceBlock(IBlockReader world, BlockPos pos, int depth) {
        if (depth > 1) {
            Block b = world.getBlockState(pos.add(sourceOffset)).getBlock();
            if (b instanceof BranchBlock) {
                return ((BranchBlock) b).getSourceBlock(world, pos.add(sourceOffset), depth - 1);
            }
        }
        return world.getBlockState(pos.add(sourceOffset));
    }

    public int getDistanceToTrunk(IBlockReader world, BlockPos pos, int total) {
        Block block = getSourceBlock(world, pos, 1).getBlock();
        if (total > 100) {
            if (world instanceof World && !((World) world).isRemote) {
                ((World) world).setBlockState(pos, Blocks.AIR.getDefaultState());
            }
            return -1000;
        }
        if (block instanceof BranchBlock) {
            Vec3i blockOffset = ((BranchBlock) block).getSourceOffset();
            int totalX = sourceOffset.getX() + blockOffset.getX();
            int totalY = sourceOffset.getY() + blockOffset.getY();
            int totalZ = sourceOffset.getZ() + blockOffset.getZ();
            if (totalX == 0 && totalY == 0 && totalZ == 0) {
                return -1000;
            }
            if (block != this && !isEnd()) {
                total += 6;
            } else {
                total += 1;
            }
            return ((BranchBlock) block).getDistanceToTrunk(world, pos.add(sourceOffset), total);
        }
//        ((b instanceof BlockLogNatural) || TFC_Core.isSoil(b) || TFC_Core.isSand(b))
        boolean isConnectedToLandOrLog = true;
        if (isConnectedToLandOrLog) return 1 + total;
        if (world instanceof World && !((World) world).isRemote()) {
            ((World) world).setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        return -1000;
    }

    private void dropLeaves(World world, BlockPos pos, int height) {
        if (world.isRemote) return;

        BlockState blockState;
        BlockState blockUp;
        for (int i = 0; i > -(height + 7) && i + pos.getY() > 0; i--) {
            BlockPos blockPos = pos.add(0, i, 0);
            blockState = world.getBlockState(blockPos);
            blockUp = world.getBlockState(pos.add(0, i + 1, 0));
            Block block = blockState.getBlock();
//            if(TFC_Core.isGrass(block)){
//             -set block above to leaf litter, block under to dirt
            if (blockState.isSolidSide(world, blockPos, Direction.UP) && !(block instanceof BranchBlock)) { //&&blockUp isn't leaves&& block isn't leaves)
                //&& block isn't invisible && isn't ice
                if (true) {//if block up is replacable
                    //set leaf litter
                }
            }
        }
    }

    public static boolean canLoseLeaves(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof BranchBlock) {
            if (((BranchBlock) block).isEnd() && false) { //&&isBananaWood()
                return false;
            }
//            return ((BranchBlock) block).getWoodType().canLoseLeaves();
        }
        return true;
    }

    protected boolean validLeafLocation(World world, BlockPos pos, boolean willow) {
        if (world.isRemote) return false;

        for (Direction dir : Direction.values()) {
            Block block = world.getBlockState(pos.offset(dir)).getBlock();
            boolean valid = (block instanceof LeavesBlock || (block instanceof BranchBlock) && ((BranchBlock) block).isEnd());
            if (valid) return true;
        }
        return false;
    }

    //updateTick? - handles removing and ticking for leaves

    //shouldLoseLeaf : has a season parameter
    //updateBranchTime : update branches to grow/lose leaves based on chunk loads
    //shouldDefinitelyloseleaf : when temp <0
    //shouldDefinitelyLoseLeaf : gets season, temp from TFC

    @Override
    public int tickRate(IWorldReader worldIn) {
        return 50;
    }


    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
//        super.harvestBlock();
        if (worldIn.isRemote) return;
        boolean hasRightTool = true;

        if (hasRightTool) {
            recursiveDestroyTree(worldIn, pos, !player.isCreative());
        }
    }

    public void recursiveDestroyTree(World worldIn, BlockPos pos, boolean generateDrop) {
        worldIn.destroyBlock(pos, generateDrop);
        BlockPos.getAllInBox(new BlockPos(pos).add(-1, -1, -1), new BlockPos(pos).add(1, 1, 1))
                .forEach(bp -> {
                    Block block = worldIn.getBlockState(bp).getBlock();
                    if (block instanceof BranchBlock) {
                        Vec3i soffset = ((BranchBlock) block).getSourceOffset();
                        if (bp.add(soffset).equals(pos)) {

                            ((BranchBlock) block).recursiveDestroyTree(worldIn, bp, generateDrop);
                        }
                    }
                });
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean allowsMovement(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }
}
