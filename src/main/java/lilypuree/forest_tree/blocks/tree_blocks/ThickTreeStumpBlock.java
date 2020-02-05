package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeStump;
import lilypuree.forest_tree.blocks.TreeTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ThickTreeStumpBlock extends TreeStump {
        public ThickTreeStumpBlock(Properties properties) {
        super(4.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(STUMP, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE));
    }

}
