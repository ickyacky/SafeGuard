package com.helion3.safeguard.commands;

import java.util.List;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.Zone;

public class ZoneCommands {
    private ZoneCommands() {}

    /**
     * Build a complete command hierarchy
     * @return
     */
    public static CommandSpec getCommand() {
        // Build child commands
        ImmutableMap.Builder<List<String>, CommandCallable> builder = ImmutableMap.builder();
        builder.put(ImmutableList.of("create"), new ZoneCreateCommand());
        builder.put(ImmutableList.of("allow"), new ZoneAllowCommand());

        return CommandSpec.builder()
                .executor(new CommandExecutor() {
                    @Override
                    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
                        if (!(source instanceof Player)) {
                            source.sendMessage(Format.error("Command usable only by a player."));
                            return CommandResult.empty();
                        }

                        Player player = (Player) source;

                        List<Zone> zones = SafeGuard.getZoneManager().getZones(player.getLocation());
                        if (zones.isEmpty()) {
                            source.sendMessage(Format.error("No zone found for your position."));
                            return CommandResult.empty();
                        }

                        for (Zone zone : zones) {
                            source.sendMessage(Format.heading("Zone " + zone.getName()));
                            source.sendMessage(Format.subdued("Owners:"));

                            for (GameProfile profile : zone.getOwners()) {
                                source.sendMessage(Format.message(profile.getName()));
                            }

                            source.sendMessage(Format.subdued("Volume:"));
                            source.sendMessage(Format.message(zone.getVolume().getMin().toString() + ", " + zone.getVolume().getMax().toString()));
                            source.sendMessage(Format.message("Blocks: " + zone.getVolume().getVolume()));
                        }

                        return CommandResult.empty();
                    }
                })
                .children(builder.build()).build();
    }
}
