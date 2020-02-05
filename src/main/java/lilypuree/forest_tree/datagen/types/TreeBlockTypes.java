package lilypuree.forest_tree.datagen.types;

public enum  TreeBlockTypes {
    TRUNK("trunk"), BRANCH("branch"), STUMP("stump");
    //When seriallizing : Vanilla log : 3, Leaves : 4

    private final String name;

    private TreeBlockTypes(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getName();
    }

    public String getName() {
        return this.name;
    }

    public static TreeBlockTypes withName(String name){
        if(name.equalsIgnoreCase("trunk")) return TRUNK;
        else if(name.equalsIgnoreCase("branch")) return BRANCH;
        else if(name.equalsIgnoreCase("stump")) return STUMP;
        return TRUNK;
    }
}
