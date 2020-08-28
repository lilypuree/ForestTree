package lilypuree.forest_tree.mixin;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.function.Function;

@Mixin(AbstractTreeFeature.class)
public class AbstractTreeFeatureMixin<T extends BaseTreeFeatureConfig>{

    @Inject(method = "place(Lnet/minecraft/world/IWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/BaseTreeFeatureConfig;)Z",at =@At("HEAD"), cancellable = true)
    protected void onPlace(CallbackInfoReturnable<Boolean> result) {
        result.setReturnValue(false);
    }
}
