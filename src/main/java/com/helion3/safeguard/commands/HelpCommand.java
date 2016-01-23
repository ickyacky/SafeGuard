package com.helion3.safeguard.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.format.TextColors;

import com.helion3.safeguard.util.Format;

public class HelpCommand {
    private HelpCommand(){}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
            .executor(new CommandExecutor() {
                @Override
                public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
                    source.sendMessage(Format.message("/sg pos", TextColors.GRAY, " - Mark a corner for a new zone."));
                    source.sendMessage(Format.message("/sg pos fullheight", TextColors.GRAY, " - Expand cube to full map height."));
                    source.sendMessage(Format.message("/sg zone create [name]", TextColors.GRAY, " - Create a new zone with positions in buffer."));
                    source.sendMessage(Format.message("/sg zone allow (player)", TextColors.GRAY, " - Allow a player access to the zone."));
                    source.sendMessage(Format.message("/sg zone deny (player)", TextColors.GRAY, " - Remove player's access to this zone."));
                    source.sendMessage(Format.message("/sg zone delete", TextColors.GRAY, " - Delete this zone."));
                    source.sendMessage(Format.message("/sg reload", TextColors.GRAY, " - Reload config."));
                    return CommandResult.empty();
                }
            }).build();
    }
}
