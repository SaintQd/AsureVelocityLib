package org.saintqd.asurevelocitylib;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.saintqd.asurevelocitylib.api.AsurePlugin;
import org.saintqd.asurevelocitylib.commands.AsureVelocityLibCommands;
import org.saintqd.asurevelocitylib.configuration.file.FileConfiguration;
import org.saintqd.asurevelocitylib.configuration.file.YamlConfiguration;
import org.saintqd.asurevelocitylib.managers.LangManager;
import org.saintqd.asurevelocitylib.utils.ResourceUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;

@Plugin(id = "asurevelocitylib",
        name = "AsureVelocityLib",
        version = "1.0.0",
        authors = {"SaintQd"}
)
public class AsureVelocityLib implements AsurePlugin {

    private static AsureVelocityLib plugin;
    private YamlConfiguration config = null;
    private int debugLevel = 0;

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    @NotNull
    private String language = "";

    @Inject
    public AsureVelocityLib(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        plugin = this;

        logger.info("Initializing plugin...");
        try {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            ResourceUtils.fetchAllResources(this, new File(path));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }

        BrigadierCommand mainCommand = AsureVelocityLibCommands.setupCommands(server);
        server.getCommandManager().register(
                server.getCommandManager().metaBuilder(mainCommand)
                        .plugin(this)
                        .build(),
                mainCommand
        );

        loadData();
    }

    public void loadData() {
        config = YamlConfiguration.loadConfiguration(dataDirectory.resolve("config.yml").toFile());

        language = config.getString("Language","");
        HashMap<Key,String> langLines = LangManager.INSTANCE.loadLanguageFile(
                this,dataDirectory.resolve("lang").resolve(language+".yml").toString());

        LangManager.INSTANCE.registerLangLines(langLines);
    }

    public static AsureVelocityLib inst() {
        return plugin;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public int getDebugLevel() {
        return debugLevel;
    }

    public void setDebugLevel(int debugLevel) {
        this.debugLevel = debugLevel;
    }

    @Override
    public String getName() {
        return "AsureVelocityLib";
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public @NotNull String namespace() {
        return "asurevelocitylib";
    }

    @Override
    public Path getDataDirectory() {
        return dataDirectory;
    }

    @Override
    public String getLanguage() {
        return language;
    }
}
