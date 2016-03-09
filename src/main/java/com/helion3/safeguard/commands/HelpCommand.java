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

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.format.TextColors;

import com.helion3.safeguard.util.Format;

public class HelpCommand {
    private HelpCommand(){}

    public static CommandSpec getCommand() {
        return CommandSpec.builder()
            .executor((source, args) -> {
                source.sendMessage(Format.message("/sg pos", TextColors.GRAY, " - Mark a corner for a new zone."));
                source.sendMessage(Format.message("/sg pos clear", TextColors.GRAY, " - Remove previously selected positions."));
                source.sendMessage(Format.message("/sg pos fullheight", TextColors.GRAY, " - Expand cube to full map height."));
                source.sendMessage(Format.message("/sg zone create [name]", TextColors.GRAY, " - Create a new zone with positions in buffer."));
                source.sendMessage(Format.message("/sg zone allow (player)", TextColors.GRAY, " - Allow a player access to the zone."));
                source.sendMessage(Format.message("/sg zone deny (player)", TextColors.GRAY, " - Remove player's access to this zone."));
                source.sendMessage(Format.message("/sg zone delete", TextColors.GRAY, " - Delete this zone."));
                source.sendMessage(Format.message("/sg zone flag (flag) (true|false)", TextColors.GRAY, " - Toggle a flag."));
                source.sendMessage(Format.message("/sg reload", TextColors.GRAY, " - Reload config."));
                return CommandResult.empty();
            }).build();
    }
}
