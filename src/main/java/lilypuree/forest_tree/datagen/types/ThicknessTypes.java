package lilypuree.forest_tree.datagen.types;

public enum  ThicknessTypes {
    THICKEST("thickest", 6.0F),THICK("thick", 4.0F),THIN("thin",2.0F);
    //when serializing, vanilla log : 3
    //for leaves : 0(leaves) 1(slab) 2(stair) 3(trapdoor)
    private final String name;
    private final float width;

    private ThicknessTypes(String name, float width) {
        this.name= name;
        this.width = width;
    }

    public String toString() {
        return this.getName();
    }

    public String getName() {
        return this.name;
    }

    public float getWidth(){
        return width;
    }

    public ThicknessTypes thinner(){
        switch (this){
            case THICKEST: return THICK;
            case THICK:
            case THIN:
                return THIN;
        }
        return THIN;
    }

    public static ThicknessTypes withName(String name){
        if (name.equalsIgnoreCase("thickest")) return THICKEST;
        else if(name.equalsIgnoreCase("thick")) return THICK;
        else if(name.equalsIgnoreCase("thin")) return THIN;
        return THICK;
    }
}
