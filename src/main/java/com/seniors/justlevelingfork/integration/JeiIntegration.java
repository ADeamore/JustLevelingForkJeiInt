package com.seniors.justlevelingfork.integration;

import com.seniors.justlevelingfork.JustLevelingFork;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import mezz.jei.api.runtime.IJeiRuntime;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

@JeiPlugin
public class JeiIntegration implements IModPlugin {

    IJeiRuntime runtime;
    public static final ResourceLocation ID = new ResourceLocation(JustLevelingFork.MOD_ID, "jei");

    public static boolean isModLoaded() {
        if(ModList.get().isLoaded("jei")){
            System.out.println("JEI Integration Beginning!");
            return true;
        }else{
            return false;
        }
    }

    Collection<?> ingredientlist;

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime r){
        runtime = r; //this is how kubejs gets the runtime data but for some reason its effectively empty
        System.out.println("Recipe Removal Service has successfully hooked into JEI");
    }

    @SubscribeEvent
    public void playerrightclick(PlayerInteractEvent.RightClickEmpty event){ //for testing runtime stuff. Runtime somehow ends up null by the time this code runs
        Collection<IIngredientType<?>> alltypes = runtime.getIngredientManager().getRegisteredIngredientTypes();
        alltypes.forEach(type ->{
            if(type.getUid().equals("ItemStack")){
                Collection<?> allIngredients = runtime.getIngredientManager().getAllIngredients(type);
            }
        });

    }

}
