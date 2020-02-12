package lilypuree.forest_tree.datagen;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.datagen.types.ThicknessTypes;
import lilypuree.forest_tree.datagen.types.TreeBlockTypes;
import lilypuree.forest_tree.datagen.types.WoodTypes;
import lilypuree.forest_tree.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

public class Languages extends LanguageProvider {

    public Languages(DataGenerator gen, String locale) {
        super(gen, ForestTree.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        for(WoodTypes wood : WoodTypes.values()){
            for (ThicknessTypes thickness : ThicknessTypes.values()){
                for (TreeBlockTypes type : TreeBlockTypes.values()){
                    add(Registration.getTreeBlockItem(wood, thickness, type), StringUtils.capitalize(thickness.getName())+" " + StringUtils.capitalize(wood.getName()) +" "+ StringUtils.capitalize(type.getName()));
                }
                add(Registration.getTreeItem(wood,thickness), StringUtils.capitalize(thickness.getName())+" "+StringUtils.capitalize(wood.getName())+" Tree");
            }
            add(Registration.getLeafBlockItem(wood, "slab"), StringUtils.capitalize(wood.getName())+" Leaves Slab");
            add(Registration.getLeafBlockItem(wood, "stairs"), StringUtils.capitalize(wood.getName())+" Leaves Stairs");
            add(Registration.getLeafBlockItem(wood, "trapdoor"), StringUtils.capitalize(wood.getName())+" Leaves Trapdoor");
            add(Registration.getTimber(wood), StringUtils.capitalize(wood.getName())+" Timber");
        }
        add(Registration.GRAFTING_TOOL.get(), "Grafting Tool");


    }
}
