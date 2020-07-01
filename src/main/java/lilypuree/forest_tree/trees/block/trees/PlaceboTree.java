package lilypuree.forest_tree.trees.block.trees;

import lilypuree.forest_tree.core.registry.ForestTreeFeatures;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public class PlaceboTree extends AdvancedTree {
    @Nullable
    @Override
    protected ConfiguredFeature<NoFeatureConfig, ?> getTreeFeature(Random randomIn, boolean canPlace, int age) {
        return ForestTreeFeatures.PLACEBO.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG);
    }
}
