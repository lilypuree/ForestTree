package lilypuree.forest_tree.trees.client;

import lilypuree.forest_tree.trees.client.util.LeavesHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PalmCrownBakedModel implements IDynamicBakedModel {

    private TextureAtlasSprite palmCrown;
    private TextureAtlasSprite palmFrondBase;
    private TextureAtlasSprite palmFrondA;
    private TextureAtlasSprite palmFrondB;
    private TextureAtlasSprite palmFrondEnd;
    private List<BakedQuad> palmFronds;

    public PalmCrownBakedModel(TextureAtlasSprite palmCrown, TextureAtlasSprite palmFrondBase, TextureAtlasSprite palmFrondA, TextureAtlasSprite palmFrondB, TextureAtlasSprite palmFrondEnd) {
        this.palmCrown = palmCrown;
        this.palmFrondBase = palmFrondBase;
        this.palmFrondA = palmFrondA;
        this.palmFrondB = palmFrondB;
        this.palmFrondEnd = palmFrondEnd;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (palmFronds == null) {
            palmFronds = new ArrayList<>();
            double initPitch = 40 * Math.PI / 180;
            double initIncline = 25 * Math.PI / 180;
            double finalIncline = 100 * Math.PI / 180;
            double finalPitch = 150 * Math.PI / 180;
            double spiral = 68.75 * 3 * Math.PI / 180;
            int leaves = 20;
            for (int i = 0; i < leaves; i++) {
                double yRot = spiral * i;
                palmFronds.addAll(LeavesHelper.createPalmFronds(this,
                        new Vector3f((float) Math.cos(yRot) * 0.2f + 0.5f, 0.3f - 0.3f * i / leaves, (float) Math.sin(yRot) * 0.2f + 0.5f), yRot,
                        initPitch + (finalPitch - initPitch) / leaves * i, initIncline + (finalIncline - initIncline) / leaves * i, -0.1f + i * 0.2f / leaves, 0.15f, 0.4f - (1 - i/leaves) * 0.8f,
                        0.7f + (float) i / leaves * 0.1f, 0.7f + (float) i / leaves * 0.1f));
            }
        }
        return palmFronds;
    }

    @Override
    public boolean isAmbientOcclusion(BlockState state) {
        return false;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
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
        return palmCrown;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }

    public TextureAtlasSprite getPalmFrondTexture(int index) {
        switch (index) {
            case 0:
                return palmFrondBase;
            case 1:
                return palmFrondA;
            case 2:
                return palmFrondB;
            default:
            case 3:
                return palmFrondEnd;
        }
    }
}
