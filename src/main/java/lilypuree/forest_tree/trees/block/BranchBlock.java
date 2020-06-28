package lilypuree.forest_tree.trees.block;

import lilypuree.forest_tree.trees.species.Species;
import lilypuree.forest_tree.util.Util;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BranchBlock extends Block {

    public static final IntegerProperty AGE = ModBlockProperties.TREE_AGE;

    private boolean end = false;
    private Vec3i sourceOffset;
    private Species species;

    public BranchBlock(Properties properties, Species speciesIn) {
        super(properties);
        this.species = speciesIn;
        this.setDefaultState(this.getStateContainer().getBaseState().with(AGE, 1));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
        Entity looker = context.getEntity();
        if (looker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) looker;
            Vec3d start = player.getEyePosition(0);
            Vec3d look = player.getLook(0);
            Vec3d end = start.add(look.scale(6));
            return getClosestShape(start, end, pos, blockReader);
        }
        return BranchVoxelShapes.getVoxelShapeForDirection(sourceOffset);
    }

    private VoxelShape getClosestShape(Vec3d start, Vec3d end, BlockPos pos, IBlockReader blockReader) {

        VoxelShape temp = VoxelShapes.empty();
        double tempDistance = Double.MAX_VALUE;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    VoxelShape shape;
                    if (i == 0 && j == 0 && k == 0) {
                        shape = BranchVoxelShapes.getVoxelShapeForDirection(sourceOffset);
                    } else {
                        BlockPos otherPos = pos.add(i, j, k);
                        Block otherBlock = blockReader.getBlockState(otherPos).getBlock();
                        if (otherBlock instanceof BranchBlock && Util.compareVec3iToInts(((BranchBlock) otherBlock).getSourceOffset(), -i, -j, -k)) {
                            shape = BranchVoxelShapes.getVoxelShapeForDirection(i, j, k);
                        }else{
                            continue;
                        }
                    }
                    BlockRayTraceResult result = shape.rayTrace(start, end, pos);
                    double distance = result == null ? Double.MAX_VALUE : start.squareDistanceTo(result.getHitVec());
                    if (distance < tempDistance) {
                        temp = shape;
                        tempDistance = distance;
                    }
                }
            }
        }

        return temp;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return BranchVoxelShapes.getHalfShapeForDirection(sourceOffset);
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

    public Species getSpecies() {
        return species;
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
            return ((BranchBlock) block).getSpecies().canLoseLeaves();
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
            //do a bfs search
        }
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }
}
