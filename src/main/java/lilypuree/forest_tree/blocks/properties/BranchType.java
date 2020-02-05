package lilypuree.forest_tree.blocks.properties;

import net.minecraft.util.IStringSerializable;

public enum BranchType implements IStringSerializable {
    NONE("none"),
    SIDE("side"),
    UP("up");

    private final String name;

    private BranchType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getName();
    }

    public String getName() {
        return this.name;
    }
}
