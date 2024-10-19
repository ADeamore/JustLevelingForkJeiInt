package com.seniors.justlevelingfork.handler;

import com.google.gson.GsonBuilder;
import com.seniors.justlevelingfork.JustLevelingFork;
import com.seniors.justlevelingfork.config.Configuration;
import com.seniors.justlevelingfork.config.LockItemListGroup;
import com.seniors.justlevelingfork.config.models.LockEnchant;
import com.seniors.justlevelingfork.config.models.LockItem;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class HandlerLockEnchantsConfig {

    public static ConfigClassHandler<HandlerLockEnchantsConfig> HANDLER = ConfigClassHandler.createBuilder(HandlerLockEnchantsConfig.class)
            .id(new ResourceLocation(JustLevelingFork.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(Configuration.getAbsoluteDirectory().resolve("justleveling-fork.lockEnchants.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "Lock enchant list")
    @ListGroup(controllerFactory = LockItemListGroup.class, valueFactory = LockItemListGroup.class, addEntriesToBottom = true)
    public List<LockEnchant> lockEnchantList = List.of();
}
