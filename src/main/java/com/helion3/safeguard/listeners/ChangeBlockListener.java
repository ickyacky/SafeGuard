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
package com.helion3.safeguard.listeners;

import com.helion3.safeguard.util.BlockUtil;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;

import com.helion3.safeguard.SafeGuard;
import com.helion3.safeguard.util.Format;
import org.spongepowered.api.event.filter.cause.First;

public class ChangeBlockListener {
    private final String flag = "block.change";

    @Listener
    public void onChangeBlock(final ChangeBlockEvent event, @First Player player) {
        if (player.hasPermission("safeguard.mod")) {
            return;
        }

        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            if (!SafeGuard.getZoneManager().allows(player, flag, transaction.getOriginal().getLocation().get())) {
                String verb = "change";
                String block = transaction.getOriginal().getState().getType().getName();

                if (event instanceof ChangeBlockEvent.Break) {
                    verb = "break";
                }
                else if (event instanceof ChangeBlockEvent.Place) {
                    block = transaction.getFinal().getState().getType().getName();
                    verb = "place";
                }

                transaction.setValid(false);

                // Should we message the player?
                if (!BlockUtil.ignore(event, transaction)) {
                    player.sendMessage(Format.error(String.format("Zone does not allow you to %s this %s block.",
                            verb, block.replace("minecraft:", ""))));
                }
            }
        }
    }
}
