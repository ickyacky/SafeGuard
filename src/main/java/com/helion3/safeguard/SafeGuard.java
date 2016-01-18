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
package com.helion3.safeguard;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;
import com.helion3.safeguard.commands.SafeGuardCommands;
import com.helion3.safeguard.listeners.ChangeBlockListener;
import com.helion3.safeguard.zones.ZoneBuffer;
import com.helion3.safeguard.zones.ZoneManager;

@Plugin(id = "SafeGuard", name = "SafeGuard", version = "1.0")
public class SafeGuard {
    private static Game game;
    private static Logger logger;
    private static Map<Player, ZoneBuffer> activeBuffers = new HashMap<Player, ZoneBuffer>();
    private static ZoneManager zoneManager = new ZoneManager();

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Commands
        game.getCommandManager().register(this, SafeGuardCommands.getCommand(), "sg", "safeguard");

        // Listeners
        game.getEventManager().registerListeners(this, new ChangeBlockListener());

        logger.info("SafeGuard started. Your haven is safe.");
    }

    /**
     * Injected Game instance.
     * @param injectGame Game
     */
    @Inject
    public void setGame(Game injectGame) {
        game = injectGame;
    }

    /**
     * Injects the Logger instance for this plugin
     * @param log Logger
     */
    @Inject
    private void setLogger(Logger log) {
        logger = log;
    }

    /**
     * Get all active buffers.
     *
     * @return Map of players and active buffer.
     */
    public static Map<Player, ZoneBuffer> getActiveBuffers() {
        return activeBuffers;
    }

    /**
     * Get zone manager.
     */
    public static ZoneManager getZoneManager() {
        return zoneManager;
    }
}
