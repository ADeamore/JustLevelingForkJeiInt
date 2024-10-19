package com.seniors.justlevelingfork.block;
import com.seniors.justlevelingfork.client.screen.JustLevelingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class bonfireblock extends Block {

    public bonfireblock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide()) {
            Minecraft.getInstance().setScreen(new JustLevelingScreen());
            pLevel.playSound(pPlayer,pPos,SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 7;
    }
}
