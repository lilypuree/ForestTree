package lilypuree.forest_tree.datagen.types;

public enum WoodTypes {
    OAK("oak"),BIRCH("birch"),SPRUCE("spruce"),ACACIA("acacia"),JUNGLE("jungle"),DARK_OAK("dark_oak");

    private final String name;

    private WoodTypes(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getName();
    }

    public String getName() {
        return this.name;
    }

    public static WoodTypes withName(String name){
        if(name.equalsIgnoreCase("oak")) return OAK;
        else if(name.equalsIgnoreCase("birch")) return BIRCH;
        else if(name.equalsIgnoreCase("spruce")) return SPRUCE;
        else if(name.equalsIgnoreCase("acacia")) return ACACIA;
        else if(name.equalsIgnoreCase("jungle")) return JUNGLE;
        else if(name.equalsIgnoreCase("dark")) return DARK_OAK;
        return OAK;
    }
}
