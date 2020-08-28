package lilypuree.forest_tree.api.genera;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.api.event.CategoryRegisterEvent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

public enum VanillaTypes implements WoodCategory, FoliageCategory {
    OAK {
        @Override
        public Block getDefaultFoliage() {
            return Blocks.OAK_LEAVES;
        }

        @Override
        public String getName() {
            return "oak";
        }

        @Override
        public ResourceLocation getFullBarkTexturePath() {
            return new ResourceLocation("minecraft:textures/block/oak_log.png");
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    }, BIRCH {
        @Override
        public Block getDefaultFoliage() {
            return Blocks.BIRCH_LEAVES;
        }

        @Override
        public String getName() {
            return "birch";
        }

        @Override
        public ResourceLocation getFullBarkTexturePath() {
            return new ResourceLocation("minecraft:textures/block/birch_log.png");
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    }, SPRUCE {
        @Override
        public Block getDefaultFoliage() {
            return Blocks.SPRUCE_LEAVES;
        }

        @Override
        public String getName() {
            return "spruce";
        }

        @Override
        public ResourceLocation getFullBarkTexturePath() {
            return new ResourceLocation("minecraft:textures/block/spruce_log.png");
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public int getFoliageColor() {
            return FoliageColors.getSpruce();
        }

        @Override
        public Optional<FoliageCategory> getEndFoliage() {
            return Optional.of(this);
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    }, ACACIA {
        @Override
        public Block getDefaultFoliage() {
            return Blocks.ACACIA_LEAVES;
        }

        @Override
        public String getName() {
            return "acacia";
        }

        @Override
        public ResourceLocation getFullBarkTexturePath() {
            return new ResourceLocation("minecraft:textures/block/acacia_log.png");
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    }, JUNGLE {
        @Override
        public Block getDefaultFoliage() {
            return Blocks.JUNGLE_LEAVES;
        }

        @Override
        public String getName() {
            return "jungle";
        }

        @Override
        public ResourceLocation getFullBarkTexturePath() {
            return new ResourceLocation("minecraft:textures/block/jungle_log.png");
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    }, DARK_OAK {
        @Override
        public Block getDefaultFoliage() {
            return Blocks.DARK_OAK_LEAVES;
        }

        @Override
        public String getName() {
            return "dark_oak";
        }

        @Override
        public ResourceLocation getFullBarkTexturePath() {
            return new ResourceLocation("minecraft:textures/block/dark_oak_log.png");
        }

        @Override
        public boolean isVanilla() {
            return true;
        }
    };
}
