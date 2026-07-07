package com.aquamancer.czlib.mixin;

import com.aquamancer.czlib.trinket.AbilitySelectionParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class AbilitySelectionMixin {
    @Inject(at = @At("HEAD"), cancellable = true, method = "onSlotClick(IILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V")
    private void onSlotClick(int slot, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        AbilitySelectionParser.onSlotClicked(slot, ((ScreenHandler)(Object) this), button, actionType);
//        MinecraftClient.getInstance().player.sendMessage(Text.literal("slot clicked="+slot+", size=" + self.getStacks().size()));
    }
}
