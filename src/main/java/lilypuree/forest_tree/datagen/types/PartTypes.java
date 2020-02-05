package lilypuree.forest_tree.datagen.types;

import java.util.function.Function;

public enum PartTypes {
    UP("up"), SIDE("side"), CENTER("center"), STUMP("stump"), ROOT("root");

    private final String name;
    private PartTypes(String name) {
        this.name = name;

    }

    public String toString() {
        return this.getName();
    }

    public String getName() {
        return this.name;
    }
}
