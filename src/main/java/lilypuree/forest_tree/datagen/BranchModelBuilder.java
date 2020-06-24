package lilypuree.forest_tree.datagen;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lilypuree.forest_tree.ForestTree;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BlockPartFace;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelBuilder;

import java.util.Arrays;
import java.util.Map;

public class BranchModelBuilder extends ModelBuilder<BranchModelBuilder> {

    private Vec3i sourceOffset = null;

    public BranchModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation, existingFileHelper);
    }

    public BranchModelBuilder source(Vec3i sourceOffsetIn){
        this.sourceOffset = sourceOffsetIn;
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        if (this.parent != null) {
            root.addProperty("parent", serializeLoc(this.parent.getLocation()));
        }
        root.addProperty("loader", serializeLoc(new ResourceLocation(ForestTree.MODID, "branchloader")));
        if(this.sourceOffset != null){
            JsonArray source = new JsonArray();
            source.add(sourceOffset.getX());
            source.add(sourceOffset.getY());
            source.add(sourceOffset.getZ());
            root.add("source", source);
        }

        if (!this.ambientOcclusion) {
            root.addProperty("ambientocclusion", this.ambientOcclusion);
        }

        if (!this.textures.isEmpty()) {
            JsonObject textures = new JsonObject();
            for (Map.Entry<String, String> e : this.textures.entrySet()) {
                textures.addProperty(e.getKey(), serializeLocOrKey(e.getValue()));
            }
            root.add("textures", textures);
        }
        return root;
    }
    private String serializeLocOrKey(String tex) {
        if (tex.charAt(0) == '#') {
            return tex;
        }
        return serializeLoc(new ResourceLocation(tex));
    }

    String serializeLoc(ResourceLocation loc) {
        if (loc.getNamespace().equals("minecraft")) {
            return loc.getPath();
        }
        return loc.toString();
    }
}
