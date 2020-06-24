package lilypuree.forest_tree.trees.block.trees;

import lilypuree.forest_tree.core.registry.ForestTreeFeatures;
import lilypuree.forest_tree.trees.world.biome.ForestTreeFeatureConfigs;
import lilypuree.forest_tree.trees.world.gen.feature.AdvancedTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Random;

public class PineTree extends AdvancedTree {

    @Nullable
    @Override
    protected ConfiguredFeature<AdvancedTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean canPlace, int age) {
        return ForestTreeFeatures.PINE.withConfiguration(ForestTreeFeatureConfigs.PINE_CONFIG);
    }
}
