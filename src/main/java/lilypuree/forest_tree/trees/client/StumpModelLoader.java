package lilypuree.forest_tree.trees.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class StumpModelLoader implements IModelLoader<StumpModelGeometry> {
    public static StumpModelLoader INSTANCE = new StumpModelLoader();

    private StumpModelLoader() {
    }


    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public StumpModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new StumpModelGeometry();
    }
}
