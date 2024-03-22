package dev.rdh.ntl.config;

import com.simibubi.create.foundation.config.ConfigBase;

import dev.rdh.ntl.CreateNTL;

@SuppressWarnings("unused") // groups are used as markers for the screen and command
public class NTLServer extends ConfigBase {
	@Override
	public String getName() {
		return "server";
	}

	public final ConfigFloat minTurnSize = f(7, 7, "minTurnSize", Comments.minTurnSize);
	public final ConfigFloat min45TurnSize = f(3.25f, 3.25f, "min45TurnSize", Comments.min45TurnSize);
	public final ConfigBool allowSlopeSTurns = b(false, "allowSlopeSTurns", Comments.allowSlopeSTurns);

	private static class Comments {
		static String minTurnSize = "Minimum radius for placing 90 degree turns";
		static String min45TurnSize = "Minimum radius for placing 45 degree turns";
		static String allowSlopeSTurns = "Allow placing s-bends on slopes";
	}

	public static String getComment(String name) {
		try {
			return (String) Comments.class.getDeclaredField(name).get(null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			CreateNTL.LOGGER.error("Failed to get comment for " + name, e);
			return "No comment.";
		}
	}
}
