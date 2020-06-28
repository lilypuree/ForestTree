package lilypuree.forest_tree.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {


    @Shadow
    private Minecraft mc;
//    @Inject(method = "getMouseOver(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getEyePosition(F)Lnet/minecraft/util/math/Vec3d;"))

    @Inject(method = "getMouseOver(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getEyePosition(F)Lnet/minecraft/util/math/Vec3d;"))
    public void onGetMouseOver(float partialTicks, CallbackInfo info){

    }


}
