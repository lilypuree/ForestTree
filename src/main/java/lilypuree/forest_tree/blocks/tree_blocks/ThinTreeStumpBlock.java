package lilypuree.forest_tree.blocks.tree_blocks;

import lilypuree.forest_tree.blocks.TreeStump;

public class ThinTreeStumpBlock extends TreeStump {
        public ThinTreeStumpBlock(Properties properties) {
        super(2.0F, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(STUMP, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE));
    }

}
