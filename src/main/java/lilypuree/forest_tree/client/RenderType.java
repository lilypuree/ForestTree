////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package net.minecraft.client.renderer;
//
//import com.google.common.collect.ImmutableList;
//import it.unimi.dsi.fastutil.Hash.Strategy;
//import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.OptionalDouble;
//import javax.annotation.Nullable;
//
//import net.minecraft.client.renderer.tileentity.EndPortalTileEntityRenderer;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.client.renderer.vertex.VertexFormat;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public abstract class RenderType extends RenderState {
//
//
//
//    private static final RenderType SOLID;
//    private static final RenderType CUTOUT_MIPPED;
//    private static final RenderType CUTOUT;
//    private static final RenderType TRANSLUCENT;
//    private static final RenderType TRANSLUCENT_NO_CRUMBLING;
//    private static final RenderType LEASH;
//    private static final RenderType WATER_MASK;
//    private static final RenderType GLINT;
//    private static final RenderType ENTITY_GLINT;
//    private static final RenderType LIGHTNING;
//    public static final RenderType.Type LINES;
//    private final VertexFormat vertexFormat;
//    private final int drawMode;
//    private final int bufferSize;
//    private final boolean useDelegate;
//    private final boolean needsSorting;
//    private final Optional<RenderType> field_230166_ag_;
//
//    public static RenderType getSolid() {
//        return SOLID;
//    }
//
//    public static RenderType getCutoutMipped() {
//        return CUTOUT_MIPPED;
//    }
//
//    public static RenderType getCutout() {
//        return CUTOUT;
//    }
//
//    private static RenderType.State getTranslucentState() {
//        return RenderType.State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).transparency(TRANSLUCENT_TRANSPARENCY).build(true);
//    }
//
//    public static RenderType getTranslucent() {
//        return TRANSLUCENT;
//    }
//
//    public static RenderType getTranslucentNoCrumbling() {
//        return TRANSLUCENT_NO_CRUMBLING;
//    }
//
//    public static RenderType getEntitySolid(ResourceLocation texture) {
//        RenderType.State renderState = RenderType.State.getBuilder().texture(new TextureState(texture, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
//        return makeType("entity_solid", DefaultVertexFormats.ENTITY, 7, 256, true, false, renderState);
//    }
//
//    public static RenderType getEntityCutout(ResourceLocation p_228638_0_) {
//        RenderType.State lvt_1_1_ = RenderType.State.getBuilder().texture(new TextureState(p_228638_0_, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
//        return makeType("entity_cutout", DefaultVertexFormats.ENTITY, 7, 256, true, false, lvt_1_1_);
//    }
//
//    public static RenderType getEntityCutoutNoCull(ResourceLocation p_230167_0_, boolean p_230167_1_) {
//        RenderType.State lvt_2_1_ = RenderType.State.getBuilder().texture(new TextureState(p_230167_0_, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(p_230167_1_);
//        return makeType("entity_cutout_no_cull", DefaultVertexFormats.ENTITY, 7, 256, true, false, lvt_2_1_);
//    }
//
//    public static RenderType getEntityCutoutNoCull(ResourceLocation p_228640_0_) {
//        return getEntityCutoutNoCull(p_228640_0_, true);
//    }
//
//    public static RenderType getEntityTranslucentCull(ResourceLocation texture) {
//        RenderType.State renderState = RenderType.State.getBuilder().texture(new TextureState(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
//        return makeType("entity_translucent_cull", DefaultVertexFormats.ENTITY, 7, 256, true, true, renderState);
//    }
//
//    public static RenderType getEntityTranslucent(ResourceLocation texture, boolean outline) {
//        RenderType.State renderState = RenderType.State.getBuilder().texture(new TextureState(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(outline);
//        return makeType("entity_translucent", DefaultVertexFormats.ENTITY, 7, 256, true, true, renderState);
//    }
//
//    public static RenderType getEntityTranslucent(ResourceLocation p_228644_0_) {
//        return getEntityTranslucent(p_228644_0_, true);
//    }
//
//    public static RenderType getEntitySmoothCutout(ResourceLocation p_228646_0_) {
//        RenderType.State lvt_1_1_ = RenderType.State.getBuilder().texture(new TextureState(p_228646_0_, false, false)).alpha(HALF_ALPHA).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).shadeModel(SHADE_ENABLED).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).build(true);
//        return makeType("entity_smooth_cutout", DefaultVertexFormats.ENTITY, 7, 256, lvt_1_1_);
//    }
//
//    public static RenderType getBeaconBeam(ResourceLocation texture, boolean isTransparent) {
//        RenderType.State renderState = RenderType.State.getBuilder().texture(new TextureState(texture, false, false)).transparency(isTransparent ? TRANSLUCENT_TRANSPARENCY : NO_TRANSPARENCY).writeMask(isTransparent ? COLOR_WRITE : COLOR_DEPTH_WRITE).fog(NO_FOG).build(false);
//        return makeType("beacon_beam", DefaultVertexFormats.BLOCK, 7, 256, false, true, renderState);
//    }
//
//    public static RenderType getEntityDecal(ResourceLocation p_228648_0_) {
//        RenderType.State lvt_1_1_ = RenderType.State.getBuilder().texture(new TextureState(p_228648_0_, false, false)).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).depthTest(DEPTH_EQUAL).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(false);
//        return makeType("entity_decal", DefaultVertexFormats.ENTITY, 7, 256, lvt_1_1_);
//    }
//
//    public static RenderType getEntityNoOutline(ResourceLocation p_228650_0_) {
//        RenderType.State lvt_1_1_ = RenderType.State.getBuilder().texture(new TextureState(p_228650_0_, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).writeMask(COLOR_WRITE).build(false);
//        return makeType("entity_no_outline", DefaultVertexFormats.ENTITY, 7, 256, false, true, lvt_1_1_);
//    }
//
//    public static RenderType getEntityAlpha(ResourceLocation p_228635_0_, float p_228635_1_) {
//        RenderType.State renderState = RenderType.State.getBuilder().texture(new TextureState(p_228635_0_, false, false)).alpha(new AlphaState(p_228635_1_)).cull(CULL_DISABLED).build(true);
//        return makeType("entity_alpha", DefaultVertexFormats.ENTITY, 7, 256, renderState);
//    }
//
//    public static RenderType getEyes(ResourceLocation texture) {
//        TextureState textureState = new TextureState(texture, false, false);
//        return makeType("eyes", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder().texture(textureState).transparency(ADDITIVE_TRANSPARENCY).writeMask(COLOR_WRITE).fog(BLACK_FOG).build(false));
//    }
//
//    public static RenderType getEnergySwirl(ResourceLocation texture, float offsetU, float offsetV) {
//        return makeType("energy_swirl", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder().texture(new TextureState(texture, false, false)).texturing(new OffsetTexturingState(offsetU, offsetV)).fog(BLACK_FOG).transparency(ADDITIVE_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(false));
//    }
//
//    public static RenderType getLeash() {
//        return LEASH;
//    }
//
//    public static RenderType getWaterMask() {
//        return WATER_MASK;
//    }
//
//    public static RenderType getOutline(ResourceLocation texture) {
//        return makeType("outline", DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256, RenderType.State.getBuilder().texture(new TextureState(texture, false, false)).cull(CULL_DISABLED).depthTest(DEPTH_ALWAYS).alpha(DEFAULT_ALPHA).texturing(OUTLINE_TEXTURING).fog(NO_FOG).target(OUTLINE_TARGET).build(RenderType.OutlineState.IS_OUTLINE));
//    }
//
//    public static RenderType getGlint() {
//        return GLINT;
//    }
//
//    public static RenderType getEntityGlint() {
//        return ENTITY_GLINT;
//    }
//
//    public static RenderType getCrumbling(ResourceLocation texture) {
//        TextureState lvt_1_1_ = new TextureState(texture, false, false);
//        return makeType("crumbling", DefaultVertexFormats.BLOCK, 7, 256, false, true, RenderType.State.getBuilder().texture(lvt_1_1_).alpha(DEFAULT_ALPHA).transparency(CRUMBLING_TRANSPARENCY).writeMask(COLOR_WRITE).layer(POLYGON_OFFSET_LAYERING).build(false));
//    }
//
//    public static RenderType getText(ResourceLocation texture) {
//        return makeType("text", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, true, RenderType.State.getBuilder().texture(new TextureState(texture, false, false)).alpha(DEFAULT_ALPHA).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(LIGHTMAP_ENABLED).build(false));
//    }
//
//    public static RenderType getTextSeeThrough(ResourceLocation texture) {
//        return makeType("text_see_through", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, true, RenderType.State.getBuilder().texture(new TextureState(texture, false, false)).alpha(DEFAULT_ALPHA).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(LIGHTMAP_ENABLED).depthTest(DEPTH_ALWAYS).writeMask(COLOR_WRITE).build(false));
//    }
//
//    public static RenderType getLightning() {
//        return LIGHTNING;
//    }
//
//    public static RenderType getEndPortal(int p_228630_0_) {
//        TransparencyState transparencyState;
//        TextureState textureState;
//        if (p_228630_0_ <= 1) {
//            transparencyState = TRANSLUCENT_TRANSPARENCY;
//            textureState = new TextureState(EndPortalTileEntityRenderer.END_SKY_TEXTURE, false, false);
//        } else {
//            transparencyState = ADDITIVE_TRANSPARENCY;
//            textureState = new TextureState(EndPortalTileEntityRenderer.END_PORTAL_TEXTURE, false, false);
//        }
//
//        return makeType("end_portal", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, RenderType.State.getBuilder().transparency(transparencyState).texture(textureState).texturing(new PortalTexturingState(p_228630_0_)).fog(BLACK_FOG).build(false));
//    }
//
//    public static RenderType getLines() {
//        return LINES;
//    }
//
//    public RenderType(String nameIn, VertexFormat vertexFormatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
//        super(nameIn, setupTaskIn, clearTaskIn);
//        this.vertexFormat = vertexFormatIn;
//        this.drawMode = drawModeIn;
//        this.bufferSize = bufferSizeIn;
//        this.useDelegate = useDelegateIn;
//        this.needsSorting = needsSortingIn;
//        this.field_230166_ag_ = Optional.of(this);
//    }
//
//    public static RenderType.Type makeType(String name, VertexFormat vertexFormat, int drawMode, int bufferSize, RenderType.State renderState) {
//        return makeType(name, vertexFormat, drawMode, bufferSize, false, false, renderState);
//    }
//
//    public static RenderType.Type makeType(String name, VertexFormat vertexFormat, int drawMode, int bufferSize, boolean useDelegate, boolean needsSorting, RenderType.State renderState) {
//        return RenderType.Type.getOrCreate(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, renderState);
//    }
//
//    public void finish(BufferBuilder bufferBuilder, int cameraX, int cameraY, int cameraZ) {
//        if (bufferBuilder.isDrawing()) {
//            if (this.needsSorting) {
//                bufferBuilder.sortVertexData((float)cameraX, (float)cameraY, (float)cameraZ);
//            }
//
//            bufferBuilder.finishDrawing();
//            this.setupRenderState();
//            WorldVertexBufferUploader.draw(bufferBuilder);
//            this.clearRenderState();
//        }
//    }
//
//    public String toString() {
//        return this.name;
//    }
//
//    public static List<RenderType> getBlockRenderTypes() {
//        return ImmutableList.of(getSolid(), getCutoutMipped(), getCutout(), getTranslucent());
//    }
//
//    public int getBufferSize() {
//        return this.bufferSize;
//    }
//
//    public VertexFormat getVertexFormat() {
//        return this.vertexFormat;
//    }
//
//    public int getDrawMode() {
//        return this.drawMode;
//    }
//
//    public Optional<RenderType> getOutline() {
//        return Optional.empty();
//    }
//
//    public boolean getDrawOutline() {
//        return false;
//    }
//
//    public boolean isUseDelegate() {
//        return this.useDelegate;
//    }
//
//    public Optional<RenderType> func_230169_u_() {
//        return this.field_230166_ag_;
//    }
//
//    static {
//        SOLID = makeType("solid", DefaultVertexFormats.BLOCK, 7, 2097152, true, false, RenderType.State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).build(true));
//        CUTOUT_MIPPED = makeType("cutout_mipped", DefaultVertexFormats.BLOCK, 7, 131072, true, false, RenderType.State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED).alpha(HALF_ALPHA).build(true));
//        CUTOUT = makeType("cutout", DefaultVertexFormats.BLOCK, 7, 131072, true, false, RenderType.State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET).alpha(HALF_ALPHA).build(true));
//        TRANSLUCENT = makeType("translucent", DefaultVertexFormats.BLOCK, 7, 262144, true, true, getTranslucentState());
//        TRANSLUCENT_NO_CRUMBLING = makeType("translucent_no_crumbling", DefaultVertexFormats.BLOCK, 7, 262144, false, true, getTranslucentState());
//        LEASH = makeType("leash", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, 7, 256, RenderType.State.getBuilder().texture(NO_TEXTURE).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).build(false));
//        WATER_MASK = makeType("water_mask", DefaultVertexFormats.POSITION, 7, 256, RenderType.State.getBuilder().texture(NO_TEXTURE).writeMask(DEPTH_WRITE).build(false));
//        GLINT = makeType("glint", DefaultVertexFormats.POSITION_TEX, 7, 256, RenderType.State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).build(false));
//        ENTITY_GLINT = makeType("entity_glint", DefaultVertexFormats.POSITION_TEX, 7, 256, RenderType.State.getBuilder().texture(new TextureState(ItemRenderer.RES_ITEM_GLINT, true, false)).writeMask(COLOR_WRITE).cull(CULL_DISABLED).depthTest(DEPTH_EQUAL).transparency(GLINT_TRANSPARENCY).texturing(ENTITY_GLINT_TEXTURING).build(false));
//        LIGHTNING = makeType("lightning", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, RenderType.State.getBuilder().writeMask(COLOR_WRITE).transparency(LIGHTNING_TRANSPARENCY).shadeModel(SHADE_ENABLED).build(false));
//        LINES = makeType("lines", DefaultVertexFormats.POSITION_COLOR, 1, 256, RenderType.State.getBuilder().line(new LineState(OptionalDouble.empty())).layer(PROJECTION_LAYERING).transparency(TRANSLUCENT_TRANSPARENCY).writeMask(COLOR_WRITE).build(false));
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    static final class Type extends RenderType {
//        private static final ObjectOpenCustomHashSet<RenderType.Type> TYPES;
//        private final RenderType.State renderState;
//        private final int hashCode;
//        private final Optional<RenderType> outlineRenderType;
//        private final boolean outline;
//
//        private Type(String nameIn, VertexFormat vertexFormatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, RenderType.State renderStateIn) {
//            super(nameIn, vertexFormatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, () -> {
//                renderStateIn.renderStates.forEach(RenderState::setupRenderState);
//            }, () -> {
//                renderStateIn.renderStates.forEach(RenderState::clearRenderState);
//            });
//            this.renderState = renderStateIn;
//            this.outlineRenderType = renderStateIn.outline == RenderType.OutlineState.AFFECTS_OUTLINE ? renderStateIn.texture.texture().map(RenderType::getOutline) : Optional.empty();
//            this.outline = renderStateIn.outline == RenderType.OutlineState.IS_OUTLINE;
//            this.hashCode = Objects.hash(new Object[]{super.hashCode(), renderStateIn});
//        }
//
//        private static RenderType.Type getOrCreate(String nameIn, VertexFormat vertexFormatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, RenderType.State renderStateIn) {
//            return (RenderType.Type)TYPES.addOrGet(new RenderType.Type(nameIn, vertexFormatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, renderStateIn));
//        }
//
//        public Optional<RenderType> getOutline() {
//            return this.outlineRenderType;
//        }
//
//        public boolean getDrawOutline() {
//            return this.outline;
//        }
//
//        public boolean equals(@Nullable Object object) {
//            return this == object;
//        }
//
//        public int hashCode() {
//            return this.hashCode;
//        }
//
//        static {
//            TYPES = new ObjectOpenCustomHashSet(RenderType.Type.EqualityStrategy.INSTANCE);
//        }
//
//        @OnlyIn(Dist.CLIENT)
//        static enum EqualityStrategy implements Strategy<RenderType.Type> {
//            INSTANCE;
//
//            private EqualityStrategy() {
//            }
//
//            public int hashCode(@Nullable RenderType.Type typeIn) {
//                return typeIn == null ? 0 : typeIn.hashCode;
//            }
//
//            public boolean equals(@Nullable RenderType.Type type1, @Nullable RenderType.Type type2) {
//                if (type1 == type2) {
//                    return true;
//                } else {
//                    return type1 != null && type2 != null ? Objects.equals(type1.renderState, type2.renderState) : false;
//                }
//            }
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static final class State {
//        private final TextureState texture;
//        private final TransparencyState transparency;
//        private final DiffuseLightingState diffuseLighting;
//        private final ShadeModelState shadowModel;
//        private final AlphaState alpha;
//        private final DepthTestState depthTest;
//        private final CullState cull;
//        private final LightmapState lightmap;
//        private final OverlayState overlay;
//        private final FogState fog;
//        private final LayerState layer;
//        private final TargetState target;
//        private final TexturingState texturing;
//        private final WriteMaskState writeMask;
//        private final LineState line;
//        private final RenderType.OutlineState outline;
//        private final ImmutableList<RenderState> renderStates;
//
//        private State(TextureState textureIn, TransparencyState transparencyIn, DiffuseLightingState diffuseLightingIn, ShadeModelState shadowModelIn, AlphaState alphaIn, DepthTestState depthTestIn, CullState cullIn, LightmapState lightmapIn, OverlayState overlayIn, FogState fogIn, LayerState layerIn, TargetState targetIn, TexturingState textureingIn, WriteMaskState writeMaskIn, LineState lineIn, RenderType.OutlineState outlineIn) {
//            this.texture = textureIn;
//            this.transparency = transparencyIn;
//            this.diffuseLighting = diffuseLightingIn;
//            this.shadowModel = shadowModelIn;
//            this.alpha = alphaIn;
//            this.depthTest = depthTestIn;
//            this.cull = cullIn;
//            this.lightmap = lightmapIn;
//            this.overlay = overlayIn;
//            this.fog = fogIn;
//            this.layer = layerIn;
//            this.target = targetIn;
//            this.texturing = textureingIn;
//            this.writeMask = writeMaskIn;
//            this.line = lineIn;
//            this.outline = outlineIn;
//            this.renderStates = ImmutableList.of(this.texture, this.transparency, this.diffuseLighting, this.shadowModel, this.alpha, this.depthTest, this.cull, this.lightmap, this.overlay, this.fog, this.layer, this.target, new RenderState[]{this.texturing, this.writeMask, this.line});
//        }
//
//        public boolean equals(Object object) {
//            if (this == object) {
//                return true;
//            } else if (object != null && this.getClass() == object.getClass()) {
//                RenderType.State renderType$state = (RenderType.State)object;
//                return this.outline == renderType$state.outline && this.renderStates.equals(renderType$state.renderStates);
//            } else {
//                return false;
//            }
//        }
//
//        public int hashCode() {
//            return Objects.hash(this.renderStates, this.outline);
//        }
//
//        public static RenderType.State.Builder getBuilder() {
//            return new RenderType.State.Builder();
//        }
//
//        @OnlyIn(Dist.CLIENT)
//        public static class Builder {
//            private TextureState texture;
//            private TransparencyState transparency;
//            private DiffuseLightingState diffuseLighting;
//            private ShadeModelState shadeModel;
//            private AlphaState alpha;
//            private DepthTestState depthTest;
//            private CullState cull;
//            private LightmapState lightmap;
//            private OverlayState overlay;
//            private FogState fog;
//            private LayerState layer;
//            private TargetState target;
//            private TexturingState texturing;
//            private WriteMaskState writeMask;
//            private LineState line;
//
//            private Builder() {
//                this.texture = RenderState.NO_TEXTURE;
//                this.transparency = RenderState.NO_TRANSPARENCY;
//                this.diffuseLighting = RenderState.DIFFUSE_LIGHTING_DISABLED;
//                this.shadeModel = RenderState.SHADE_DISABLED;
//                this.alpha = RenderState.ZERO_ALPHA;
//                this.depthTest = RenderState.DEPTH_LEQUAL;
//                this.cull = RenderState.CULL_ENABLED;
//                this.lightmap = RenderState.LIGHTMAP_DISABLED;
//                this.overlay = RenderState.OVERLAY_DISABLED;
//                this.fog = RenderState.FOG;
//                this.layer = RenderState.NO_LAYERING;
//                this.target = RenderState.MAIN_TARGET;
//                this.texturing = RenderState.DEFAULT_TEXTURING;
//                this.writeMask = RenderState.COLOR_DEPTH_WRITE;
//                this.line = RenderState.DEFAULT_LINE;
//            }
//
//            public RenderType.State.Builder texture(TextureState textureIn) {
//                this.texture = textureIn;
//                return this;
//            }
//
//            public RenderType.State.Builder transparency(TransparencyState transparencyIn) {
//                this.transparency = transparencyIn;
//                return this;
//            }
//
//            public RenderType.State.Builder diffuseLighting(DiffuseLightingState diffuseLightingIn) {
//                this.diffuseLighting = diffuseLightingIn;
//                return this;
//            }
//
//            public RenderType.State.Builder shadeModel(ShadeModelState shadeModelIn) {
//                this.shadeModel = shadeModelIn;
//                return this;
//            }
//
//            public RenderType.State.Builder alpha(AlphaState alphaIn) {
//                this.alpha = alphaIn;
//                return this;
//            }
//
//            public RenderType.State.Builder depthTest(DepthTestState depthTestIn) {
//                this.depthTest = depthTestIn;
//                return this;
//            }
//
//            public RenderType.State.Builder cull(CullState cullIn) {
//                this.cull = cullIn;
//                return this;
//            }
//
//            public RenderType.State.Builder lightmap(LightmapState lightmapIn) {
//                this.lightmap = lightmapIn;
//                return this;
//            }
//
//            public RenderType.State.Builder overlay(OverlayState overlayIn) {
//                this.overlay = overlayIn;
//                return this;
//            }
//
//            public RenderType.State.Builder fog(FogState fogIn) {
//                this.fog = fogIn;
//                return this;
//            }
//
//            public RenderType.State.Builder layer(LayerState layerIn) {
//                this.layer = layerIn;
//                return this;
//            }
//
//            public RenderType.State.Builder target(TargetState targetIn) {
//                this.target = targetIn;
//                return this;
//            }
//
//            public RenderType.State.Builder texturing(TexturingState texturingIn) {
//                this.texturing = texturingIn;
//                return this;
//            }
//
//            public RenderType.State.Builder writeMask(WriteMaskState writeMaskIn) {
//                this.writeMask = writeMaskIn;
//                return this;
//            }
//
//            public RenderType.State.Builder line(LineState lineIn) {
//                this.line = lineIn;
//                return this;
//            }
//
//            public RenderType.State build(boolean outline) {
//                return this.build(outline ? RenderType.OutlineState.AFFECTS_OUTLINE : RenderType.OutlineState.NONE);
//            }
//
//            public RenderType.State build(RenderType.OutlineState outlineIn) {
//                return new RenderType.State(this.texture, this.transparency, this.diffuseLighting, this.shadeModel, this.alpha, this.depthTest, this.cull, this.lightmap, this.overlay, this.fog, this.layer, this.target, this.texturing, this.writeMask, this.line, outlineIn);
//            }
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    static enum OutlineState {
//        NONE,
//        IS_OUTLINE,
//        AFFECTS_OUTLINE;
//
//        private OutlineState() {
//        }
//    }
//}
