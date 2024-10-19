package com.seniors.justlevelingfork.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.seniors.justlevelingfork.JustLevelingFork;
import com.seniors.justlevelingfork.client.gui.DrawTabs;
import com.seniors.justlevelingfork.handler.HandlerResources;
import com.seniors.justlevelingfork.integration.EmiIntegration;
import com.seniors.justlevelingfork.registry.RegistryAptitudes;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.awt.MouseInfo.getPointerInfo;

@OnlyIn(Dist.CLIENT)
public class OverlayNewItemsScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui." + JustLevelingFork.MOD_ID + ".unlocked_items_screen");

    private final int imageWidth, imageHeight;
    private int leftPos, topPos;
    private Collection<EmiStack> itemlist = new ArrayList<EmiStack>();
    private int scrolling = 0;
    private boolean canscroll = false;


    public OverlayNewItemsScreen() {
        super(TITLE);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        itemlist = EmiIntegration.newstack;
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {

        this.minecraft = Minecraft.getInstance();


        int sx = graphics.guiWidth()/2 - this.imageWidth/2;
        int sy = graphics.guiHeight()/2 - this.imageHeight/2;
        int ex = sx + this.imageWidth;
        int ey = sy + this.imageHeight;
        int cx = sx;
        int cy = sy;

        renderBackground(graphics);
        graphics.renderOutline(sx,sy,this.imageWidth,this.imageHeight, Color.WHITE.getRGB());
        graphics.drawString(this.minecraft.font,"Newly Unlocked Items:",sx,sy - 16,Color.WHITE.getRGB());
        if(canscroll){
            this.addRenderableWidget(Button.builder(Component.literal("Down"),this::scrolldown).pos(ex,ey - 16).size(32,16).build());
            this.addRenderableWidget(Button.builder(Component.literal("Up"),this::scrollup).pos(ex,sy).size(16,16).build());
        }


        Iterator<EmiStack> stackiterator = itemlist.iterator();
        while(stackiterator.hasNext()){
            ItemStack temp = stackiterator.next().getItemStack();
            int my = cy - (scrolling * 18);
            if(!(my < sy) & !(my > ey - 18)) {
                graphics.renderItem(temp, cx + 3, my + 5);
                if (pMouseX > cx & pMouseX < cx + 18 & pMouseY > my & pMouseY < my + 18) {
                    List<Component> lines = temp.getTooltipLines(this.minecraft.player, TooltipFlag.NORMAL);
                    graphics.renderComponentTooltip(this.minecraft.font, lines, pMouseX, pMouseY);
                }
            }else{
                canscroll = true;
            }
            cx = cx + 18;
            if(cx > ex - 18){
                cx = sx;
                cy = cy + 18;
            }
        }

        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    private void scrollup(Button button) {
        scrolling = Mth.clamp(scrolling - 1, 0, scrolling);
    }

    private void scrolldown(Button button) {
        scrolling++;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
