package lilypuree.forest_tree.trees.species;

import net.minecraft.block.Block;
import net.minecraft.util.IDynamicSerializable;
import net.minecraft.util.math.Vec3i;

public interface Species extends IDynamicSerializable {

    public abstract float getThickness(int distanceToTrunk, int age, boolean isEnd, Vec3i sourcePos);

    public abstract boolean isConifer();

    public abstract boolean canLoseLeaves();

    public abstract boolean fruits();

    public abstract int getID();

    public abstract String getName();

    Block getLog();

    Block getLeaves();
}
