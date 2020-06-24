package lilypuree.forest_tree.blocks_old.properties;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;

public class ForestTreeProperties {
    public static final EnumProperty<LeafSlabType> LEAVES_SLAB_TYPE = EnumProperty.create("leaves", LeafSlabType.class);
    public static final EnumProperty<BranchType> NORTH_CONNECTION = EnumProperty.create("north", BranchType.class);
    public static final EnumProperty<BranchType> SOUTH_CONNECTION = EnumProperty.create("south", BranchType.class);
    public static final EnumProperty<BranchType> EAST_CONNECTION = EnumProperty.create("east", BranchType.class);
    public static final EnumProperty<BranchType> WEST_CONNECTION = EnumProperty.create("west", BranchType.class);
    public static final EnumProperty<BranchType> CONNECTION = EnumProperty.create("connection", BranchType.class);
    public static final BooleanProperty RIGHT_BRANCH = BooleanProperty.create("right");
    public static final BooleanProperty LEFT_BRANCH = BooleanProperty.create("left");
    public static final BooleanProperty MAIN_BRANCH = BooleanProperty.create("main");
    public static final BooleanProperty STUMP = BooleanProperty.create("stump");

    public static EnumProperty<BranchType> getConnectionBasedOnDirection(Direction dir){
        switch (dir){
            case NORTH: return NORTH_CONNECTION;
            case WEST: return  WEST_CONNECTION;
            case SOUTH: return SOUTH_CONNECTION;
            case EAST: return EAST_CONNECTION;
        }
        return CONNECTION;
    }
}
