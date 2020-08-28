package lilypuree.forest_tree.trees;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.api.gen.ITreeModel;
import lilypuree.forest_tree.api.genera.ForestTreeTypes;
import lilypuree.forest_tree.api.genera.Species;
import lilypuree.forest_tree.api.genera.TreeGenus;
import lilypuree.forest_tree.trees.treemodels.PalmTreeModel;
import net.minecraft.util.ResourceLocation;

public class PalmTree extends TreeGenus {


    public class PalmSpecies extends Species {
        PalmSpecies(TreeGenus genus) {
            super(genus.getName(), genus);
            setTreeModel(new PalmTreeModel(this));
        }


    }

    public PalmTree() {
        super(new ResourceLocation(ForestTree.MODID, "palm"), ForestTreeTypes.PALM, ForestTreeTypes.PALM);
    }


    @Override
    public void createSpecies() {
        setCommonSpecies(new PalmSpecies(this));
    }
}
