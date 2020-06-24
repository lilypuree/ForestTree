package lilypuree.forest_tree.trees.species;

import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModSpecies {

    private static List<Species> species;

    public static class DefaultSpecies extends SpeciesBase{
        @Override
        public int getID() {
            return 0;
        }

        @Override
        public String getName() {
            return "oak";
        }

        @Override
        public Block getLog() {
            return Blocks.OAK_LOG;
        }

        @Override
        public Block getLeaves() {
            return Blocks.OAK_LEAVES;
        }

        @Override
        public <T> T serialize(DynamicOps<T> p_218175_1_) {
            return null;
        }
    }

    public static class Pine extends SpeciesBase{
        @Override
        public int getID() {
            return 1;
        }

        @Override
        public String getName() {
            return "spruce";
        }

        @Override
        public Block getLog() {
            return Blocks.SPRUCE_LOG;
        }

        @Override
        public Block getLeaves() {
            return Blocks.SPRUCE_LEAVES;
        }

        @Override
        public boolean isConifer() {
            return true;
        }

        @Override
        public <T> T serialize(DynamicOps<T> p_218175_1_) {
            return null;
        }
    }

    public static Collection<Species> allSpecies(){
        species = new ArrayList<>();
        species.add(new DefaultSpecies());
        species.add(new Pine());
        return species;
    }
}
