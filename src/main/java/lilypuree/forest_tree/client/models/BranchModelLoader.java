package lilypuree.forest_tree.client.models;

import com.google.gson.*;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.IModelLoader;

public class BranchModelLoader implements IModelLoader<BranchModelGeometry> {

    public static BranchModelLoader INSTANCE = new BranchModelLoader();

    private BranchModelLoader() {

    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
    }

    @Override
    public BranchModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        Vec3i source = this.parseVector(modelContents, "source", new Vec3i(0, 0, 0));
        return new BranchModelGeometry(source);
    }

    private Vec3i parseVector(JsonObject json, String key, Vec3i fallback) {
        if (!json.has(key)) {
            return fallback;
        } else {
            JsonArray jsonarray = JSONUtils.getJsonArray(json, key);
            if (jsonarray.size() != 3) {
                throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
            } else {
                int[] aint = new int[3];

                for (int i = 0; i < aint.length; ++i) {
                    aint[i] = JSONUtils.getInt(jsonarray.get(i), key + "[" + i + "]");
                }

                return new Vec3i(aint[0], aint[1], aint[2]);
            }
        }
    }
}
