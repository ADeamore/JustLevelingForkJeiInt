package com.seniors.justlevelingfork.block;

import com.seniors.justlevelingfork.JustLevelingFork;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, JustLevelingFork.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.BONFIRE_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/bonfire_block")));
    }
}
