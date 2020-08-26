package lilypuree.forest_tree.trees.species;

import com.mojang.datafixers.types.DynamicOps;
import lilypuree.forest_tree.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;

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

        @Override
        public ResourceLocation getFullTexturePath() {
            return new ResourceLocation("minecraft:textures/block/oak_log.png");
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

        @Override
        public ResourceLocation getFullTexturePath() {
            return new ResourceLocation("minecraft:textures/block/spruce_log.png");
        }
    }

    public static class Palm extends SpeciesBase{
        @Override
        public int getID() {
            return 2;
        }

        @Override
        public String getName() {
            return "palm";
        }

        @Override
        public Block getLog() {
            return Blocks.AIR;
//            return Registration.PALM_LOG.get();
        }

        @Override
        public Block getLeaves() {
            return Registration.PALM_CROWN.get();
        }

        @Override
        public boolean isConifer() {
            return false;
        }

        @Override
        public <T> T serialize(DynamicOps<T> p_218175_1_) {
            return null;
        }

        @Override
        public ResourceLocation getFullTexturePath() {
            return new ResourceLocation("forest_tree:textures/block/palm_log.png");
        }
    }

    public static Species OAK;
    public static Species PINE;
    public static Species PALM;

    static {
        species = new ArrayList<>();
        OAK = new DefaultSpecies();
        PINE = new Pine();
        PALM = new Palm();
        species.add(OAK);
        species.add(PINE);
        species.add(PALM);
    }

    public static Collection<Species> allSpecies(){
        return species;
    }
}
