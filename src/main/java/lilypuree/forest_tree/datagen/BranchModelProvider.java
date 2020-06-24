package lilypuree.forest_tree.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelProvider;

public abstract class BranchModelProvider extends ModelProvider<BranchModelBuilder> {

    public BranchModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, BLOCK_FOLDER, BranchModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void generateAll(DirectoryCache cache) {
        super.generateAll(cache);
    }
}
