package lilypuree.forest_tree.client.models;

import lilypuree.forest_tree.common.trees.block.ModBlockProperties;
import lilypuree.forest_tree.common.trees.block.StumpBlock;
import lilypuree.forest_tree.client.util.QuadUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class StumpBakedModel implements IDynamicBakedModel {

    private TextureAtlasSprite barkTexture;
    private TextureAtlasSprite topTexture;

    private Map<Integer, List<BakedQuad>> stumpQuads = new HashMap<>();

    public StumpBakedModel(TextureAtlasSprite bark, TextureAtlasSprite top) {
        this.barkTexture = bark;
        this.topTexture = top;

    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (state == null) return Collections.emptyList();

        return stumpQuads.computeIfAbsent(state.get(ModBlockProperties.THICKNESS), a -> {
            StumpBlock block = (StumpBlock) state.getBlock();
            float thickness = ModBlockProperties.getThickness(state);

            int k = BranchBakedModel.getThicknessDivisions(thickness);
            float w = k * BranchBakedModel.MINIMUM_THICKNESS / 2.0f;
            float t = w * 16;
            float barkMinU = (8 - t) / 2.0f;
            float barkMaxU = (8 + t) / 2.0f;

            Vector3f[] verts = new Vector3f[]{
                    new Vector3f(0.5f + w, 0, 0.5f + w), new Vector3f(0.5f + w, 0, 0.5f - w), new Vector3f(0.5f - w, 0, 0.5f - w), new Vector3f(0.5f - w, 0, 0.5f + w), new Vector3f(0.5f + w, 0.5f, 0.5f + w), new Vector3f(0.5f + w, 0.5f, 0.5f - w), new Vector3f(0.5f - w, 0.5f, 0.5f - w), new Vector3f(0.5f - w, 0.5f, 0.5f + w)
            };
            List<BakedQuad> barkBakedQuads = new ArrayList<>();
            List<BakedQuad> endBarkBakedQuads = new ArrayList<>();
            List<BakedQuad> endSectionBakedQuads = new ArrayList<>();

            endBarkBakedQuads.add(QuadUtils.createQuad(verts[0], verts[3], verts[2], verts[1], barkMinU * 2, barkMinU * 2, barkMaxU * 2, barkMaxU * 2, topTexture));
            endBarkBakedQuads.add(QuadUtils.createQuad(verts[4], verts[5], verts[6], verts[7], barkMinU * 2, barkMinU * 2, barkMaxU * 2, barkMaxU * 2, topTexture));
            barkBakedQuads.add(QuadUtils.createQuad(verts[0], verts[4], verts[7], verts[3], barkMinU, 0, barkMaxU, 8, barkTexture));
            barkBakedQuads.add(QuadUtils.createQuad(verts[2], verts[6], verts[5], verts[1], barkMinU, 0, barkMaxU, 8, barkTexture));
            barkBakedQuads.add(QuadUtils.createQuad(verts[3], verts[7], verts[6], verts[2], barkMinU, 0, barkMaxU, 8, barkTexture));
            barkBakedQuads.add(QuadUtils.createQuad(verts[1], verts[5], verts[4], verts[0], barkMinU, 0, barkMaxU, 8, barkTexture));
            barkBakedQuads.addAll(endBarkBakedQuads);

            return barkBakedQuads;
        });
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean func_230044_c_() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return barkTexture;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }

}
