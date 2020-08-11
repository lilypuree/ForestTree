package com.ferreusveritas.dynamictrees.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IRadiusCoordinator;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.PerlinNoiseGenerator;

import java.util.function.Function;

public class BiomeRadiusCoordinator implements IRadiusCoordinator {

	public PerlinNoiseGenerator noiseGenerator;
	protected final WorldTreeGenerator worldTreeGenerator;
	protected final IWorld world;
	protected int pass;
	protected Function<Integer, Integer> chunkMultipass;
	
	public BiomeRadiusCoordinator(WorldTreeGenerator worldTreeGenerator, IWorld world) {
//		noiseGenerator = new NoiseGeneratorPerlin(new Random(96), 1);
		noiseGenerator = new PerlinNoiseGenerator(new SharedSeedRandom(world.getSeed()), 3, 32);
		this.world = world;
		this.worldTreeGenerator = worldTreeGenerator;
	}
	
	@Override
	public int getRadiusAtCoords(int x, int z) {
		
		int rad = chunkMultipass.apply(pass);
		if(rad >= 2 && rad <= 8) {
			return rad;
		}
		
		double scale = 128;//Effectively scales up the noisemap
		Biome biome = world.getBiome(new BlockPos(x + 8, 0, z + 8));//Placement is offset by +8,+8
//		double noiseDensity = (noiseGenerator.getValue(x / scale, z / scale) + 1D) / 2.0D;//Gives 0.0 to 1.0
		double noiseDensity = (noiseGenerator.noiseAt(x / scale, z / scale, true) + 1D) / 2.0D;//Gives 0.0 to 1.0
		double density = worldTreeGenerator.getBiomeDataBase(world).getDensity(biome).getDensity(world.getRandom(), noiseDensity);
		double size = ((1.0 - density) * 9);//Size is the inverse of density(Gives 0 to 9)
		
		//Oh Joy. Random can potentially start with the same number for each chunk. Let's just 
		//throw this large prime xor hack in there to get it to at least look like it's random.
		int kindaRandom = ((x * 674365771) ^ (z * 254326997)) >> 4;
		int shakelow =  (kindaRandom & 0x3) % 3;//Produces 0,0,1 or 2
		int shakehigh = (kindaRandom & 0xc) % 3;//Produces 0,0,1 or 2
		
		return MathHelper.clamp((int) size, 2 + shakelow, 8 - shakehigh);//Clamp to tree volume radius range
	}
	
	@Override
	public boolean runPass(int chunkX, int chunkZ, int pass) {
		this.pass = pass;
		
		if(pass == 0) {
			Biome biome = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));//Aim at center of chunk
			chunkMultipass = worldTreeGenerator.getBiomeDataBase(world).getMultipass(biome);
		}
		
		return chunkMultipass.apply(pass) >= 0;
	}
	
}
