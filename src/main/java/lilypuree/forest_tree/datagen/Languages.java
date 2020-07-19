package lilypuree.forest_tree.datagen;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.datagen.types.ThicknessTypes;
import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
import lilypuree.forest_tree.datagen.types.WoodTypes;
import lilypuree.forest_tree.Registration;
import lilypuree.forest_tree.trees.world.gen.feature.parametric.Module;
import lilypuree.forest_tree.trees.world.gen.feature.parametric.Parameter;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

public class Languages extends LanguageProvider {

    public Languages(DataGenerator gen, String locale) {
        super(gen, ForestTree.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(Registration.TREE_DESIGNER_BLOCK.get(), "Tree Designer");
        add(Registration.CUSTOM_SAPLING.get(), "Custom Sapling");
        add(Registration.GRAFTING_TOOL.get(), "Grafting Tool");

        for (Module module : Module.values()){
            for (Parameter parameter : Parameter.parameters[module.index]){
                add("forest_tree.treedesignergui.parameters."+parameter.name, parameter.name);
                add("forest_tree.treedesignergui.descriptions."+parameter.name, "description here");
            }
            add("forest_tree.treedesignergui.modules."+module.name(), module.name());
        }
    }
}
