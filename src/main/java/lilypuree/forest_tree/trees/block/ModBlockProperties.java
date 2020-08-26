package lilypuree.forest_tree.trees.block;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraftforge.client.model.data.ModelProperty;

public class ModBlockProperties {

    public static final ModelProperty<Float> THICKNESS = new ModelProperty<>();
    public static final ModelProperty<Boolean> BRANCH_LEAVES = new ModelProperty<>();

    public static final IntegerProperty TREE_AGE = IntegerProperty.create("tree_age", 0, 11);
    public static final BooleanProperty STUMP = BooleanProperty.create("stump");
}
