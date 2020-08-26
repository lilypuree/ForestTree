package lilypuree.forest_tree.trees.client;

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

public class PalmCrownGeometry implements IModelGeometry<PalmCrownGeometry> {
    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite crown = spriteGetter.apply(owner.resolveTexture("crown"));
        TextureAtlasSprite base = spriteGetter.apply(owner.resolveTexture("base"));
        TextureAtlasSprite a = spriteGetter.apply(owner.resolveTexture("a"));
        TextureAtlasSprite b = spriteGetter.apply(owner.resolveTexture("b"));
        TextureAtlasSprite end = spriteGetter.apply(owner.resolveTexture("end"));
        return new PalmCrownBakedModel(crown, base, a,  b,end);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Collection<Material> materials = new HashSet<>();
        materials.add(owner.resolveTexture("crown"));
        materials.add(owner.resolveTexture("base"));
        materials.add(owner.resolveTexture("a"));
        materials.add(owner.resolveTexture("b"));
        materials.add(owner.resolveTexture("end"));
        return materials;
    }
}
