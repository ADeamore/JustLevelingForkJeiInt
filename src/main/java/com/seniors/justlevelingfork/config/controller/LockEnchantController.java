package com.seniors.justlevelingfork.config.controller;

import com.seniors.justlevelingfork.config.models.LockEnchant;
import com.seniors.justlevelingfork.config.models.LockItem;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;

public class LockEnchantController extends AbstractDropdownController<LockEnchant> {

    public LockEnchantController(Option<LockEnchant> option){
        super(option);
    }

    @Override
    public String getString() {
        return option.pendingValue().toString();
    }

    @Override
    public void setFromString(String value) {
        option.requestSet(LockEnchant.getLockEnchantFromString(value, option.pendingValue()));
    }
}
