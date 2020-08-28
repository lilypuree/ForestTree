package lilypuree.forest_tree.client.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class MultipleFlowerModelLoader implements IModelLoader<MultipleFlowerModelGeometry> {
    public static MultipleFlowerModelLoader INSTANCE = new MultipleFlowerModelLoader();

    private MultipleFlowerModelLoader(){}

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public MultipleFlowerModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new MultipleFlowerModelGeometry();
    }
}
