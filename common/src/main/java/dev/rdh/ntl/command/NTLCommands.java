package dev.rdh.ntl.command;

import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;


import dev.rdh.ntl.CreateNTL;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.infrastructure.command.AllCommands;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

import java.util.Collections;
import java.util.List;

public class NTLCommands {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
		List<MutableComponent> links = List.of(
			//todo: add links
		);
		LiteralArgumentBuilder<CommandSourceStack> base = Commands.literal(CreateNTL.ID)
			.executes(context -> {
				message(context, CreateNTL.NAME + " v" + CreateNTL.VERSION + " by rdh\nVisit us on:");
				MutableComponent link = MutableComponent.create(CommonComponents.EMPTY.getContents());
				links.forEach(a -> link.append(a).append(Component.literal(" ")));
				message(context, link);
				return 1;
			})
			.then(NTLConfigCommand.register(dedicated));

		LiteralCommandNode<CommandSourceStack> root = dispatcher.register(base);

		CommandNode<CommandSourceStack> cu = dispatcher.findNode(Collections.singleton("ntl"));
		if(cu != null) return;
		dispatcher.getRoot().addChild(AllCommands.buildRedirect("ntl", root));
	}

	protected static MutableComponent link(String link, String display, ChatFormatting color) {
		return ComponentUtils.wrapInSquareBrackets(Component.nullToEmpty(display))
			.withStyle(color)
			.withStyle(style -> style
				.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Components.literal("Click to open " + display + " page")))
				.withUnderlined(false));
	}

	protected static void message(CommandContext<CommandSourceStack> context, Component message) {
		context.getSource().sendSystemMessage(message);
	}

	protected static void message(CommandContext<CommandSourceStack> context, String message) {
		message(context, Component.nullToEmpty(message));
	}

	protected static void error(CommandContext<CommandSourceStack> context, Component message) {
		context.getSource().sendFailure(message);
	}

	protected static void error(CommandContext<CommandSourceStack> context, String message) {
		error(context, Component.nullToEmpty(message));
	}
}
