package com.opentc4.client.render.item;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemThaumometerRenderer implements IItemRenderer {

    private static final ResourceLocation MODEL_LOCATION = new ResourceLocation(
        "opentc4",
        "textures/models/item/thaumometer.obj");
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(
        "opentc4",
        "textures/models/item/thaumometer.png");

    private final IModelCustom model;

    public ItemThaumometerRenderer() {
        model = AdvancedModelLoader.loadModel(MODEL_LOCATION);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true; // Render in all contexts
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true; // Allow helpers for smooth rendering
    }

    private float smoothEquipProgress = 1.0F;
    private float smoothPitch = 0f;
    private float smoothYaw = 0f;

    private void updateSmoothRotation(float targetPitch, float targetYaw) {
        float smoothing = 0.1f; // adjust for speed

        smoothPitch += (targetPitch - smoothPitch) * smoothing;
        smoothYaw += (targetYaw - smoothYaw) * smoothing;
    }

    private float getSmoothPitch() {
        return smoothPitch;
    }

    private float getSmoothYaw() {
        return smoothYaw;
    }

    private void updateEquipProgress(float targetProgress) {
        smoothEquipProgress += (targetProgress - smoothEquipProgress) * 0.1F; // adjust smoothing speed here
    }

    private float getSmoothEquipProgress() {
        return smoothEquipProgress;
    }

    private static Field equippedField = null;
    private static Field prevEquippedField = null;
    private static boolean reflectionInitialized = false;
    private static boolean reflectionFailed = false;

    private void tryInitializeReflection() {
        if (reflectionInitialized || reflectionFailed) return;

        try {
            equippedField = ItemRenderer.class.getDeclaredField("equippedProgress");
            prevEquippedField = ItemRenderer.class.getDeclaredField("prevEquippedProgress");
            equippedField.setAccessible(true);
            prevEquippedField.setAccessible(true);
            reflectionInitialized = true;
        } catch (Exception e) {
            reflectionFailed = true;
            System.err.println("[OpenTC4] Thaumometer smoothing disabled. Here be dragons!");
        }
    }

    private void renderQuadCenteredFromTexture(Minecraft mc, ResourceLocation texture, float scale, float r, float g,
        float b, int alpha, int blendMode) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        mc.getTextureManager()
            .bindTexture(texture);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, blendMode);
        GL11.glColor4f(r, g, b, alpha / 255.0F);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-0.5, 0, -0.5, 0, 0);
        tessellator.addVertexWithUV(0.5, 0, -0.5, 1, 0);
        tessellator.addVertexWithUV(0.5, 0, 0.5, 1, 1);
        tessellator.addVertexWithUV(-0.5, 0, 0.5, 0, 1);
        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        Minecraft mc = Minecraft.getMinecraft();
        int renderedViewEntityId = 0;
        int playerEntityId = 0;

        if (type == ItemRenderType.EQUIPPED) {
            renderedViewEntityId = mc.renderViewEntity.getEntityId();
            playerEntityId = ((EntityLivingBase) data[1]).getEntityId();
        }

        EntityPlayer player = mc.thePlayer;

        float partialTicks = 0F;
        if (data.length > 1 && data[1] instanceof Float) {
            partialTicks = (Float) data[1];
        }

        float scaleBase = 0.8F;
        EntityPlayerSP playerSP = (EntityPlayerSP) player;

        tryInitializeReflection();

        float equippedProgress = 1.0F;
        float prevEquippedProgress = 1.0F;

        if (reflectionInitialized) {
            try {
                equippedProgress = equippedField.getFloat(mc.entityRenderer.itemRenderer);
                prevEquippedProgress = prevEquippedField.getFloat(mc.entityRenderer.itemRenderer);
            } catch (Exception e) {
                reflectionFailed = true;
                System.err.println("[OpenTC4] Reflection failed mid-render. Here be dragons!");
            }
        }

        float equipProgressInterpolated = prevEquippedProgress
            + (equippedProgress - prevEquippedProgress) * partialTicks;
        updateEquipProgress(equipProgressInterpolated);
        float smoothProgress = getSmoothEquipProgress();
        float pitchInterpolated = playerSP.prevRenderArmPitch
            + (playerSP.renderArmPitch - playerSP.prevRenderArmPitch) * partialTicks;
        float yawInterpolated = playerSP.prevRenderArmYaw
            + (playerSP.renderArmYaw - playerSP.prevRenderArmYaw) * partialTicks;
        updateSmoothRotation(pitchInterpolated, yawInterpolated);
        float smoothPitch = getSmoothPitch();
        float smoothYaw = getSmoothYaw();

        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON && playerEntityId == renderedViewEntityId
            && mc.gameSettings.thirdPersonView == 0) {
            GL11.glTranslatef(1F, 0.75F, -1F);
            GL11.glRotatef(135.0F, 0.0F, -1.0F, 0.0F);

            GL11.glRotatef((player.rotationPitch - smoothPitch) * 0.1F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef((player.rotationYaw - smoothYaw) * 0.1F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(
                -0.7F * scaleBase,
                -(-0.65F * scaleBase) + (1.0F - smoothProgress) * 1.5F,
                0.9F * scaleBase);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, -0.9F * scaleBase);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);

            GL11.glPushMatrix();
            GL11.glScalef(5.0F, 5.0F, 5.0F);
            mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());

            for (int i = 0; i < 2; i++) {
                int sideMultiplier = i * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, -0.6F, 1.1F * sideMultiplier);
                GL11.glRotatef(-45 * sideMultiplier, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-65 * sideMultiplier, 0.0F, 1.0F, 0.0F);

                Render render = RenderManager.instance.getEntityRenderObject(player);
                if (render instanceof RenderPlayer) {
                    ((RenderPlayer) render).renderFirstPersonArm(player);
                }

                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();

            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.4F, -0.4F, 0.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(2.0F, 2.0F, 2.0F);

        } else {
            // Third person or inventory render
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if (type == ItemRenderType.EQUIPPED) {
                GL11.glTranslatef(1.6F, 0.3F, 2.0F);
                GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
                GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
            } else if (type == ItemRenderType.INVENTORY) {
                GL11.glRotatef(60.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
                GL11.glRotatef(248.0F, 0.0F, -1.0F, 0.0F);
            } else if (type == ItemRenderType.ENTITY) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
        }

        mc.getTextureManager()
            .bindTexture(new ResourceLocation("opentc4:textures/models/item/thaumometer.png"));
        this.model.renderAll();

        // Render the screen quad
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.11F, 0.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        // GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);

        renderQuadCenteredFromTexture(
            mc,
            new ResourceLocation("opentc4:textures/models/item/scanscreen.png"),
            2.8F,
            1.0F,
            1.0F,
            1.0F,
            190 + (int) (MathHelper.sin(player.ticksExisted + mc.theWorld.rand.nextFloat() * 2) * 10.0F + 10.0F),
            GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
