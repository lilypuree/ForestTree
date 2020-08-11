package com.ferreusveritas.dynamictrees.worldgen.feature;

import com.ferreusveritas.dynamictrees.api.worldgen.IGroundFinder;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.WorldTreeGenerator;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

public class WorldTreeFeature<T extends NoFeatureConfig> extends Feature<T> {


    public WorldTreeFeature(Function<Dynamic<?>, ? extends T> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, T config) {
        WorldTreeGenerator worldTreeGenerator = WorldTreeGenerator.getWorldTreeGenerator();
        BiomeDataBase dataBase = worldTreeGenerator.getBiomeDataBase(worldIn);
        IGroundFinder groundFinder = new GroundFinder();
        ChunkPos chunkPos = new ChunkPos(pos);
        if (dataBase != WorldTreeGenerator.DIMENSIONBLACKLISTED) {
            SafeChunkBounds safeBounds = new SafeChunkBounds(worldIn, chunkPos);
            worldTreeGenerator.getCircleProvider().getPoissonDiscs(worldIn, chunkPos).forEach(c -> {
                worldTreeGenerator.makeTree(worldIn, dataBase, c, groundFinder, safeBounds);
            });
            return true;
        }
        return false;
    }


    public static class GroundFinder implements IGroundFinder {

        protected boolean inNetherRange(BlockPos pos) {
            return pos.getY() >= 0 && pos.getY() <= 128;
        }

        protected ArrayList<Integer> findSubterraneanLayerHeights(IWorld world, BlockPos start) {


            BlockPos.Mutable pos = new BlockPos.Mutable(world.getHeight(Heightmap.Type.MOTION_BLOCKING, start).offset(Direction.DOWN));

            ArrayList<Integer> layers = new ArrayList();

            while (inNetherRange(pos)) {
                while (!world.isAirBlock(pos) && inNetherRange(pos)) {
                    pos.move(Direction.UP, 4);
                } //Zip up 4 blocks at a time until we hit air
                while (world.isAirBlock(pos) && inNetherRange(pos)) {
                    pos.move(Direction.DOWN);
                } //Move down 1 block at a time until we hit not-air
                if (world.getBlockState(pos).getMaterial() != Material.LAVA) {
                    layers.add(pos.getY());
                } //Record this position
                pos.move(Direction.UP, 16); //Move up 16 blocks
                while (world.isAirBlock(pos) && inNetherRange(pos)) {
                    pos.move(Direction.UP, 4);
                } //Zip up 4 blocks at a time until we hit ground
            }

            //Discard the last result as it's just the top of the biome(bedrock for nether)
            if (layers.size() > 0) {
                layers.remove(layers.size() - 1);
            }

            return layers;
        }

        protected BlockPos findSubterraneanGround(IWorld world, BlockPos start) {
            ArrayList<Integer> layers = findSubterraneanLayerHeights(world, start);
            if (layers.size() < 1) {
                return BlockPos.ZERO;
            }
            int y = layers.get(world.getRandom().nextInt(layers.size()));
            return new BlockPos(start.getX(), y, start.getZ());
        }

        protected boolean inOverworldRange(BlockPos pos) {
            return pos.getY() >= 0 && pos.getY() <= 255;
        }

        protected BlockPos findOverworldGround(IWorld world, BlockPos start) {

            IChunk chunk = world.getChunk(start);//We'll use a chunk for the search so we don't have to keep looking up the chunk for every block

            BlockPos.Mutable mPos = new BlockPos.Mutable(world.getHeight(Heightmap.Type.MOTION_BLOCKING, start)).move(Direction.UP, 2);//Mutable allows us to change the test position easily
            while (inOverworldRange(mPos)) {

                BlockState state = chunk.getBlockState(mPos);
                Block testBlock = state.getBlock();

                if (!world.isAirBlock(mPos)) {
                    Material material = state.getMaterial();
                    if (material == Material.EARTH || material == Material.WATER || //These will account for > 90% of blocks in the world so we can solve this early
                            (state.getMaterial().blocksMovement() &&
                                    !(material == Material.LEAVES) &&
                                    !state.isFoliage(world, mPos))) {
                        return mPos.toImmutable();
                    }
                }

                mPos.move(Direction.DOWN);
            }

            return BlockPos.ZERO;
        }

        @Override
        public BlockPos findGround(BiomeDataBase.BiomeEntry biomeEntry, IWorld world, BlockPos start) {
            return biomeEntry.isSubterraneanBiome() ? findSubterraneanGround(world, start) : findOverworldGround(world, start);
        }

    }


}
