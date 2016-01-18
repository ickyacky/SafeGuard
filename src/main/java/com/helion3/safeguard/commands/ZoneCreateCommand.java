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
import java.util.Optional;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.Zone;
import com.helion3.safeguard.zones.ZoneBuffer;

public class ZoneCreateCommand implements CommandCallable {
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(Format.error("Command usable only by a player."));
            return CommandResult.empty();
        }

        Player player = (Player) source;

        if (!SafeGuard.getActiveBuffers().containsKey(player)) {
            source.sendMessage(Format.error("You haven't added any positions for a shape. Use /sg pos"));
            return CommandResult.empty();
        }

        String[] args = arguments.split(" ");
        if (args.length != 1 || args[0].isEmpty()) {
            source.sendMessage(Format.error("You must provide a zone name. /sg create (zone name)"));
            return CommandResult.empty();
        }

        ZoneBuffer buffer = SafeGuard.getActiveBuffers().get(player);

        if (!buffer.isComplete()) {
            source.sendMessage(Format.error("Your shape is incomplete. Use /sg pos"));
            return CommandResult.empty();
        }

        SafeGuard.getActiveBuffers().remove(player);

        // Create a new zone
        Zone zone = new Zone(args[0], buffer.getZoneVolume());
        zone.addOwner(player.getProfile());

        SafeGuard.getZoneManager().add(zone);

        source.sendMessage(Format.success("Zone created successfully!"));

        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("safehaven.create");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Text.of("Mark a position as a zone boundary."));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Text.of("Create a rectangle with two positions."));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("/safe pos");
    }
}
