package dev.rdh.ntl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

import com.simibubi.create.content.trains.track.TrackPlacement;

import dev.rdh.ntl.Util;
import dev.rdh.ntl.config.NTLConfigs;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

@SuppressWarnings("unused")
@Mixin(value = TrackPlacement.class, priority = 2000)
public abstract class TrackPlacementMixin {
	@ModifyExpressionValue(method = "tryConnect", at = @At(value = "CONSTANT", args = "doubleValue=7.0", ordinal = 0))
	private static double setMinTurnSize(double original) {
		return Util.orElse(NTLConfigs.server.minTurnSize, original);
	}

	@ModifyExpressionValue(method = "tryConnect", at = @At(value = "CONSTANT", args = "doubleValue=3.25", ordinal = 0))
	private static double setMinTurn45Size(double original) {
		return Util.orElse(NTLConfigs.server.min45TurnSize, original);
	}

	@WrapOperation(
		method = "tryConnect",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/Mth;equal(DD)Z",
			ordinal = 0,
			slice = "getCenterOf"
		),
		slice = @Slice(
			from = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/utility/VecHelper;getCenterOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;", ordinal = 0),
			id = "getCenterOf"
		)
	)
	private static boolean allowSlopeSTurns(double x, double y, Operation<Boolean> original) {
		return Util.orElse(NTLConfigs.server.allowSlopeSTurns, original.call(x, y));
	}

	@ModifyVariable(
		method = "tryConnect",
		at = @At(value = "LOAD", ordinal = 3),
		name = "skipCurve",
		index = 28
	)
	private static boolean fixSlopeChecking(boolean original) {
		if(original) {
			return true;
		}

		return Util.orElse(NTLConfigs.server.allowSlopeSTurns, false);
	}

	@ModifyVariable(
		method = "tryConnect",
		at = @At(value = "LOAD", ordinal = 2),
		name = "skipCurve",
		index = 28
	)
	private static boolean fixSlopeChecking2(boolean original) {
		if(original) {
			return true;
		}

		return !Util.orElse(NTLConfigs.server.allowSlopeSTurns, true);
	}
}
