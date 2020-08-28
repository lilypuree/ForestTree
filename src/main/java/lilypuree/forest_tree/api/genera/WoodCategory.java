package lilypuree.forest_tree.api.genera;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public interface WoodCategory {

    default Optional<FoliageCategory> getEndFoliage() {
        return Optional.empty();
    }

    String getName();

    default boolean isVanilla() {
        return false;
    }

    default Block.Properties getStumpProperties(){
        return Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0f).sound(SoundType.WOOD).notSolid();
    }

    default Block.Properties getBranchProperties(){
        return Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0f).sound(SoundType.WOOD).notSolid();
    }

    ResourceLocation getFullBarkTexturePath();

    default ResourceLocation getTruncatedTexturePath() {
        String newPath = getFullBarkTexturePath().getPath().replaceFirst("textures/", "");
        newPath = newPath.replaceFirst(".png", "");
        return new ResourceLocation(getFullBarkTexturePath().getNamespace(), newPath);
    }
}
