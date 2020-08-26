/**
 * Code based on https://github.com/JTK222/DRP-MARG
 */

package lilypuree.forest_tree.core.setup;

import lilypuree.forest_tree.ForestTree;
import lilypuree.forest_tree.trees.species.ModSpecies;
import lilypuree.forest_tree.trees.species.Species;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

public class BranchTextureStitcher {

    public static File RESOURCE_PACK_FOLDER;

    private ResourceLocation[] requiredTextureLocs = new ResourceLocation[]{
            new ResourceLocation("minecraft:textures/block/oak_log.png")
    };
    private TexturePair[] requiredTextures;


    public static void setupFolders() {
        File modData = new File("./mod_data/" + ForestTree.MODID + "/");
        modData.mkdirs();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            RESOURCE_PACK_FOLDER = new File(modData, "resource_pack");

            new File(RESOURCE_PACK_FOLDER, "assets").mkdirs();
            ModSpecies.allSpecies().stream().map(Species::getFullTexturePath).forEach(fullpath -> {
//                String requiredFile = fullpath.toString().replaceFirst(":", "/");
//                File directory = new File(RESOURCE_PACK_FOLDER +"/assets/" + requiredFile.substring(0, requiredFile.lastIndexOf("/")));
//                directory.mkdirs();
                String fileName = fullpath.toString().replaceFirst(":", "/").replaceAll(".png", "_large.png");
                File outputFile = new File(RESOURCE_PACK_FOLDER + "/assets/" + fileName);
                outputFile.getParentFile().mkdirs();
            });
            try {
                if (!new File(RESOURCE_PACK_FOLDER, "pack.mcmeta").exists())
                    Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("/assets/mcmeta_template"), new File(RESOURCE_PACK_FOLDER, "pack.mcmeta").toPath());
//                if (!new File(RESOURCE_PACK_FOLDER, "pack.png").exists())
//                    Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("/assets/pack.png"), new File(RESOURCE_PACK_FOLDER, "pack.png").toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BranchTextureStitcher prepare() {

        this.requiredTextures = ModSpecies.allSpecies().stream().parallel().map(Species::getFullTexturePath).map(loc -> {
            try {
                IResource resource = Minecraft.getInstance().getResourceManager().getResource(loc);
                if (resource == null) return null;
                InputStream input = new BufferedInputStream(resource.getInputStream());
                return new TexturePair(loc, ImageIO.read(input));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).toArray(TexturePair[]::new);
        return this;
    }

    public void generate() {
        Arrays.stream(requiredTextures).parallel().forEach(texturePair -> {
            try {
                texturePair.setImage(upscale(texturePair.getImage()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Arrays.stream(requiredTextures).parallel().forEach(texturePair -> {
            try {
                String fileName = texturePair.getLoc().toString().replaceFirst(":", "/").replaceAll(".png", "_large.png");
                File outputFile = new File(RESOURCE_PACK_FOLDER + "/assets/" + fileName);
                outputFile.getParentFile().mkdirs();
                ImageIO.write(texturePair.getImage(), "png", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static BufferedImage upscale(BufferedImage sourceImage) {
        int x = sourceImage.getWidth();
        int y = sourceImage.getHeight();
        BufferedImage newImage = new BufferedImage(x * 2, y * 2, sourceImage.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(sourceImage, 0, 0, null);
        graphics.drawImage(sourceImage, x, 0, null);
        graphics.drawImage(sourceImage, 0, y, null);
        graphics.drawImage(sourceImage, x, y, null);
        graphics.dispose();
        return newImage;
    }

    public static class TexturePair {
        private ResourceLocation loc;
        private BufferedImage image;

        public TexturePair(ResourceLocation loc, BufferedImage image) {
            this.loc = loc;
            this.image = image;
        }

        public ResourceLocation getLoc() {
            return loc;
        }

        public BufferedImage getImage() {
            return image;
        }

        public void setImage(BufferedImage image) {
            this.image = image;
        }
    }
}
