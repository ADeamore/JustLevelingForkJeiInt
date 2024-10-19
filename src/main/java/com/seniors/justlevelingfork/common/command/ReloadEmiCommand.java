package com.seniors.justlevelingfork.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.seniors.justlevelingfork.common.command.arguments.AptitudeArgument;
import com.seniors.justlevelingfork.integration.EmiIntegration;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ReloadEmiCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("emireload").executes(ReloadEmiCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        if(Minecraft.getInstance().level.isClientSide) {
            EmiIntegration.updatelist();
        }
        return 0;
    }
}
