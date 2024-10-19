package com.seniors.justlevelingfork.integration;

import com.seniors.justlevelingfork.JustLevelingClient;
import com.seniors.justlevelingfork.client.screen.OverlayNewItemsScreen;
import com.seniors.justlevelingfork.common.capability.AptitudeCapability;
import dev.emi.emi.api.*;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.runtime.EmiReloadManager;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {
    public static final Collection<EmiStack> oldstack = new ArrayList<EmiStack>();
    public static final Collection<EmiStack> newstack = new ArrayList<EmiStack>();
    public static boolean init = true;
    public static boolean kickstart = false;
    
    public EmiIntegration(){
    }

    public static boolean isModLoaded() {
        if(ModList.get().isLoaded("emi")){
            System.out.println("EMI Integration Beginning!");
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void register(EmiRegistry emiRegistry) {

        if(Minecraft.getInstance().player.isCreative()) return;

        AptitudeCapability provider = AptitudeCapability.get(Minecraft.getInstance().player);

        newstack.clear();

        //loop thru existing recipes
        List<EmiStack> stacklist = EmiApi.getIndexStacks();
        Iterator<EmiStack> stackiterator = stacklist.iterator();
        while(stackiterator.hasNext()){
            EmiStack currentstack = stackiterator.next();
            boolean removed = oldstack.contains(currentstack);

            if(!provider.canUseItemClient(currentstack.getItemStack())){
                emiRegistry.removeEmiStacks(currentstack);
                removed = true;
            }else {
                //handle enchantment stuff here
                CompoundTag temp = currentstack.getItemStack().serializeNBT();
                if (temp.contains("tag")) {
                    CompoundTag tag = temp.getCompound("tag");
                    if (tag.contains("StoredEnchantments")) {
                        String enchantmentstring = tag.get("StoredEnchantments").toString().replace("\"", "#").replace("]", "").replace("[", "");
                        if (!provider.canUseEnchantClient(enchantmentstring)) {
                            emiRegistry.removeEmiStacks(currentstack);
                            removed = true;
                        }
                    }
                }
            }
            if(!removed){
                if (init) {
                    oldstack.add(currentstack);
                } else {
                    newstack.add(currentstack);
                    oldstack.add(currentstack);
                }
            }
        }
        if(!init){
            //draw the gui
            kickstart = true;
        }else{
            init = false;
        }
    }

    public static void updatelist() {
        //find a way to reload EMI
        EmiReloadManager.reload();
    }

    @SubscribeEvent
    public void tickevent(TickEvent.ClientTickEvent event){
        if(kickstart){
            JustLevelingClient.client.setScreen(new OverlayNewItemsScreen());
            kickstart = false;
        }
    }
}
