package com.seniors.justlevelingfork.config.controller;

import com.seniors.justlevelingfork.config.models.LockEnchant;
import com.seniors.justlevelingfork.config.models.LockItem;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;

public interface LockEnchantControllerBuilder extends ControllerBuilder<LockEnchant> {
    static LockEnchantControllerBuilder create(Option<LockEnchant> option){
        return new LockEnchantControllerBuilderImpl(option);
    }
}
