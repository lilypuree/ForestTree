package lilypuree.forest_tree.trees.client.util;

import lilypuree.forest_tree.trees.client.PalmCrownBakedModel;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LeavesHelper {

    /**
     * Makes a palm frond created out of 8 quads, 4 segments.
     *
     * @param origin
     * @param yRot
     * @param pitch
     * @param iIncline
     * @param fold
     * @param centerCrease
     * @param scale
     * @return
     */
    public static List<BakedQuad> createPalmFronds(PalmCrownBakedModel model, Vector3f origin, double yRot, double pitch, double iIncline, float curlFactor, float centerCrease, float fold, float scale, float scaleWidth) {
        curlFactor = MathHelper.clamp(curlFactor, -1, 1);
        centerCrease = MathHelper.clamp(centerCrease, 0, 1);
        fold = MathHelper.clamp(fold, -1, 1);
        double a1 = pitch - iIncline;
        double a2 = Math.asin(Math.sin(a1) / 3);
        double minFIncline = pitch + a2;
        double zeroFIncline = pitch + a1;
        double fIncline = zeroFIncline;
        if (curlFactor < 0) {
            fIncline = minFIncline * (-curlFactor) + zeroFIncline * (1 + curlFactor);
        } else if (curlFactor > 0) {
            fIncline = (iIncline + Math.PI) * curlFactor + zeroFIncline * (1 - curlFactor);
        }
        double a3 = fIncline - pitch;
        double a4 = Math.asin((Math.sin(a3) - Math.sin(a1)) / 2);
        double b1 = a1 - a4;
        double b2 = a3 + a4;
        double turn1;
        double turn2;
        double turn3;
        if (b1 < b2) {
            turn1 = (1 - centerCrease) * b1;
            double c = Math.asin(Math.sin(a1) + Math.sin(a1 - turn1) - Math.sin(a3));
            turn2 = (a1 - turn1) + c;
            turn3 = a3 - c;
        } else {
            turn3 = (1 - centerCrease) * b2;
            double c = Math.asin(Math.sin(a3) + Math.sin(a3 - turn3) - Math.sin(a1));
            turn1 = a1 - c;
            turn2 = (a3 - turn3) + c;
        }
        Vector3f[] v = new Vector3f[]{
                sphericalVector(iIncline, yRot, scale),
                sphericalVector(iIncline + turn1, yRot, scale),
                sphericalVector(iIncline + turn1 + turn2, yRot, scale),
                sphericalVector(fIncline, yRot, scale)
        };
        Vector3f[] n = new Vector3f[]{
                sphericalVector(iIncline - Math.PI / 2, yRot, scaleWidth),
                sphericalVector(iIncline + turn1 - Math.PI / 2, yRot, scaleWidth),
                sphericalVector(iIncline + turn1 + turn2 - Math.PI / 2, yRot, scaleWidth),
                sphericalVector(fIncline - Math.PI / 2, yRot, scaleWidth)
        };
        float angle = (float) Math.PI * (1 - fold) / 2;

        List<BakedQuad> quads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Vector3f r = n[i].copy();
            Vector3f l = n[i].copy();
            r.transform(new Quaternion(v[i], angle, false));
            l.transform(new Quaternion(v[i], -angle, false));
            createQuadFromTwoEdges(quads, origin, r, v[i], model.getPalmFrondTexture(i));
            createQuadFromTwoEdges(quads, origin, l, v[i], model.getPalmFrondTexture(i));
            origin.add(v[i]);
        }
        return quads;
    }

    private static void createQuadFromTwoEdges(Collection<BakedQuad> quads ,Vector3f origin, Vector3f w, Vector3f l, TextureAtlasSprite sprite){
        Vector3f v1 = origin.copy();
        Vector3f v2 = origin.copy();
        Vector3f v3 = origin.copy();
        v1.add(w);
        v2.add(l);
        v3.add(w);
        v3.add(l);
        quads.add(QuadUtils.createQuad(v1, v3, v2, origin, 0, 0, 16, 16, sprite));
        quads.add(QuadUtils.createQuad(v1, v3, v2, origin, 0, 0, 16, 16, sprite, true));
    }

    private static Vector3f sphericalVector(double theta, double phi, float r) {
        float rsin = r * (float) Math.sin(theta);
        return new Vector3f((float) Math.cos(phi) * rsin, (float) Math.cos(theta) * r, (float) Math.sin(phi) * rsin);
    }
}
