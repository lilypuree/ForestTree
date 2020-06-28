package lilypuree.forest_tree.trees.world.biome;



import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.species.ModSpecies;
import lilypuree.forest_tree.trees.world.gen_old.feature.AdvancedTreeFeatureConfig;

public class ForestTreeFeatureConfigs {

    public static final AdvancedTreeFeatureConfig OAK_TREE_CONFIG = (new AdvancedTreeFeatureConfig.Builder(new ModSpecies.DefaultSpecies())
    .height(4).heightRandom(3).age(0).branchYPosRandom(2).extraBranchLength(3)).setSapling(Registration.OAK_SAPLING.get()).build();

    public static final AdvancedTreeFeatureConfig PINE_CONFIG = (new AdvancedTreeFeatureConfig.Builder(new ModSpecies.Pine())
            .height(6).heightRandom(7).age(0).extraBranchLength(2)).setSapling(Registration.PINE_SAPLING.get()).build();


}
