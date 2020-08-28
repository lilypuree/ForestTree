package lilypuree.forest_tree.common.shrubs.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MultipleFlowerBlock extends BushBlock {

    public MultipleFlowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        if (heldItem.getItem() instanceof BlockItem) {
            if (!worldIn.isRemote) {
                BlockState flowerBlock = ((BlockItem) heldItem.getItem()).getBlock().getDefaultState();
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if (tileEntity instanceof MultipleFlowerTile) {
                    if (((MultipleFlowerTile) tileEntity).insertFlower(flowerBlock) && !player.isCreative()) {
                        heldItem.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            } else {
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MultipleFlowerTile();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
