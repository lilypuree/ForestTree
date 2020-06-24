package lilypuree.forest_tree.trees.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.client.model.geometry.IModelGeometryPart;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class BranchModelGeometry implements IModelGeometry<BranchModelGeometry> {


    private Vec3i source;

    public BranchModelGeometry(Vec3i source){
        Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply()
        this.source = source;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite sideTexture = spriteGetter.apply(owner.resolveTexture("side"));
        TextureAtlasSprite endTexture = spriteGetter.apply(owner.resolveTexture("end"));
        TextureAtlasSprite leafTexture = spriteGetter.apply(owner.resolveTexture("leaf"));
        return new BranchBakedModel(source, sideTexture,endTexture, leafTexture);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Collection<Material> materials = new HashSet<>();
        materials.add(owner.resolveTexture("side"));
        materials.add(owner.resolveTexture("end"));
        materials.add(owner.resolveTexture("leaf"));
        return materials;
    }
}
