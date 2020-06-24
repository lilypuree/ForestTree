package lilypuree.forest_tree.blocks_old.properties;

import net.minecraft.util.IStringSerializable;

public enum  LeafSlabType implements IStringSerializable {
    NONE("none"),
    TOP("top"),
    BOTTOM("bottom"),
    DOUBLE("double");

    private final String name;

    private LeafSlabType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
}
