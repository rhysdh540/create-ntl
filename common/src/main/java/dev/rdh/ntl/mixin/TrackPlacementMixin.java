package dev.rdh.ntl.mixin;

import com.simibubi.create.content.trains.track.*;

import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("unused")
@Mixin(value = TrackPlacement.class, remap = false, priority = 0) // inject before snr does
public abstract class TrackPlacementMixin {

}
