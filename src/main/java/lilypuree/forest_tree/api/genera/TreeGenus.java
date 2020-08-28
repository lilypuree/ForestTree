package lilypuree.forest_tree.api.genera;

import lilypuree.forest_tree.ForestTree;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class TreeGenus {

    public static final TreeGenus NULLGENUS = new TreeGenus() {

    };

    /**
     * Simple name of the tree e.g. "oak"
     */
    private final ResourceLocation name;

    protected Species commonSpecies = Species.NULLSPECIES;

    private WoodCategory wood;

    private FoliageCategory foliage;

    public TreeGenus() {
        this.name = new ResourceLocation(ForestTree.MODID, "null");
    }

    public TreeGenus(ResourceLocation name) {
        this.name = name;
        createSpecies();
    }

    public TreeGenus(ResourceLocation name, WoodCategory wood, FoliageCategory foliage) {
        this.name = name;
        this.wood = wood;
        this.foliage = foliage;
        createSpecies();
    }

    public void createSpecies() {
    }

    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        speciesRegistry.register(getCommonSpecies());
    }

    public void setCommonSpecies(@Nonnull Species species) {
        commonSpecies = species;
    }

    public Species getCommonSpecies() {
        return commonSpecies;
    }

    public WoodCategory getWoodCategory() {
        return wood;
    }

    public FoliageCategory getFoliageCategory() {
        return foliage;
    }


    ///////////////////////////////////////////
    // TREE PROPERTIES
    ///////////////////////////////////////////

    public ResourceLocation getName() {
        return name;
    }

    public boolean isWood() {
        return true;
    }


}
