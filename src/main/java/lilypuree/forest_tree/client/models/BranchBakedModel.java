package lilypuree.forest_tree.client.models;

import lilypuree.forest_tree.api.genera.WoodCategory;
import lilypuree.forest_tree.common.trees.block.BranchBlock;
import lilypuree.forest_tree.common.trees.block.ModBlockProperties;
import lilypuree.forest_tree.client.util.QuadUtils;
import lilypuree.forest_tree.client.util.QuaternionHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BranchBakedModel implements IDynamicBakedModel {

    public static final int THICKNESS_DIVISIONS = 12;
    public static final float MINIMUM_THICKNESS = 1.0f / THICKNESS_DIVISIONS;
    private float l;
    private boolean noRotation;
    private Vector3f translateVec;
    private Quaternion rotation;
    private TextureAtlasSprite barkTexture;
    private TextureAtlasSprite topTexture;
    private TextureAtlasSprite leafTexture;

    private Map<Integer, List<BakedQuad>> branchQuads = new HashMap<>();
    private Map<Integer, List<BakedQuad>> barkQuads = new HashMap<>();
    private Map<Integer, List<BakedQuad>> endBarkQuads = new HashMap<>();
    private Map<Integer, List<BakedQuad>> endSectionQuads = new HashMap<>();


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
        if (l < 0.51f && l > 0.49f) l = 0.5f;
        rotation = QuaternionHelper.getQuaternionFromSourceOffset(sourceOffset);
        initBranchQuads();
    }

    private void initBranchQuads() {
        for (int i = 1; i <= THICKNESS_DIVISIONS; i++) {
            float w = i * MINIMUM_THICKNESS / 2;
            float t = w * 16;
            float barkMinU = (8 - t) / 2.0f;
            float barkMaxU = (8 + t) / 2.0f;

            Vector3f[] verts = new Vector3f[]{
                    rotatedV(w, -l, w), rotatedV(w, -l, -w), rotatedV(-w, -l, -w), rotatedV(-w, -l, w), rotatedV(w, l, w), rotatedV(w, l, -w), rotatedV(-w, l, -w), rotatedV(-w, l, w)
            };
            List<BakedQuad> barkBakedQuads = new ArrayList<>();
            List<BakedQuad> endBarkBakedQuads = new ArrayList<>();
            List<BakedQuad> endSectionBakedQuads = new ArrayList<>();

            endSectionBakedQuads.add(QuadUtils.createQuad(verts[0], verts[3], verts[2], verts[1], 8 - t, 8 - t, 8 + t, 8 + t, topTexture));
            endSectionBakedQuads.add(QuadUtils.createQuad(verts[4], verts[5], verts[6], verts[7], 8 - t, 8 - t, 8 + t, 8 + t, topTexture));
            endBarkBakedQuads.add(QuadUtils.createQuad(verts[0], verts[3], verts[2], verts[1], barkMinU, barkMinU, barkMaxU, barkMaxU, barkTexture));
            endBarkBakedQuads.add(QuadUtils.createQuad(verts[4], verts[5], verts[6], verts[7], barkMinU, barkMinU, barkMaxU, barkMaxU, barkTexture));
            barkBakedQuads.add(QuadUtils.createQuad(verts[0], verts[4], verts[7], verts[3], barkMinU, 0, barkMaxU, l * 16, barkTexture));
            barkBakedQuads.add(QuadUtils.createQuad(verts[1], verts[5], verts[4], verts[0], barkMinU, 0, barkMaxU, l * 16, barkTexture));
            barkBakedQuads.add(QuadUtils.createQuad(verts[2], verts[6], verts[5], verts[1], barkMinU, 0, barkMaxU, l * 16, barkTexture));
            barkBakedQuads.add(QuadUtils.createQuad(verts[3], verts[7], verts[6], verts[2], barkMinU, 0, barkMaxU, l * 16, barkTexture));
//            barkQuads.put(i, barkBakedQuads);
//            endSectionQuads.put(i, endSectionBakedQuads);
//            endBarkQuads.put(i, endBarkBakedQuads);
            branchQuads.put(i, barkBakedQuads);
            branchQuads.get(i).addAll(endBarkBakedQuads);
        }
    }


    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        if (side != null) {
            return Collections.emptyList();
        }

        float thickness = getThicknessFromModelData(extraData);
        int k = getThicknessDivisions(thickness);

        if (extraData.hasProperty(ModBlockProperties.BRANCH_LEAVES)) {
            List<BakedQuad> fullQuads = new ArrayList<>(branchQuads.get(k));
            ;
            //render full or small leaves
            float w = thickness / 2;
            float xOffset = 0.3f * translateVec.getX();
            ;
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
        } else {
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
        WoodCategory category = block.getWoodCategory();
//        int distanceToTrunk = block.getDistanceToTrunk(world, pos, 0);
//        if (distanceToTrunk < 0) {
//            distanceToTrunk = 20;
//        }
        int distanceToTrunk = 2;
//        float thickness = species.getThickness(state.get(ModBlockProperties.THICKNESS), block.isEnd(), block.getSourceOffset());
        float thickness = ModBlockProperties.getThickness(state);
        if (tileData == EmptyModelData.INSTANCE) {
            tileData = new ModelDataMap.Builder().withInitial(ModBlockProperties.THICKNESS_PROPERTY, thickness).build();
        } else {
            tileData.setData(ModBlockProperties.THICKNESS_PROPERTY, thickness);
        }
        if (category.getEndFoliage().isPresent() && block.isEnd()) {
            Vec3i v = block.getSourceOffset();
            boolean isSmallEnd = v.equals(new Vec3i(0, -1, 0));
            tileData.setData(ModBlockProperties.BRANCH_LEAVES, !isSmallEnd);
        }

        return tileData;
    }

    public static float getThicknessFromModelData(IModelData extraData) {
        float thickness = 0.5f;
        if (extraData.hasProperty(ModBlockProperties.THICKNESS_PROPERTY)) {
            thickness = extraData.getData(ModBlockProperties.THICKNESS_PROPERTY);
        }
        return thickness;
    }

    public static int getThicknessDivisions(float thickness) {
        int k = (int) (thickness / MINIMUM_THICKNESS);
        k = MathHelper.clamp(k, 1, THICKNESS_DIVISIONS);
        return k;
    }
}
