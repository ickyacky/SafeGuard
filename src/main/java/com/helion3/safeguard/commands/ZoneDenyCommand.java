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
import java.util.concurrent.ExecutionException;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.ProfileNotFoundException;
import org.spongepowered.api.text.Text;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.Zone;

public class ZoneDenyCommand implements CommandCallable {
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(Format.error("Command usable only by a player."));
            return CommandResult.empty();
        }

        Player player = (Player) source;
        String[] args = arguments.split(" ");

        List<Zone> zones = SafeGuard.getZoneManager().getZones(player.getLocation());
        if (zones.isEmpty()) {
            source.sendMessage(Format.error("No zone at your location. Try defining it by name. /sg zone [name] ..."));
            return CommandResult.empty();
        }

        if (zones.size() > 1) {
            source.sendMessage(Format.error("Overlapping zones at your location. Try defining one by name. /sg zone [name] ..."));
            return CommandResult.empty();
        }

        if (args[0].isEmpty()) {
            source.sendMessage(Format.error("Invalid command syntax. /sg zone deny [player] (flag)"));
            return CommandResult.empty();
        }

        final Zone zone = zones.get(0);

        if (!player.hasPermission("safeguard.mod") && !zone.getOwners().contains(player.getProfile())) {
            source.sendMessage(Format.error("You do not have permission to change this zone."));
            return CommandResult.empty();
        }

        ListenableFuture<GameProfile> future = SafeGuard.getGame().getServer().getGameProfileManager().get(args[0]);
        future.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    GameProfile profile = future.get();

                    // Deny permissions and save
                    if (zone.deny(profile)) {
                        zone.save();

                        source.sendMessage(Format.success(profile.getName() + " denied permission in this zone."));
                    } else {
                        source.sendMessage(Format.error(profile.getName() + " does not have any permission in this zone."));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    if (e instanceof ProfileNotFoundException) {
                        source.sendMessage(Format.error("Could not find player with name " + args[0]));
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        }, MoreExecutors.sameThreadExecutor());

        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("safeguard.create");
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Text.of("Create a new zone."));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Text.of("Create a new zone."));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of("/sg zone allow [user] (flags)");
    }
}
