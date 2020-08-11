package lilypuree.forest_tree.trees.world.gen.feature.biomeFeatures;

import lilypuree.forest_tree.core.registry.FeatureRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class ForestTreeTreeFeatures {
    public static void addTree(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeatureRegistry.TREE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }
}
