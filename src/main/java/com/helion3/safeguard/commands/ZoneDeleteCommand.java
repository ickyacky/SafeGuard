package com.helion3.safeguard.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.Zone;

public class ZoneDeleteCommand {
    private ZoneDeleteCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("Delete a zone."))
        .permission("safeguard.create")
        .executor(new CommandExecutor() {
            @Override
            public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
                if (!(source instanceof Player)) {
                    source.sendMessage(Format.error("Command usable only by a player."));
                    return CommandResult.empty();
                }

                Player player = (Player) source;

                List<Zone> zones = SafeGuard.getZoneManager().getZones(player.getLocation(), player);
                if (zones.isEmpty()) {
                    source.sendMessage(Format.error("No zone at your location. Try defining it by name. /sg zone [name] ..."));
                    return CommandResult.empty();
                }

                SafeGuard.getZoneManager().delete(zones.get(0));

                source.sendMessage(Format.success("Zone removed successfully."));

                return CommandResult.success();
            }
        }).build();
    }
}
