package lilypuree.forest_tree.trees.customization;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TreeDesignerBlock extends Block {

    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent(ForestTree.MODID + "container.treeDesigning");
    private static final VoxelShape BLOCK_SHAPE = VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);

    public TreeDesignerBlock(Block.Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote()) return ActionResultType.SUCCESS;
        else {
            final TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TreeDesignerTile) {
                if (player.isSneaking() && player.getHeldItemMainhand().isEmpty()) {
                    ((TreeDesignerTile) tileEntity).extractSapling();
                    worldIn.notifyBlockUpdate(pos, state, state, 3);
                } else if (player.getHeldItemMainhand().getItem() == Registration.CUSTOM_SAPLING_ITEM.get()) {

                } else {
                    NetworkHooks.openGui((ServerPlayerEntity) player, (TreeDesignerTile) tileEntity, pos);
                    return ActionResultType.SUCCESS;
                }
            }
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
    }


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BLOCK_SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TreeDesignerTile();
    }
}
