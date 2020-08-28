package lilypuree.forest_tree.client.models;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class StumpModelGeometry implements IModelGeometry<StumpModelGeometry> {

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite sideTexture = spriteGetter.apply(BranchModelGeometry.appendLarge(owner.resolveTexture("side")));
        TextureAtlasSprite endTexture = spriteGetter.apply(owner.resolveTexture("end"));
        return new StumpBakedModel(sideTexture, endTexture);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Collection<Material> materials = new HashSet<>();
        materials.add(owner.resolveTexture("side"));
        materials.add(BranchModelGeometry.appendLarge(owner.resolveTexture("side")));
        materials.add(owner.resolveTexture("end"));
        return materials;
    }
}
