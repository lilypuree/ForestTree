package lilypuree.forest_tree.common.world.feature.biomefeatures;

import lilypuree.forest_tree.core.registry.FeatureRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class ForestTreeFeatures {
    public static void addDenseFieldsFlowers(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeatureRegistry.DENSE_FLOWER_PATCH.withConfiguration(ForestTreeFeatureConfigs.DENSE_FLOWER_FIELD_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(
                new FrequencyConfig(200))));
    }
}
