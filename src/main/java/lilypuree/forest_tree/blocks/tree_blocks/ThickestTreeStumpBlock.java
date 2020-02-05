package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeStump;
import lilypuree.forest_tree.blocks.TreeTrunk;
import lilypuree.forest_tree.blocks.properties.BranchType;

public class ThickestTreeStumpBlock extends TreeStump {
        public ThickestTreeStumpBlock(Properties properties) {
        super(6.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(STUMP, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE));
    }

}
