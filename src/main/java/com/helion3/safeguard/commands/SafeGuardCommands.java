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

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.helion3.safeguard.util.Format;

public class SafeGuardCommands {
    private SafeGuardCommands() {}

    /**
     * Build a complete command hierarchy
     * @return
     */
    public static CommandSpec getCommand() {
        ImmutableMap.Builder<List<String>, CommandCallable> builder = ImmutableMap.builder();
        builder.put(ImmutableList.of("pos", "position"), PositionCommand.getCommand());
        builder.put(ImmutableList.of("zone", "z"), ZoneCommands.getCommand());
        builder.put(ImmutableList.of("reload"), ReloadCommand.getCommand());
        builder.put(ImmutableList.of("?", "help"), HelpCommand.getCommand());

        return CommandSpec.builder()
        .executor((src, args) -> {
            src.sendMessage(Text.of(
                Format.heading(TextColors.GRAY, "By ", TextColors.GOLD, "viveleroi.\n"),
                TextColors.GRAY, "Help: ", TextColors.WHITE, "/sg ?\n",
                TextColors.GRAY, "IRC: ", TextColors.WHITE, "irc.esper.net #helion3\n"
            ));
            return CommandResult.empty();
        })
        .children(builder.build()).build();
    }
}
