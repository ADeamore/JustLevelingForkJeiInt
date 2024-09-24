package com.seniors.justlevelingfork.integration;

import com.seniors.justlevelingfork.common.capability.AptitudeCapability;
import dev.emi.emi.api.*;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.runtime.EmiReloadManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModList;
import dev.emi.emi.api.stack.EmiStack;

import java.util.*;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {

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

        AptitudeCapability provider = AptitudeCapability.get(Minecraft.getInstance().player);

        //loop thru existing recipes
        List<EmiStack> stacklist = EmiApi.getIndexStacks();
        Iterator<EmiStack> stackiterator = stacklist.iterator();
        while(stackiterator.hasNext()){
            EmiStack currentstack = stackiterator.next();

            if(!provider.canUseItemClient(currentstack.getItemStack())){
                emiRegistry.removeEmiStacks(currentstack);
            }
        }
    }

    public static void updatelist() {
        //find a way to reload EMI
        EmiReloadManager.reload();
    }
}
