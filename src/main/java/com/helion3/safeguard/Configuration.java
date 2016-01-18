package com.helion3.safeguard;

import java.io.File;
import java.io.IOException;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class Configuration {
    private ConfigurationNode rootNode = null;

    /**
     * Loads (creates new if needed) Prism configuration file.
     * @param defaultConfig
     * @param configManager
     */
    public Configuration(File defaultConfig, ConfigurationLoader<CommentedConfigurationNode> configManager) {
        try {
            // If file does not exist, we must create it
            if (!defaultConfig.exists()) {
                defaultConfig.getParentFile().mkdirs();
                defaultConfig.createNewFile();
                rootNode = configManager.createEmptyNode(ConfigurationOptions.defaults());
            } else {
                rootNode = configManager.load();
            }

            // Database
            ConfigurationNode dbName = rootNode.getNode("db", "name");
            if (dbName.isVirtual()) {
                dbName.setValue("prism");
            }



            // Save
            try {
                configManager.save(rootNode);
            } catch(IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
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
