package dev.rdh.ntl.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.simibubi.create.infrastructure.config.CTrains;

@Mixin(CTrains.class)
public class CTrainsMixin {
	@ModifyExpressionValue(method = "<init>", at = @At(value = "CONSTANT", args = "intValue=128", ordinal = 0), remap = false)
	private int bigCurves(int original) {
		return Integer.MAX_VALUE;
	}
}
