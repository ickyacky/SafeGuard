/**
 * This file is part of SafeGuard, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 Helion3 http://helion3.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.helion3.safeguard.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.Zone;

public class ZoneFlagCommand {
    private ZoneFlagCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("Change flags for a zone."))
        .permission("safeguard.create")
        .arguments(
            new FlagArgument(Text.of("flag")),
            GenericArguments.bool(Text.of("value"))
        )
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

                if (!player.hasPermission("safeguard.mod") && !zones.get(0).getOwners().contains(player.getProfile())) {
                    source.sendMessage(Format.error("You do not have permission to change this zone."));
                    return CommandResult.empty();
                }

                String flag = args.<String>getOne("flag").get();
                boolean value = args.<Boolean>getOne("value").get();

                // Only use the first
                Zone zone = zones.get(0);

                zone.getPermissions().put(flag, value);
                zone.save();

                source.sendMessage(Format.success(String.format("Successfully %sabled %s in this zone!", (value ? "en" : "dis"), flag)));

                return CommandResult.success();
            }
        }).build();
    }
}
