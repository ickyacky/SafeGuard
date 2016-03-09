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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.helion3.safeguard.commands.SafeGuardCommands;
import com.helion3.safeguard.listeners.ChangeBlockListener;
import com.helion3.safeguard.listeners.DamageEntityListener;
import com.helion3.safeguard.listeners.DropItemListener;
import com.helion3.safeguard.listeners.ExplosionListener;
import com.helion3.safeguard.listeners.InteractBlockListener;
import com.helion3.safeguard.listeners.InteractEntityListener;
import com.helion3.safeguard.listeners.MoveEntityListener;
import com.helion3.safeguard.listeners.SpawnEntityListener;
import com.helion3.safeguard.util.DataUtil;
import com.helion3.safeguard.zones.Zone;
import com.helion3.safeguard.zones.ZoneBuffer;
import com.helion3.safeguard.zones.ZoneManager;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "safeguard", name = "safeguard", version = "1.0")
public class SafeGuard {
    private static Configuration config;
    private static Game game;
    private static Logger logger;
    private static File parentDirectory;
    private static Map<Player, ZoneBuffer> activeBuffers = new HashMap<Player, ZoneBuffer>();
    private static ZoneManager zoneManager = new ZoneManager();

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        parentDirectory = defaultConfig.getParentFile();

        try {
            loadZones();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Commands
        game.getCommandManager().register(this, SafeGuardCommands.getCommand(), "sg", "safeguard");

        // Listeners
        game.getEventManager().registerListeners(this, new ChangeBlockListener());
        game.getEventManager().registerListeners(this, new DamageEntityListener());
        game.getEventManager().registerListeners(this, new DropItemListener());
        game.getEventManager().registerListeners(this, new ExplosionListener());
        game.getEventManager().registerListeners(this, new InteractBlockListener());
        game.getEventManager().registerListeners(this, new InteractEntityListener());
        game.getEventManager().registerListeners(this, new MoveEntityListener());
        game.getEventManager().registerListeners(this, new SpawnEntityListener());

        logger.info("SafeGuard started. Your haven is safe.");
    }

    /**
     * Get the config
     *
     * @return Configuration
     */
    public static Configuration getConfig() {
        return config;
    }

    /**
     * Get the Game instance.
     *
     * @return Game
     */
    public static Game getGame() {
        return game;
    }

    /**
     * Injected Game instance.
     *
     * @param injectGame Game
     */
    @Inject
    public void setGame(Game injectGame) {
        game = injectGame;
    }

    /**
     * Get the logger.
     *
     * @return Logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Injects the Logger instance for this plugin.
     *
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
     * Get parent directory.
     *
     * @return File
     */
    public static File getParentDirectory() {
        return parentDirectory;
    }

    /**
     * Get zone manager.
     *
     * @return ZoneManager
     */
    public static ZoneManager getZoneManager() {
        return zoneManager;
    }

    /**
     * Loads zones from save files.
     *
     * @throws IOException
     */
    public static void loadZones() throws IOException {
        File dir = new File(parentDirectory.getAbsolutePath() + "/zones");
        if (dir.exists()) {
            Files.walk(Paths.get(parentDirectory.getAbsolutePath() + "/zones")).forEach(filePath -> {
                if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".json")) {
                    try {
                        FileReader reader = new FileReader(filePath.toString());
                        JsonObject json = new JsonParser().parse(reader).getAsJsonObject();

                        // Convert to a data container
                        DataContainer data = DataUtil.jsonToDataContainer(json);

                        // Create a zone
                        zoneManager.add(Zone.from(data));

                        reader.close();
                    } catch (Exception e) {
                        logger.error("Json Exception for file " + filePath.toString());
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
