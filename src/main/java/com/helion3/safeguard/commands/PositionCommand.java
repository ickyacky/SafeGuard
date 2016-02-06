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

import java.util.Optional;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.option.OptionSubject;
import org.spongepowered.api.text.Text;

import com.flowpowered.math.vector.Vector3i;
import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import com.helion3.safeguard.zones.CubicalZoneBuffer;
import com.helion3.safeguard.zones.ZoneBuffer;

public class PositionCommand {
    private PositionCommand() {}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
        .description(Text.of("Mark a position as a zone boundary."))
        .permission("safeguard.create")
        .executor((source, args) -> {
            if (!(source instanceof Player)) {
                source.sendMessage(Format.error("Command usable only by a player."));
                return CommandResult.empty();
            }

            Player player = (Player) source;
            ZoneBuffer buffer;

            if (SafeGuard.getZoneManager().zoneExists(player.getLocation())) {
                source.sendMessage(Format.error("Zone already exists here. Overlapping zones not currently allowed."));
                return CommandResult.empty();
            }

            // Get or create a buffer
            if (SafeGuard.getActiveBuffers().containsKey(player)) {
                buffer = SafeGuard.getActiveBuffers().get(player);
            } else {
                // Only currently supported shape
                buffer = new CubicalZoneBuffer(player.getWorld());
            }

            if (buffer.isComplete()) {
                source.sendMessage(Format.error("All positions set. Next use /sg zone create (zone name)"));
            } else {
                Subject subject = player.getContainingCollection().get(player.getIdentifier());
                int lengthLimit = 0;

                if (subject instanceof OptionSubject) {
                    Optional<String> optionalLimit = ((OptionSubject) subject).getOption("zoneLengthLimit");
                    if (optionalLimit.isPresent()) {
                        lengthLimit = Integer.parseInt(optionalLimit.get());
                    }
                }

                Vector3i newPosition = player.getLocation().getBlockPosition();

                boolean allowed = true;
                if (lengthLimit > 0 && buffer.getPositions().size() > 0) {
                    Vector3i min = buffer.getPositions().get(0);

                    if (Math.abs(Math.abs(min.getX()) - Math.abs(newPosition.getX())) > lengthLimit) {
                        allowed = false;
                    }
                    else if (Math.abs(Math.abs(min.getZ()) - Math.abs(newPosition.getZ())) > lengthLimit) {
                        allowed = false;
                    }
                }

                if (!allowed) {
                    source.sendMessage(Format.error("You are limited to a zone no large than " + lengthLimit + "x" + lengthLimit));
                    return CommandResult.empty();
                }

                // Add current position
                buffer.addPosition(newPosition);

                // Store
                SafeGuard.getActiveBuffers().put(player, buffer);

                // Message
                if (buffer.isComplete()) {
                    source.sendMessage(Format.success("Added position. Shape complete, use /sg zone create (zone name)"));
                } else {
                    source.sendMessage(Format.success("Added position to buffer."));
                }
            }

            return CommandResult.success();
        })
        .child(getFullheightCommand(), "fullheight")
        .child(getClearCommand(), "clear")
        .build();
    }

    protected static CommandSpec getClearCommand() {
        return CommandSpec.builder()
        .description(Text.of("Clear positions in your buffer."))
        .permission("safeguard.create")
        .executor((source, args) -> {
            if (!(source instanceof Player)) {
                source.sendMessage(Format.error("Command usable only by a player."));
                return CommandResult.empty();
            }

            Player player = (Player) source;

            SafeGuard.getActiveBuffers().remove(player);
            source.sendMessage(Format.success("Positions cleared."));

            return CommandResult.success();
        })
        .build();
    }

    protected static CommandSpec getFullheightCommand() {
        return CommandSpec.builder()
        .description(Text.of("Expand current buffer positions to the full map height."))
        .permission("safeguard.create")
        .executor((source, args) -> {
            if (!(source instanceof Player)) {
                source.sendMessage(Format.error("Command usable only by a player."));
                return CommandResult.empty();
            }

            Player player = (Player) source;

            // Get buffer
            if (!SafeGuard.getActiveBuffers().containsKey(player)) {
                source.sendMessage(Format.error("You haven't set any positions yet."));
                return CommandResult.empty();
            }

            ZoneBuffer buffer = SafeGuard.getActiveBuffers().get(player);
            if (!buffer.isComplete()) {
                source.sendMessage(Format.error("Buffer is incomplete. Please finish it first."));
                return CommandResult.empty();
            }

            int oldSize = buffer.getZoneVolume().getVolume();

            buffer.transformMinPosition((Vector3i pos) -> new Vector3i(pos.getX(), 0, pos.getZ()));
            buffer.transformMaxPosition((Vector3i pos) -> new Vector3i(pos.getX(), player.getWorld().getDimension().getBuildHeight(), pos.getZ()));

            int newSize = buffer.getZoneVolume().getVolume();

            source.sendMessage(Format.success("Expanded to full height. " + (newSize - oldSize) + " new blocks."));

            return CommandResult.success();
        })
        .build();
    }
}
