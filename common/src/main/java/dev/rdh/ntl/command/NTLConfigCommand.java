package dev.rdh.ntl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.rdh.ntl.mixin.accessor.CValueAccessor;
import dev.rdh.ntl.CreateNTL;
import dev.rdh.ntl.config.NTLConfigs;
import dev.rdh.ntl.config.NTLServer;

import com.simibubi.create.foundation.config.ConfigBase.CValue;
import com.simibubi.create.foundation.config.ConfigBase.ConfigGroup;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Field;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class NTLConfigCommand extends NTLCommands {
	private static boolean dedicated;

	public static ArgumentBuilder<CommandSourceStack, ?> register(boolean dedicated) {
		NTLConfigCommand.dedicated = dedicated;
		LiteralArgumentBuilder<CommandSourceStack> base = literal("config");

		LiteralArgumentBuilder<CommandSourceStack> category = null;

		for (Field field : NTLServer.class.getDeclaredFields()) {
			// skip if not config value
			if (!CValue.class.isAssignableFrom(field.getType())) continue;

			String name = field.getName();

			// change category if needed
			if (field.getType() == ConfigGroup.class) {
				if (category != null) base.then(category);
				category = literal(name);

				// add description for category
				base.then(literal(field.getName()).executes(context -> {
					message(context, NTLServer.getComment(name));
					return Command.SINGLE_SUCCESS;
				}));

				continue;
			}
			if(category == null) category = base;

			// get config as CValue
			CValue<?, ?> cValue;
			try {
				cValue = (CValue<?, ?>) field.get(NTLConfigs.server);
			} catch (IllegalAccessException | ClassCastException e) {
				CreateNTL.LOGGER.error("Failed to get config value for " + field.getName(), e);
				continue;
			}

			// get config as forge config value
			ConfigValue<?> value = ((CValueAccessor) cValue).getValue();

			// get, description, reset
			gdr(category, name, value);

			// set for boolean
			if (value instanceof BooleanValue bValue)
				setBoolean(category, name, bValue);

			// set for enums
			else if (value instanceof EnumValue<? extends Enum<?>> eValue)
				setEnum(category, name, eValue);

			// set for int
			else if (value instanceof IntValue iValue)
				setInt(category, name, iValue);

			// set for double
			else if (value instanceof DoubleValue dValue)
				setDouble(category, name, dValue);

		}

		if (category != null)
			base.then(category);
		return base;
	}

	private static boolean perms(Object o) {
		if(!(o instanceof CommandSourceStack source)) return false;
		Entity e = source.getEntity();
		return dedicated || e != null && e.hasPermissions(2);
	}

	private static <T> void gdr(LiteralArgumentBuilder<CommandSourceStack> category, String name, ConfigValue<T> value) {
		category.then(literal(name)
			.executes(context -> {
				message(context, name + ": " + NTLServer.getComment(name));
				message(context, "Current value: " + value.get());
				message(context, "Default value: " + value.getDefault());
				return Command.SINGLE_SUCCESS;
			})
			.then(literal("reset").requires(NTLConfigCommand::perms)
				.executes(context -> {
					if(value.get().equals(value.getDefault())) {
						error(context, "Value is already default!");
						return 0;
					}
					value.set(value.getDefault());
					message(context, name + " reset to: " + value.get());
					return Command.SINGLE_SUCCESS;
				})
			)
		);
	}

	private static void setBoolean(LiteralArgumentBuilder<CommandSourceStack> category, String name, BooleanValue value) {
		category.then(literal(name)
			.then(argument("value", BoolArgumentType.bool()).requires(NTLConfigCommand::perms)
				.executes(context -> {
					boolean set = BoolArgumentType.getBool(context, "value");
					if(set == value.get()) {
						error(context, "Value is already set to " + set);
						return 0;
					}
					value.set(set);
					message(context, name + " set to: " + set);
					return Command.SINGLE_SUCCESS;
				})
			)
		);
	}

	private static void setInt(LiteralArgumentBuilder<CommandSourceStack> category, String name, IntValue value) {
		category.then(literal(name)
			.then(argument("value", IntegerArgumentType.integer()).requires(NTLConfigCommand::perms)
				.executes(context -> {
					int set = IntegerArgumentType.getInteger(context, "value");
					if(set == value.get()) {
						error(context, "Value is already set to " + set);
						return 0;
					}
					value.set(set);
					message(context, name + " set to: " + set);
					return Command.SINGLE_SUCCESS;
				})
			)
		);
	}

	private static void setDouble(LiteralArgumentBuilder<CommandSourceStack> category, String name, DoubleValue value) {
		category.then(literal(name)
			.then(argument("value", DoubleArgumentType.doubleArg()).requires(NTLConfigCommand::perms)
				.executes(context -> {
					double set = DoubleArgumentType.getDouble(context, "value");
					if(set == value.get()) {
						error(context, "Value is already set to " + set);
						return 0;
					}
					value.set(set);
					message(context, name + " set to: " + set);
					return Command.SINGLE_SUCCESS;
				})
			)
		);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Enum<T>> void setEnum(LiteralArgumentBuilder<CommandSourceStack> category, String name, EnumValue<T> value) {
		category.then(literal(name)
			.then(argument("value", EnumArgument.enumArg(value.getDefault().getClass(), true))
				.requires(NTLConfigCommand::perms)
				.executes(context -> {
					T set = EnumArgument.getEnum(context, "value", (Class<T>) value.getDefault().getClass());
					if(set == value.get()) {
						error(context, "Value is already set to " + set.name().toLowerCase());
						return 0;
					}
					value.set(set);
					message(context, name + " set to: " + set.name().toLowerCase());
					return Command.SINGLE_SUCCESS;
				})
			)
		);
	}
}
