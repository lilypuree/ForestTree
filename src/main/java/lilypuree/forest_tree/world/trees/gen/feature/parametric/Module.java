package lilypuree.forest_tree.world.trees.gen.feature.parametric;

public enum Module {
    CREATION(0), GROWTH(1), TERMINATION(2), DIRECTIONS(3);

    public int index;

    Module(int i) {
        this.index = i;
    }

    public int getParameterCount() {
        return Parameter.parameters[index].length;
    }
}
