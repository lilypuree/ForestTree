package lilypuree.forest_tree.trees.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class PalmCrownModelLoader implements IModelLoader<PalmCrownGeometry> {

    public static PalmCrownModelLoader INSTANCE = new PalmCrownModelLoader();

    private PalmCrownModelLoader(){

    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public PalmCrownGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new PalmCrownGeometry();
    }
}
