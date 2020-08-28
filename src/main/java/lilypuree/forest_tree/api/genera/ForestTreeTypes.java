package lilypuree.forest_tree.api.genera;

import lilypuree.forest_tree.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

public enum ForestTreeTypes implements WoodCategory, FoliageCategory {
    PALM{
        @Override
        public Block getDefaultFoliage() {
            return Registration.PALM_CROWN.get();
        }

        @Override
        public String getName() {
            return "palm";
        }

        @Override
        public ResourceLocation getFullBarkTexturePath() {
            return new ResourceLocation("forest_tree:textures/block/palm_log.png");
        }

        @Override
        public boolean hasLeafBlock() {
            return false;
        }

        @Override
        public boolean isForestTreeLeaves() {
            return true;
        }
    }
}
