package lilypuree.forest_tree.client.models;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class BranchModelGeometry implements IModelGeometry<BranchModelGeometry> {

    private Vec3i source;

    public BranchModelGeometry(Vec3i source) {
//        Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply()
        this.source = source;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite endTexture = spriteGetter.apply(owner.resolveTexture("end"));
        TextureAtlasSprite sideTexture = spriteGetter.apply(appendLarge(owner.resolveTexture("side")));
        TextureAtlasSprite leafTexture = spriteGetter.apply(owner.resolveTexture("leaf"));
        return new BranchBakedModel(source, sideTexture, endTexture, leafTexture);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Collection<Material> materials = new HashSet<>();
        materials.add(appendLarge(owner.resolveTexture("side")));
        materials.add(owner.resolveTexture("end"));
        materials.add(owner.resolveTexture("leaf"));
        return materials;
    }

    public static Material appendLarge(Material material) {
        try {
            Field loc = ObfuscationReflectionHelper.findField(Material.class, "textureLocation");
            Field atlas = ObfuscationReflectionHelper.findField(Material.class, "atlasLocation");
            loc.setAccessible(true);
            atlas.setAccessible(true);
            ResourceLocation textureLocation = (ResourceLocation) loc.get(material);
            return new Material((ResourceLocation)atlas.get(material), new ResourceLocation(textureLocation.getNamespace(), textureLocation.getPath() + "_large"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return material;
    }
}
