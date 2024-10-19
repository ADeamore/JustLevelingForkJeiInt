package com.seniors.justlevelingfork.config.controller;

import com.seniors.justlevelingfork.config.models.LockEnchant;
import com.seniors.justlevelingfork.config.models.LockItem;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;

public class LockEnchantControllerBuilderImpl extends AbstractControllerBuilderImpl<LockEnchant> implements LockEnchantControllerBuilder {
    public LockEnchantControllerBuilderImpl(Option<LockEnchant> option) {
        super(option);
    }

    @Override
    public Controller<LockEnchant> build() {
        return new LockEnchantController(option);
    }
}
