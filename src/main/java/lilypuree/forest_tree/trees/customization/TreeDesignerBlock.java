package lilypuree.forest_tree.trees.customization;

import lilypuree.forest_tree.ForestTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TreeDesignerBlock extends Block {

    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent(ForestTree.MODID + "container.treeDesigning");

    public TreeDesignerBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            final TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TreeDesignerTile) {
                ForestTree.LOGGER.info("opening gui");
                NetworkHooks.openGui((ServerPlayerEntity) player, (TreeDesignerTile) tileEntity, pos);
            }
            return ActionResultType.SUCCESS;
        }
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
