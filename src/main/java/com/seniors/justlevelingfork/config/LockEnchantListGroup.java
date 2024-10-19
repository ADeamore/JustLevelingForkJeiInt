package com.seniors.justlevelingfork.config;

import com.seniors.justlevelingfork.config.controller.LockEnchantControllerBuilder;
import com.seniors.justlevelingfork.config.controller.LockItemControllerBuilder;
import com.seniors.justlevelingfork.config.models.LockEnchant;
import com.seniors.justlevelingfork.config.models.LockItem;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigField;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.autogen.OptionAccess;

import java.util.List;

public class LockEnchantListGroup implements ListGroup.ValueFactory<LockEnchant>, ListGroup.ControllerFactory<LockEnchant> {

    @Override
    public ControllerBuilder<LockEnchant> createController(ListGroup annotation, ConfigField<List<LockEnchant>> field, OptionAccess storage, Option<LockEnchant> option) {
        return LockEnchantControllerBuilder.create(option);
    }

    @Override
    public LockEnchant provideNewValue() {
        return new LockEnchant();
    }
}
