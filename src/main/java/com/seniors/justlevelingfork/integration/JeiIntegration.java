package com.seniors.justlevelingfork.integration;

import com.seniors.justlevelingfork.JustLevelingFork;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import mezz.jei.api.runtime.IJeiRuntime;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

@JeiPlugin
public class JeiIntegration implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation(JustLevelingFork.MOD_ID, "jei");
    Collection<String> hiddenIngredients = Collections.emptyList();
    public IJeiRuntime runtime;

    public static boolean isModLoaded() {
        if(ModList.get().isLoaded("jei")){
            System.out.println("JEI Integration Beginning!");
            return true;
        }else{
            return false;
        }
    }


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
    public void onPlayerSpawnEvent(EntityJoinLevelEvent event){ //need something that triggers after this point, this triggers too early
        Player player = Minecraft.getInstance().player;
        if(player == null) return;
        if(event.getEntity().is(player)){
            if(this.runtime == null) return;
            HandleListReset();
        }
    }

    public void HandleListReset(){

        //get the manager, player and aptitude levels
        IIngredientManager ingredientManager = this.runtime.getIngredientManager();
        Player player = Minecraft.getInstance().player;
        AptitudeCapability provider = AptitudeCapability.get(player);

        // loop thru different stack types (itemtype, and fluidtype)
        Iterator<IIngredientType<?>> ingtypesiterator = ingredientManager.getRegisteredIngredientTypes().iterator();
        while(ingtypesiterator.hasNext()){

            //loop thru each entry in the list
            IIngredientType<?> ingtype = ingtypesiterator.next();
            String temp = ingtype.getUid();
            Iterator<?> itemstackiterator = ingredientManager.getAllIngredients(ingtype).iterator();
            while(itemstackiterator.hasNext()){

                //get the actual item "modname:itemname" string (wow this is overcomplicated)
                Object itemtypeobject = itemstackiterator.next();
                Optional<? extends ITypedIngredient<?>> itemStackOptional = ingredientManager.createTypedIngredient(itemtypeobject);
                ItemStack itemStack = itemStackOptional.get().getItemStack().get();
                String itemname = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString();

                //check if you can use the item
                if (!provider.canUseSpecificID(player,itemname)) {

                    //print that the item was hidden
                    System.out.println("JLFork hid item: " + itemname);
                    //record the item to a collection
                    hiddenIngredients.add(itemname); //err for some fuckin reason? i do not understand. something about immutables

                    //somehow set the visibility here? :(
                }
            };
        };
    }

    /*
    @SubscribeEvent
    public void handleplayerlevelup(LevelUpEvent event){
        Player player  = event.getPlayer();

    }
     */
}
