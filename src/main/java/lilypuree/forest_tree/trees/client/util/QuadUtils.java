package lilypuree.forest_tree.trees.client.util;

import com.google.common.collect.ImmutableList;
import lilypuree.forest_tree.trees.client.BranchBakedModel;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import java.util.List;

public class QuadUtils {

    public static void putCuboid(List<BakedQuad> quads, Vector3f v1, Vector3f v7, TextureAtlasSprite texture, int tintindex) {
        float x = v7.getX() - v1.getX();
        float y = v7.getY() - v1.getY();
        float z = v7.getZ() - v1.getZ();
        Vector3f v2 = new Vector3f(v7.getX(), v1.getY(), v1.getZ());
        Vector3f v3 = new Vector3f(v7.getX(), v1.getY(), v7.getZ());
        Vector3f v4 = new Vector3f(v1.getX(), v1.getY(), v7.getZ());
        Vector3f v5 = new Vector3f(v1.getX(), v7.getY(), v1.getZ());
        Vector3f v6 = new Vector3f(v7.getX(), v7.getY(), v1.getZ());
        Vector3f v8 = new Vector3f(v1.getX(), v7.getY(), v7.getZ());

        float uvX = x * 16;
        float uvY = y * 16;
        float uvZ = z * 16;

        quads.add(createQuad(v1, v2, v3, v4, 0, 0, uvX, uvZ, texture, tintindex));
        quads.add(createQuad(v1, v5, v6, v2, 0, 0, uvX, uvY, texture, tintindex));
        quads.add(createQuad(v2, v6, v7, v3, 0, 0, uvX, uvY, texture, tintindex));
        quads.add(createQuad(v3, v7, v8, v4, 0, 0, uvX, uvY, texture, tintindex));
        quads.add(createQuad(v4, v8, v5, v1, 0, 0, uvX, uvY, texture, tintindex));
        quads.add(createQuad(v5, v8, v7, v6, 0, 0, uvX, uvZ, texture, tintindex));
    }

    public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, float u, float v, float u_, float v_, TextureAtlasSprite sprite){
       return createQuad(v1, v2, v3, v4, u, v, u_, v_, sprite, -1);
    }

    public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, float u, float v, float u_, float v_, TextureAtlasSprite sprite, int tintIndex) {
        Vector3f normal = v3.copy();
        normal.sub(v2);
        Vector3f other = v1.copy();
        other.sub(v2);
        normal.cross(other);
        normal.normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getFacingFromVector(normal.getX(), normal.getY(), normal.getZ()));
        builder.setQuadTint(tintIndex);
        float rgb = 1.0f;
        putVertex(builder, normal, v1.getX(), v1.getY(), v1.getZ(), u_, v, sprite, rgb, rgb, rgb);
        putVertex(builder, normal, v2.getX(), v2.getY(), v2.getZ(), u_, v_, sprite, rgb, rgb, rgb);
        putVertex(builder, normal, v3.getX(), v3.getY(), v3.getZ(), u, v_, sprite, rgb, rgb, rgb);
        putVertex(builder, normal, v4.getX(), v4.getY(), v4.getZ(), u, v, sprite, rgb, rgb, rgb);
        return builder.build();
    }

    public static void putVertex(BakedQuadBuilder builder, Vector3f normal,
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
                            builder.put(j, (short)0, (short)0);
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

}
