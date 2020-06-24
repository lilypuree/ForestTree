package lilypuree.forest_tree.trees.client;

import com.google.common.collect.ImmutableList;
import lilypuree.forest_tree.trees.block.BranchBlock;
import lilypuree.forest_tree.trees.block.ModBlockProperties;
import lilypuree.forest_tree.trees.client.util.QuaternionHelper;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BranchBakedModel implements IDynamicBakedModel {

    private float l;
    private boolean noRotation;
    private Vector3f translateVec;
    private Quaternion rotation;
    private TextureAtlasSprite barkTexture;
    private TextureAtlasSprite topTexture;
    private TextureAtlasSprite leafTexture;

    public BranchBakedModel(Vec3i sourceOffset, TextureAtlasSprite bark, TextureAtlasSprite top, @Nullable TextureAtlasSprite leaf) {
        barkTexture = bark;
        topTexture = top;
        leafTexture = leaf;
        if(sourceOffset.equals(new Vec3i(0,-1,0))){
            noRotation = true;
        }else {
            noRotation = false;
        }
        int x = sourceOffset.getX();
        int y = sourceOffset.getY();
        int z = sourceOffset.getZ();
        translateVec = new Vector3f((float) x / 2, (float) y / 2, (float) z / 2);
        translateVec.add(0.5f, 0.5f, 0.5f);
        l = (float) Math.sqrt(x * x + y * y + z * z) / 2;
        rotation = QuaternionHelper.getQuaternionFromSourceOffset(sourceOffset);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        float thickness = 0.2f;
        if (extraData.hasProperty(ModBlockProperties.THICKNESS)) {
            thickness = extraData.getData(ModBlockProperties.THICKNESS);
        }
        float w = thickness / 2;
        Vector3f[] verts = new Vector3f[]{
                rotatedV(w, -l, w), rotatedV(w, -l, -w), rotatedV(-w, -l, -w), rotatedV(-w, -l, w), rotatedV(w, l, w), rotatedV(w, l, -w), rotatedV(-w, l, -w), rotatedV(-w, l, w)
        };
        if (side != null) {
            return Collections.emptyList();
        }
        float t = w * 16;
        List<BakedQuad> quads = new ArrayList<>();
        quads.add(createQuad(verts[0], verts[3], verts[2], verts[1], 0, 0, 16, 16, topTexture));
        quads.add(createQuad(verts[4], verts[5], verts[6], verts[7], 0, 0, 16, 16, topTexture));
        quads.add(createQuad(verts[0], verts[4], verts[7], verts[3], 8 - t, 0, 8 + t, l * 16, barkTexture));
        quads.add(createQuad(verts[1], verts[5], verts[4], verts[0], 8 - t, 0, 8 + t, l * 16, barkTexture));
        quads.add(createQuad(verts[2], verts[6], verts[5], verts[1], 8 - t, 0, 8 + t, l * 16, barkTexture));
        quads.add(createQuad(verts[3], verts[7], verts[6], verts[2], 8 - t, 0, 8 + t, l * 16, barkTexture));

        if (extraData.hasProperty(ModBlockProperties.BRANCH_LEAVES)) {
            //render full or small leaves
            float xOffset = 0;
            float zOffset = 0;
            if (state != null) {
                Vec3i v = ((BranchBlock) state.getBlock()).getSourceOffset();
                xOffset = 0.15f * (float) v.getX();
                zOffset = 0.15f * (float) v.getZ();
            }
            float yOffset = 0f;
            rand = new Random(rand.nextInt(100000));
            yOffset = rand.nextFloat() * 0.4f;

            if (extraData.getData(ModBlockProperties.BRANCH_LEAVES)) {
                putCuboid(quads, new Vector3f(-0.1f + xOffset - w * 0.3f, 0 + yOffset, -0.1f + zOffset - w * 0.3f), new Vector3f(0.7f + xOffset + w * 0.3f, 0.6f + yOffset, 0.7f + zOffset + w * 0.3f), leafTexture);
            } else {
                putCuboid(quads, new Vector3f(0.2f, 0, 0.2f), new Vector3f(0.8f, 0.9f, 0.8f), leafTexture);
            }
        }
        return quads;
    }

    private void putCuboid(List<BakedQuad> quads, Vector3f v1, Vector3f v7, TextureAtlasSprite texture) {
        float x = v7.getX() - v1.getX();
        float y = v7.getY() - v1.getY();
        float z = v7.getZ() - v1.getZ();
        Vector3f v2 = new Vector3f(v7.getX(), v1.getY(), v1.getZ());
        Vector3f v3 = new Vector3f(v7.getX(), v1.getY(), v7.getZ());
        Vector3f v4 = new Vector3f(v1.getX(), v1.getY(), v7.getZ());
        Vector3f v5 = new Vector3f(v1.getX(), v7.getY(), v1.getZ());
        Vector3f v6 = new Vector3f(v7.getX(), v7.getY(), v1.getZ());
        Vector3f v8 = new Vector3f(v1.getX(), v7.getY(), v7.getZ());
        quads.add(createQuad(v1, v2, v3, v4, 0, 0, x, z, texture));
        quads.add(createQuad(v1, v5, v6, v2, 0, 0, x, y, texture));
        quads.add(createQuad(v2, v6, v7, v3, 0, 0, x, y, texture));
        quads.add(createQuad(v3, v7, v8, v4, 0, 0, x, y, texture));
        quads.add(createQuad(v4, v8, v5, v1, 0, 0, x, y, texture));
        quads.add(createQuad(v5, v8, v7, v6, 0, 0, x, z, texture));
    }

    private BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, float u, float v, float u_, float v_, TextureAtlasSprite sprite) {
        Vector3f normal = v3.copy();
        normal.sub(v2);
        Vector3f other = v1.copy();
        other.sub(v2);
        normal.cross(other);
        normal.normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.getX(), normal.getY(), normal.getZ()));
        putVertex(builder, normal, v1.getX(), v1.getY(), v1.getZ(), u_, v, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v2.getX(), v2.getY(), v2.getZ(), u_, v_, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v3.getX(), v3.getY(), v3.getZ(), u, v_, sprite, 1.0f, 1.0f, 1.0f);
        putVertex(builder, normal, v4.getX(), v4.getY(), v4.getZ(), u, v, sprite, 1.0f, 1.0f, 1.0f);
        return builder.build();
    }

    private void putVertex(BakedQuadBuilder builder, Vector3f normal,
                           double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b) {

        ImmutableList<VertexFormatElement> elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0; j < elements.size(); j++) {
            VertexFormatElement e = elements.get(j);
            switch (e.getUsage()) {
                case POSITION:
                    builder.put(j, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(j, r, g, b, 1.0f);
                    break;
                case UV:
                    switch (e.getIndex()) {
                        case 0:
                            float iu = sprite.getInterpolatedU(u);
                            float iv = sprite.getInterpolatedV(v);
                            builder.put(j, iu, iv);
                            break;
                        case 2:
                            builder.put(j, 0f, 1f);
                            break;
                        default:
                            builder.put(j);
                            break;
                    }
                    break;
                case NORMAL:
                    builder.put(j, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ());
                    break;
                default:
                    builder.put(j);
                    break;
            }
        }
    }

    private static Vec3d v(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }

    private Vector3f rotatedV(double x, double y, double z) {
        Vector3f vec = rotateVector(v(x, y, z));
        vec.add(translateVec);
        return vec;
    }

    private Vector3f rotateVector(Vec3d in) {
        if(noRotation){
            return new Vector3f((float)in.x,(float)in.y,(float)in.z);
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
