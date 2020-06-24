package lilypuree.forest_tree.datagen.types;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum WoodTypes {
    OAK("oak"), BIRCH("birch"), SPRUCE("spruce"), ACACIA("acacia"), JUNGLE("jungle"), DARK_OAK("dark_oak");

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

    public static WoodTypes withName(String name) {
        if (name.equalsIgnoreCase("oak")) return OAK;
        else if (name.equalsIgnoreCase("birch")) return BIRCH;
        else if (name.equalsIgnoreCase("spruce")) return SPRUCE;
        else if (name.equalsIgnoreCase("acacia")) return ACACIA;
        else if (name.equalsIgnoreCase("jungle")) return JUNGLE;
        else if (name.equalsIgnoreCase("dark")) return DARK_OAK;
        return OAK;
    }

    public static WoodTypes fromPath(String path) {
        String[] paths = path.split("_");
        return withName(paths[0]);
    }

    public Block getLog() {
        switch (this) {
            case OAK:
                return Blocks.OAK_LOG;
            case SPRUCE:
                return Blocks.SPRUCE_LOG;
            case BIRCH:
                return Blocks.BIRCH_LOG;
            case JUNGLE:
                return Blocks.JUNGLE_LOG;
            case DARK_OAK:
                return Blocks.DARK_OAK_LOG;
            case ACACIA:
                return Blocks.ACACIA_LOG;
        }
        return Blocks.OAK_LOG;
    }

    public Block getWood() {
        switch (this) {
            case OAK:
                return Blocks.OAK_WOOD;
            case SPRUCE:
                return Blocks.SPRUCE_WOOD;
            case BIRCH:
                return Blocks.BIRCH_WOOD;
            case JUNGLE:
                return Blocks.JUNGLE_WOOD;
            case DARK_OAK:
                return Blocks.DARK_OAK_WOOD;
            case ACACIA:
                return Blocks.ACACIA_WOOD;
        }
        return Blocks.OAK_WOOD;
    }

    public Block getLeaves() {
        switch (this) {
            case OAK:
                return Blocks.OAK_LEAVES;
            case BIRCH:
                return Blocks.BIRCH_LEAVES;
            case SPRUCE:
                return Blocks.SPRUCE_LEAVES;
            case JUNGLE:
                return Blocks.JUNGLE_LEAVES;
            case DARK_OAK:
                return Blocks.DARK_OAK_LEAVES;
            case ACACIA:
                return Blocks.ACACIA_LEAVES;
        }
        return Blocks.OAK_LEAVES;
    }

    public Item getSapling() {
        switch (this) {
            case OAK:
                return Items.OAK_SAPLING;
            case BIRCH:
                return Items.BIRCH_SAPLING;
            case SPRUCE:
                return Items.SPRUCE_SAPLING;
            case JUNGLE:
                return Items.JUNGLE_SAPLING;
            case DARK_OAK:
                return Items.DARK_OAK_SAPLING;
            case ACACIA:
                return Items.ACACIA_SAPLING;

        }
        return Items.OAK_SAPLING;
    }
}
