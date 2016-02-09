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
import java.util.Map;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.Zone;
import org.spongepowered.api.text.format.TextColors;

public class ZoneCommands {
    private ZoneCommands() {}

    /**
     * Build a complete command hierarchy
     * @return
     */
    public static CommandSpec getCommand() {
        // Build child commands
        ImmutableMap.Builder<List<String>, CommandCallable> builder = ImmutableMap.builder();
        builder.put(ImmutableList.of("create", "c"), new ZoneCreateCommand());
        builder.put(ImmutableList.of("allow", "a"), new ZoneAllowCommand());
        builder.put(ImmutableList.of("deny", "d"), new ZoneDenyCommand());
        builder.put(ImmutableList.of("delete"), ZoneDeleteCommand.getCommand());
        builder.put(ImmutableList.of("flag", "f"), ZoneFlagCommand.getCommand());

        return CommandSpec.builder()
        .executor((source, args) -> {
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

                // Owners
                source.sendMessage(Format.subdued("Owners:"));
                for (GameProfile profile : zone.getOwners()) {
                    source.sendMessage(Format.message(profile.getName()));
                }

                // Volume
                source.sendMessage(Format.subdued("Volume:"));
                source.sendMessage(Format.message(zone.getVolume().getMin().toString() + ", " + zone.getVolume().getMax().toString()));
                source.sendMessage(Format.message("Blocks: " + zone.getVolume().getVolume()));

                // Flags
                source.sendMessage(Format.subdued("Flags:"));
                for (Map.Entry<String, Boolean> entry : zone.getPermissions().getPermissions().entrySet()) {
                    source.sendMessage(Format.message(entry.getKey(), TextColors.YELLOW, entry.getValue()));
                }
            }

            return CommandResult.empty();
        })
        .children(builder.build()).build();
    }
}
