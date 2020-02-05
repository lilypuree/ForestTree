package lilypuree.forest_tree.blocks;


import lilypuree.forest_tree.blocks.properties.ForestTreeProperties;
import lilypuree.forest_tree.datagen.types.ThicknessTypes;
import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
import lilypuree.forest_tree.datagen.types.WoodTypes;
import lilypuree.forest_tree.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TreeStump extends Block implements IWaterLoggable, ITreeBlock {
    public static final BooleanProperty STUMP = ForestTreeProperties.STUMP;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static Random random = new Random();
    private final VoxelShape TOP_SHAPE;
    private final VoxelShape BOTTOM_SHAPE;
    private final VoxelShape FULL_SHAPE;
    protected final float nodeWidth;

    public TreeStump(float nodeWidth, Block.Properties properties) {
        super(properties);
        this.nodeWidth = nodeWidth;
        double f = 8.0D - nodeWidth;
        double f1 = 8.0D + nodeWidth;
        TOP_SHAPE = Block.makeCuboidShape(f, nodeWidth * 1.5D, f, f1, 16.0D, f1);
        BOTTOM_SHAPE = Block.makeCuboidShape(f, 0.0D, f, f1, nodeWidth * 1.5D, f1);
        FULL_SHAPE = VoxelShapes.or(TOP_SHAPE, BOTTOM_SHAPE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(STUMP)) {
            return BOTTOM_SHAPE;
        } else {
            return FULL_SHAPE;
        }
    }

    public float getNodeWidth() {
        return nodeWidth;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Registration.TREE_TILE.get().create();
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

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TreeTile){
            String[] s = state.getBlock().getRegistryName().getPath().split("_");
            ((TreeTile) tileEntity).setThickness(ThicknessTypes.withName(s[1]));
            ((TreeTile) tileEntity).setWood(WoodTypes.withName(s[0]));
            ((TreeTile) tileEntity).init();
        }
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STUMP, WATERLOGGED);
    }


    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote() && worldIn.getTileEntity(pos) instanceof TreeTile) {
            ((TreeTile) worldIn.getTileEntity(pos)).chopDownTree();
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }


    @Override
    public boolean onBlockActivated(BlockState block, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult rayTraceResult) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (tileEntity instanceof TreeTile) {
            if (heldItem.getToolTypes().contains(ToolType.AXE) && !block.get(STUMP)) {
                if (((TreeTile) tileEntity).attemptChopDown()) {
                    worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.2F, 1.0F);
                    if (!worldIn.isRemote) worldIn.setBlockState(pos, block.cycle(STUMP));
                } else worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
            if (heldItem.getToolTypes().contains(ToolType.SHOVEL) && !block.get(STUMP)){
                if(playerIn.isCreative() && !worldIn.isRemote){
                    playerIn.addItemStackToInventory(((TreeTile) tileEntity).getTreeItem());
                }else {
                    if(((TreeTile) tileEntity).attemptUproot(playerIn.getHorizontalFacing())){
                        worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.2F, 1.0F);
                    }else {
                        worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_GRASS_HIT, SoundCategory.BLOCKS, 1.2F, 1.0F);
                    }
                }
            }
        }

        if (!worldIn.isRemote && tileEntity instanceof TreeTile) {
            if (playerIn.getHeldItem(handIn).getItem() == Items.CARROT && !block.get(STUMP)) {
                ((TreeTile) worldIn.getTileEntity(pos)).registerTree();

                return true;
            }


            if (playerIn.getHeldItem(handIn).getItem() == Items.ALLIUM) {

                System.out.println(((TreeTile) worldIn.getTileEntity(pos)).write(new CompoundNBT()).toString());

                return true;
            }
        }

        return super.onBlockActivated(block, worldIn, pos, playerIn, handIn, rayTraceResult);
    }


    @Override
    public boolean isToolEffective(BlockState state, ToolType tool) {
        if (state.get(STUMP)) {
            if (tool == ToolType.SHOVEL) return true;
        } else {
            if (tool == ToolType.AXE) return true;
        }
        return false;
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 9;
    }

    @Override
    public TreeBlockTypes getTreeBlockType() {
        return TreeBlockTypes.STUMP;
    }
}
