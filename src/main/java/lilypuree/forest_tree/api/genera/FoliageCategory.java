package lilypuree.forest_tree.api.genera;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.FoliageColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

public interface FoliageCategory {

    String getName();

    Block getDefaultFoliage();

    default boolean isForestTreeLeaves(){
        return false;
    }

    default Block.Properties getLeavesProperties(){
        return Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).doesNotBlockMovement().notSolid();
    }

    @OnlyIn(Dist.CLIENT)
    default int getFoliageColor() {
        return FoliageColors.getDefault();
    }

    default boolean hasLeafBlock() {
        return true;
    }


}
