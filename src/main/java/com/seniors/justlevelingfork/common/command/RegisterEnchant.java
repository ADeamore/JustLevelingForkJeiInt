package com.seniors.justlevelingfork.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.seniors.justlevelingfork.common.command.arguments.AptitudeArgument;
import com.seniors.justlevelingfork.config.models.LockEnchant;
import com.seniors.justlevelingfork.config.models.LockItem;
import com.seniors.justlevelingfork.handler.HandlerLockEnchantsConfig;
import com.seniors.justlevelingfork.handler.HandlerLockItemsConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Stream;

public class RegisterEnchant {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((Commands.literal("registerenchant")
                .requires((source) -> source.hasPermission(2)))
                .then(Commands.argument("aptitude", AptitudeArgument.getArgument())
                        .then(Commands.argument("level", IntegerArgumentType.integer())
                                .executes(RegisterEnchant::execute))
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        if (command.getSource().getEntity() instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            if (stack == ItemStack.EMPTY || stack.isEmpty()) {
                player.sendSystemMessage(Component.literal("No enchantment detected in main hand!"));
                return Command.SINGLE_SUCCESS;
            }

            CompoundTag temp = stack.serializeNBT();

            if(!temp.contains("tag")){
                player.sendSystemMessage(Component.literal("No nbt data detected in main hand!"));
                player.sendSystemMessage(Component.literal("Try using an enchanted book!"));
                return Command.SINGLE_SUCCESS;
            }

            CompoundTag tag = temp.getCompound("tag");
            
            if(!tag.contains("StoredEnchantments")){
                player.sendSystemMessage(Component.literal("No enchantment data detected in main hand!"));
                player.sendSystemMessage(Component.literal("This only works with an enchanted book!"));
                return Command.SINGLE_SUCCESS;
            }

            String enchantmentstring = tag.get("StoredEnchantments").toString().replace("\"","#").replace("]","").replace("[","");

            String[] stringarray = enchantmentstring.split("},");
            if(Arrays.stream(stringarray).count() > 1) {
                player.sendSystemMessage(Component.literal("More than one enchant detected."));
                player.sendSystemMessage(Component.literal("This only works with singularly enchanted books!"));
                return Command.SINGLE_SUCCESS;
            }

            String aptitudeName = command.getArgument("aptitude", String.class);
            Integer level = command.getArgument("level", Integer.class);

            List<LockEnchant> enchantstream = HandlerLockEnchantsConfig.HANDLER.instance().lockEnchantList;
            Optional<LockEnchant> optionalLockEnchant = enchantstream.stream().filter(c -> c.Enchant.equalsIgnoreCase(enchantmentstring)).findFirst();
            if (optionalLockEnchant.isPresent()) {
                LockEnchant lockEnchant = optionalLockEnchant.get();
                int index = HandlerLockEnchantsConfig.HANDLER.instance().lockEnchantList.indexOf(lockEnchant);
                if (level < 1) {
                    if (lockEnchant.Aptitudes.size() <= 1) {
                        HandlerLockEnchantsConfig.HANDLER.instance().lockEnchantList.remove(index);
                        player.sendSystemMessage(Component.literal("Removing item from lockEnchantList..."));
                    }
                    else {
                        Optional<LockEnchant.AptitudeLevel> aptitude = lockEnchant.Aptitudes.stream().filter(c -> c.Aptitude.toString().equalsIgnoreCase(aptitudeName)).findFirst();
                        aptitude.ifPresent(value -> lockEnchant.Aptitudes.remove(value));

                        HandlerLockEnchantsConfig.HANDLER.instance().lockEnchantList.set(index, lockEnchant);
                        player.sendSystemMessage(Component.literal("Removing aptitude from item..."));
                    }

                    return Command.SINGLE_SUCCESS;
                }

                lockEnchant.Aptitudes.stream().filter(c -> c.Aptitude.toString().equalsIgnoreCase(aptitudeName)).findFirst().ifPresent(value -> lockEnchant.Aptitudes.remove(value));

                lockEnchant.Aptitudes.add(new LockEnchant.AptitudeLevel(aptitudeName, level));

                HandlerLockEnchantsConfig.HANDLER.instance().lockEnchantList.set(index, lockEnchant);
                HandlerLockEnchantsConfig.HANDLER.save();

                player.sendSystemMessage(Component.literal("Item already in lockEnchantList, adding extra aptitude..."));
                return Command.SINGLE_SUCCESS;
            }

            LockEnchant lockEnchant = new LockEnchant(enchantmentstring);
            lockEnchant.Aptitudes = new ArrayList<>();
            lockEnchant.Aptitudes.add(new LockEnchant.AptitudeLevel(aptitudeName, level));

            HandlerLockEnchantsConfig.HANDLER.instance().lockEnchantList.add(lockEnchant);
            HandlerLockEnchantsConfig.HANDLER.save();

            player.sendSystemMessage(Component.literal("Item added into lockEnchantList..."));
        }

        return Command.SINGLE_SUCCESS;
    }
}
