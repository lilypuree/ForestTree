package lilypuree.forest_tree.common.trees.block;

import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraftforge.client.model.data.ModelProperty;

public class ModBlockProperties {

    public static final ModelProperty<Float> THICKNESS_PROPERTY = new ModelProperty<>();
    public static final ModelProperty<Boolean> BRANCH_LEAVES = new ModelProperty<>();

    public static final int MAX_DIVISIONS = 12;
    public static final IntegerProperty THICKNESS = IntegerProperty.create("thickness", 1, MAX_DIVISIONS);
    public static final BooleanProperty STUMP = BooleanProperty.create("stump");

    public static float getThickness(BlockState state) {
        return (float) state.get(THICKNESS) / MAX_DIVISIONS;
    }
}
