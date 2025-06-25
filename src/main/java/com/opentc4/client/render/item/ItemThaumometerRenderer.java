package com.opentc4.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

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

    private void renderQuad(float size, float alpha) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-size, size, 0, 0, 1);
        tessellator.addVertexWithUV(size, size, 0, 1, 1);
        tessellator.addVertexWithUV(size, -size, 0, 1, 0);
        tessellator.addVertexWithUV(-size, -size, 0, 0, 0);
        tessellator.draw();

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;

        float partialTicks = 0F;
        if (data.length > 1 && data[1] instanceof Float) {
            partialTicks = (Float) data[1];
        }

        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(TEXTURE_LOCATION);

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            RenderPlayer renderPlayer = (RenderPlayer) RenderManager.instance.getEntityRenderObject(mc.thePlayer);

            mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());

            for (int side = 0; side < 2; side++) {
                int direction = side * 2 - 1; // -1 for left, +1 for right

                GL11.glPushMatrix();

                // Arm positioning
                GL11.glTranslatef(0.0F, -0.6F, 1.1F * direction);
                GL11.glRotatef(-45 * direction, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-65 * direction, 0.0F, 1.0F, 0.0F);

                // Failsafe to make sure scale is correct
                GL11.glScalef(1.0F, 1.0F, 1.0F);

                // Render the arms with player's skin
                renderPlayer.renderFirstPersonArm(mc.thePlayer);

                GL11.glPopMatrix();
            }

            // Basic positioning
            GL11.glTranslatef(0.8F, 0.6F, -0.8F);
            GL11.glRotatef(135.0F, 0.0F, -1.0F, 0.0F);

            // Smooth player head movement interpolation
            float prevPitch = mc.thePlayer.prevRotationPitch;
            float currPitch = mc.thePlayer.rotationPitch;
            float prevYaw = mc.thePlayer.prevRotationYaw;
            float currYaw = mc.thePlayer.rotationYaw;

            float interpPitch = prevPitch + (currPitch - prevPitch) * partialTicks;
            float interpYaw = prevYaw + (currYaw - prevYaw) * partialTicks;

            GL11.glRotatef((currPitch - interpPitch) * 0.1F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef((currYaw - interpYaw) * 0.1F, 0.0F, 1.0F, 0.0F);

            GL11.glScalef(1F, 1F, 1F);

        } else if (type == ItemRenderType.EQUIPPED) {
            GL11.glTranslatef(1.6F, 0.3F, 2.0F);
            GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
            GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
            GL11.glScalef(1F, 1F, 1F);

        } else if (type == ItemRenderType.INVENTORY) {
            GL11.glRotatef(60.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(30.0F, 0.0F, 0.0F, -1.0F);
            GL11.glRotatef(248.0F, 0.0F, -1.0F, 0.0F);
            GL11.glScalef(0.5F, 0.5F, 0.5F);

        } else if (type == ItemRenderType.ENTITY) {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
        }

        model.renderAll();

        // scan screen :)
        mc.renderEngine.bindTexture(new ResourceLocation("opentc4", "textures/models/item/scanscreen.png"));
        GL11.glPushMatrix();
        GL11.glScalef(0.75f, 0.75f, 0.75f);

        if (type == ItemRenderType.INVENTORY) {
            GL11.glScalef(0.75F, 0.75F, 0.75F);
        }

        GL11.glTranslatef(0.0F, 0.11F, 0.0F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);


        // Make a quad (defined higher up)
        renderQuad(2.5F, 1.0F);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
