package lilypuree.forest_tree.trees.customization;

import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.block.trees.CustomTree;
import lilypuree.forest_tree.trees.customization.CustomSaplingTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Random;

public class CustomSaplingBlock extends BushBlock implements IGrowable {
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE_0_1;
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

    public CustomSaplingBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(STAGE, 0));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Registration.CUSTOM_SAPLING_TILE.get().create();
    }

    //copied over from shulker boxes
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof CustomSaplingTile) {
            CustomSaplingTile customSaplingTile = (CustomSaplingTile) tileEntity;
            if (!worldIn.isRemote()) {
                ItemStack itemstack = new ItemStack(this);
                CompoundNBT compoundNBT = customSaplingTile.saveToNbt(new CompoundNBT());
                if (!compoundNBT.isEmpty()) {
                    itemstack.setTagInfo("BlockEntityTag", compoundNBT);
                }
                if (customSaplingTile.hasCustomName()) {
                    itemstack.setDisplayName(customSaplingTile.getCustomName());
                }
                InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemstack);
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof CustomSaplingTile) {
            if (stack.hasDisplayName()) {
                ((CustomSaplingTile) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        ItemStack stack = super.getItem(worldIn, pos, state);
        CustomSaplingTile customSaplingTile = (CustomSaplingTile) worldIn.getTileEntity(pos);
        if (customSaplingTile == null) return stack;
        CompoundNBT compoundnbt = customSaplingTile.saveToNbt(new CompoundNBT());
        if (!compoundnbt.isEmpty()) {
            stack.setTagInfo("BlockEntityTag", compoundnbt);
        }
        return stack;
    }
    //shulker box code end

    //1.16 code... will Random tick work?
    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (worldIn.getLight(pos.up()) >= 9 && random.nextInt(7) == 0) {
            if (!worldIn.isAreaLoaded(pos, 1))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            this.placeTree(worldIn, pos, state, random);
        }
    }

    public void placeTree(ServerWorld world, BlockPos pos, BlockState state, Random rand) {
//        if (state.get(STAGE) == 0) {
//            world.setBlockState(pos, state.cycle(STAGE), 4);
//        } else {
        if (!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(world, rand, pos)) return;
        CustomSaplingTile customSaplingTile = (CustomSaplingTile) world.getTileEntity(pos);
        if (customSaplingTile != null) {
            CustomTree customTree = new CustomTree(customSaplingTile.getTreeGenerator());

            //TODO
            //set age here?
            customTree.place(world, world.getChunkProvider().getChunkGenerator(), pos, state, rand);
        }
//        }
    }


    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return (double) worldIn.rand.nextFloat() < 0.45D;
    }

    @Override
    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        this.placeTree(worldIn, pos, state, rand);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}
