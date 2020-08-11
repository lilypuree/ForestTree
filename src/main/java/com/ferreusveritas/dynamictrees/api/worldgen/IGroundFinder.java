package com.ferreusveritas.dynamictrees.api.worldgen;

import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.BiomeEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface IGroundFinder {
	BlockPos findGround(BiomeEntry biomeEntry, IWorld world, BlockPos start);
}
