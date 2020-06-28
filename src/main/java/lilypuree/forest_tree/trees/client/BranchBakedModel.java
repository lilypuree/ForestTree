package lilypuree.forest_tree.trees.client;

import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.functions.FuncCeiling;
import lilypuree.forest_tree.trees.block.BranchBlock;
import lilypuree.forest_tree.trees.block.ModBlockProperties;
import lilypuree.forest_tree.trees.client.util.QuadUtils;
import lilypuree.forest_tree.trees.client.util.QuaternionHelper;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BranchBakedModel implements IDynamicBakedModel {

    private static final int THICKNESS_DIVISIONS = 8;
    private static final float MINIMUM_THICKNESS = 1.0f / THICKNESS_DIVISIONS;
    private float l;
    private boolean noRotation;
    private Vector3f translateVec;
    private Quaternion rotation;
    private TextureAtlasSprite barkTexture;
    private TextureAtlasSprite topTexture;
    private TextureAtlasSprite leafTexture;

    private Map<Integer, List<BakedQuad>> branchQuads = new HashMap<>();


    public BranchBakedModel(Vec3i sourceOffset, TextureAtlasSprite bark, TextureAtlasSprite top, @Nullable TextureAtlasSprite leaf) {
        barkTexture = bark;
        topTexture = top;
        leafTexture = leaf;
        if (sourceOffset.equals(new Vec3i(0, -1, 0))) {
            noRotation = true;
        } else {
            noRotation = false;
        }
        int x = sourceOffset.getX();
        int y = sourceOffset.getY();
        int z = sourceOffset.getZ();
        translateVec = new Vector3f((float) x / 2, (float) y / 2, (float) z / 2);
        translateVec.add(0.5f, 0.5f, 0.5f);
        l = (float) Math.sqrt(x * x + y * y + z * z) / 2;
        rotation = QuaternionHelper.getQuaternionFromSourceOffset(sourceOffset);
        initBranchQuads();
    }

    private void initBranchQuads() {
        for (int i = 1; i <= THICKNESS_DIVISIONS; i++) {
            float w = i * MINIMUM_THICKNESS / 2;
            float t = w * 16;

            Vector3f[] verts = new Vector3f[]{
                    rotatedV(w, -l, w), rotatedV(w, -l, -w), rotatedV(-w, -l, -w), rotatedV(-w, -l, w), rotatedV(w, l, w), rotatedV(w, l, -w), rotatedV(-w, l, -w), rotatedV(-w, l, w)
            };
            List<BakedQuad> bakedQuads = new ArrayList<>();
            bakedQuads.add(QuadUtils.createQuad(verts[0], verts[3], verts[2], verts[1], 8 - t, 8 - t, 8 + t, 8 + t, topTexture));
            bakedQuads.add(QuadUtils.createQuad(verts[4], verts[5], verts[6], verts[7], 8 - t, 8 - t, 8 + t, 8 + t, topTexture));
            bakedQuads.add(QuadUtils.createQuad(verts[0], verts[4], verts[7], verts[3], 8 - t, 0, 8 + t, l * 16, barkTexture));
            bakedQuads.add(QuadUtils.createQuad(verts[1], verts[5], verts[4], verts[0], 8 - t, 0, 8 + t, l * 16, barkTexture));
            bakedQuads.add(QuadUtils.createQuad(verts[2], verts[6], verts[5], verts[1], 8 - t, 0, 8 + t, l * 16, barkTexture));
            bakedQuads.add(QuadUtils.createQuad(verts[3], verts[7], verts[6], verts[2], 8 - t, 0, 8 + t, l * 16, barkTexture));
            branchQuads.put(i, bakedQuads);
        }
    }



    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        if (side != null) {
            return Collections.emptyList();
        }

        float thickness = 0.4f;
        if (extraData.hasProperty(ModBlockProperties.THICKNESS)) {
            thickness = extraData.getData(ModBlockProperties.THICKNESS);
        }
        int k = (int) (thickness / MINIMUM_THICKNESS) + 1;
        k = Math.min(k, THICKNESS_DIVISIONS);



        if (extraData.hasProperty(ModBlockProperties.BRANCH_LEAVES)) {
            List<BakedQuad> fullQuads = new ArrayList<>(branchQuads.get(k));;
            //render full or small leaves
            float w = thickness / 2;
            float xOffset = 0.3f * translateVec.getX();;
            float zOffset = 0.3f * translateVec.getZ();

//            rand = new Random(rand.nextInt(100000));
            float yOffset = rand.nextFloat() * 0.4f;

            //BRANCH_LEAVES is false if it is on the tip of a branch i.e. "small end"
            if (extraData.getData(ModBlockProperties.BRANCH_LEAVES)) {
                QuadUtils.putCuboid(fullQuads, new Vector3f(-0.1f + xOffset - w * 0.3f, 0 + yOffset, -0.1f + zOffset - w * 0.3f), new Vector3f(0.7f + xOffset + w * 0.3f, 0.6f + yOffset, 0.7f + zOffset + w * 0.3f), leafTexture, 0);
            } else {
                QuadUtils.putCuboid(fullQuads, new Vector3f(0.2f, 0, 0.2f), new Vector3f(0.8f, 0.9f, 0.8f), leafTexture, 0);
            }
            return fullQuads;
        }else {
            return branchQuads.get(k); //no leaves
        }
    }

    private Vector3f rotatedV(double x, double y, double z) {
        Vector3f vec = rotateVector(new Vec3d(x, y, z));
        vec.add(translateVec);
        return vec;
    }

    private Vector3f rotateVector(Vec3d in) {
        if (noRotation) {
            return new Vector3f((float) in.x, (float) in.y, (float) in.z);
        }
        Vec3d u = new Vec3d(rotation.getX(), rotation.getY(), rotation.getZ());
        float s = rotation.getW();
        Vec3d result = u.scale(u.dotProduct(in) * 2.0f).add(in.scale(s * s - u.dotProduct(u))).add(u.crossProduct(in).scale(2.0f * s));
        return new Vector3f((float) result.x, (float) result.y, (float) result.z);
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
        return barkTexture;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        BranchBlock block = (BranchBlock) state.getBlock();
        Species species = block.getSpecies();
        int distanceToTrunk = block.getDistanceToTrunk(world, pos, 0);
        if (distanceToTrunk < 0) {
            distanceToTrunk = 20;
        }
        float thickness = species.getThickness(distanceToTrunk, state.get(ModBlockProperties.TREE_AGE), block.isEnd(), block.getSourceOffset());
        if (tileData == EmptyModelData.INSTANCE) {
            tileData = new ModelDataMap.Builder().withInitial(ModBlockProperties.THICKNESS, thickness).build();
        } else {
            tileData.setData(ModBlockProperties.THICKNESS, thickness);
        }
        if (species.isConifer() && block.isEnd()) {
            Vec3i v = block.getSourceOffset();
            boolean isSmallEnd = v.equals(new Vec3i(0, -1, 0));
            tileData.setData(ModBlockProperties.BRANCH_LEAVES, !isSmallEnd);
        }

        return tileData;
    }
}
