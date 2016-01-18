package com.helion3.safeguard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ZoneConfiguration {
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private ConfigurationNode rootNode = null;;

    public ZoneConfiguration(File parentDirectory, String name) {
        try {
            // If files do not exist, we must create them
            File dir = new File(parentDirectory.getAbsolutePath() + "/players");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File conf = new File(dir.getAbsolutePath() + "/" + name + ".conf");
            boolean fileCreated = false;

            if (!conf.exists()) {
                conf.createNewFile();
                fileCreated = true;
            }

            configLoader = HoconConfigurationLoader.builder().setFile(conf).build();
            if (fileCreated) {
                rootNode = configLoader.createEmptyNode(ConfigurationOptions.defaults());
            } else {
                rootNode = configLoader.load();
            }

            ConfigurationNode zones = rootNode.getNode("zones");
            if (zones.isVirtual()) {
                zones.setValue(new ArrayList<String>());
            }

            // Save
            save();
        } catch (IOException e) {
            // @todo handle properly
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            configLoader.save(rootNode);
        } catch (IOException e) {
            // @todo handle properly
            e.printStackTrace();
        }
    }

    /**
     * Shortcut to rootNode.getNode().
     *
     * @param path Object[] Paths to desired node
     * @return ConfigurationNode
     */
    public ConfigurationNode getNode(Object... path) {
        return rootNode.getNode(path);
    }
}
