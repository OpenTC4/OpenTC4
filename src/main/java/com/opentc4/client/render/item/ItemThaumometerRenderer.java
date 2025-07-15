package com.opentc4.client.render.item;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
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

    private float smoothEquip = 1.0F;
    private float pitch = 0f;
    private float yaw = 0f;

    private static Field equippedField;
    private static Field prevEquippedField;

    public ItemThaumometerRenderer() {
        model = AdvancedModelLoader.loadModel(MODEL_LOCATION);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    private void smoothRotation(float targetPitch, float targetYaw) {
        pitch += (targetPitch - pitch) * 0.1f;
        yaw += (targetYaw - yaw) * 0.1f;
    }

    private void smoothEquipProgress(float targetProgress) {
        smoothEquip += (targetProgress - smoothEquip) * 0.1f;
    }

    private void initReflection() {
        if (equippedField != null && prevEquippedField != null) return;

        try {
            equippedField = ItemRenderer.class.getDeclaredField("equippedProgress");
            prevEquippedField = ItemRenderer.class.getDeclaredField("prevEquippedProgress");
            equippedField.setAccessible(true);
            prevEquippedField.setAccessible(true);
        } catch (Exception ignored) {
            System.err.println("[OpenTC4] Something failed, Thaumometer animation may be choppy.");
        }
    }

    private void drawOverlay(Minecraft mc, ResourceLocation texture, float alpha) {
        GL11.glPushMatrix();
        GL11.glScalef(2.8F, 2.8F, 2.8F);
        mc.getTextureManager()
            .bindTexture(texture);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);

        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(-0.5, 0, -0.5, 0, 0);
        t.addVertexWithUV(0.5, 0, -0.5, 1, 0);
        t.addVertexWithUV(0.5, 0, 0.5, 1, 1);
        t.addVertexWithUV(-0.5, 0, 0.5, 0, 1);
        t.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        EntityPlayerSP playerSP = (EntityPlayerSP) player;

        float partialTicks = data.length > 1 && data[1] instanceof Float ? (Float) data[1] : 0F;

        initReflection();

        float equipped = 1.0F;
        float prevEquipped = 1.0F;
        try {
            if (equippedField != null && prevEquippedField != null) {
                equipped = equippedField.getFloat(mc.entityRenderer.itemRenderer);
                prevEquipped = prevEquippedField.getFloat(mc.entityRenderer.itemRenderer);
            }
        } catch (Exception ignored) {}

        float equipProgress = prevEquipped + (equipped - prevEquipped) * partialTicks;
        smoothEquipProgress(equipProgress);

        float pitchInterp = playerSP.prevRenderArmPitch
            + (playerSP.renderArmPitch - playerSP.prevRenderArmPitch) * partialTicks;
        float yawInterp = playerSP.prevRenderArmYaw
            + (playerSP.renderArmYaw - playerSP.prevRenderArmYaw) * partialTicks;
        smoothRotation(pitchInterp, yawInterp);

        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON
            && mc.thePlayer.getEntityId() == mc.renderViewEntity.getEntityId()
            && mc.gameSettings.thirdPersonView == 0) {
            GL11.glTranslatef(1F, 0.75F, -1F);
            GL11.glRotatef(135.0F, 0.0F, -1.0F, 0.0F);
            GL11.glRotatef((player.rotationPitch - pitch) * 0.1F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef((player.rotationYaw - yaw) * 0.1F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.56F, 0.55F + (1.0F - smoothEquip) * 1.5F, 0.72F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, -0.72F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);

            GL11.glPushMatrix();
            GL11.glScalef(5.0F, 5.0F, 5.0F);
            mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());

            for (int i = 0; i < 2; i++) {
                int mult = i * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, -0.6F, 1.1F * mult);
                GL11.glRotatef(-45 * mult, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-65 * mult, 0.0F, 1.0F, 0.0F);
                Render render = RenderManager.instance.getEntityRenderObject(player);
                if (render instanceof RenderPlayer) {
                    ((RenderPlayer) render).renderFirstPersonArm(player);
                }
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();

            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.4F, -0.4F, 0.0F);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
        } else {
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
            .bindTexture(TEXTURE_LOCATION);
        model.renderAll();

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.11F, 0.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);

        drawOverlay(
            mc,
            new ResourceLocation("opentc4:textures/models/item/scanscreen.png"),
            190 + (int) (MathHelper.sin(player.ticksExisted + mc.theWorld.rand.nextFloat() * 2) * 10.0F + 10.0F)
                / 255F);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
