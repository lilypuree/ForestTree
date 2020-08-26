package lilypuree.forest_tree.shrubs.client;

import lilypuree.forest_tree.shrubs.block.MultipleFlowerTile;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class MultipleFlowerModel implements IDynamicBakedModel {

    private static ResourceLocation TEXTURE = new ResourceLocation("minecraft:block/poppy");
    public static ModelProperty<Long> SEED = new ModelProperty<>();

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();
        List<BlockState> flowers = extraData.getData(MultipleFlowerTile.FLOWERS);
        Random posRand = new Random(extraData.getData(SEED));
        if (flowers != null) {
            Vec2f[] offsets = getRandomOffsets(flowers.size(), posRand);
            for (int i = 0; i < flowers.size(); i++) {
                IBakedModel flowerModel = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(flowers.get(i));
                int finalI = i;
                quads.addAll(flowerModel.getQuads(state, side, posRand, EmptyModelData.INSTANCE).stream().map(quad -> getOffsetQuad(quad, offsets[finalI])).collect(Collectors.toList()));
            }
        }
        return quads;
    }

    private BakedQuad getOffsetQuad(BakedQuad quadIn, Vec2f offset) {
        int[] vertexData = quadIn.getVertexData().clone();
        for (int i = 0, j = 0; i < 4; i++, j += DefaultVertexFormats.BLOCK.getIntegerSize()) {
            float x = Float.intBitsToFloat(vertexData[j]);
            float z = Float.intBitsToFloat(vertexData[j + 2]);
            x += offset.x;
            z += offset.y;
            vertexData[j] = Float.floatToIntBits(x);
            vertexData[j + 2] = Float.floatToIntBits(z);
        }
        return new BakedQuad(vertexData, quadIn.getTintIndex(), quadIn.getFace(), quadIn.func_187508_a(), quadIn.shouldApplyDiffuseLighting());
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
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }

    private static Vec2f centerPoint;
    private static Vec2f[] cornerpoints;
    private static Vec2f[] centralpoints;

    static {
        centerPoint = new Vec2f(0.0f, 0.0f);
        cornerpoints = new Vec2f[8];
        centralpoints = new Vec2f[4];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                centralpoints[i * 2 + j] = new Vec2f(-0.25f + 0.5f * i, -0.25f + 0.5f * j);
            }
        }
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) continue;
                cornerpoints[k++] = new Vec2f(-1 / 3f + 1 / 3f * i, -1 / 3f + 1 / 3f * j);
            }
        }
    }

    private static Vec2f[] getRandomOffsets(int number, Random rand) {
        Vec2f[] newPositions = new Vec2f[9];
        if (number > 9 || number <= 0) {

        } else if (number > 1 && number < 5) {
            newPositions = pickNRandom(centralpoints, number , rand);
            for (int i = 0; i < number; i++) {
                newPositions[i] = getRandomOffset(newPositions[i], 0.2f, rand);
            }
        } else {
            newPositions[0] = getRandomOffset(centerPoint, 0.3f, rand);
            if (number == 5) {
                Vec2f[] offsets = pickNRandom(centralpoints, number - 1, rand);
                for (int i = 0; i < 4; i++) {
                    newPositions[i + 1] = getRandomOffset(offsets[i], 0.2f, rand);
                }
            } else {
                Vec2f[] offsets = pickNRandom(cornerpoints, number - 1, rand);
                for (int i = 0; i < number - 1; i++) {
                    newPositions[i + 1] = (getRandomOffset(offsets[i], 1 / 6f, rand));
                }
            }
        }
        return newPositions;
    }

    private static Vec2f getRandomOffset(Vec2f cen, float dispersion, Random rand) {
        float xVar = MathHelper.clamp(rand.nextFloat() * dispersion * 2 - dispersion, -dispersion, +dispersion);
        float yVar = MathHelper.clamp(rand.nextFloat() * dispersion * 2 - dispersion, -dispersion, +dispersion);
        return new Vec2f(cen.x + xVar, cen.y + yVar);
    }

    public static Vec2f[] pickNRandom(Vec2f[] array, int n, Random rand) {
        List<Vec2f> list = new ArrayList<Vec2f>(array.length);
        list.addAll(Arrays.asList(array));
        Collections.shuffle(list, rand);

        Vec2f[] answer = new Vec2f[n];
        for (int i = 0; i < n; i++)
            answer[i] = list.get(i);
//        Arrays.sort(answer);

        return answer;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        tileData.setData(SEED, pos.toLong());
        return tileData;
    }
}
