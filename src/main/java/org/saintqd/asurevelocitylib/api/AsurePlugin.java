package org.saintqd.asurevelocitylib.api;

import net.kyori.adventure.key.Namespaced;
import org.saintqd.asurevelocitylib.configuration.file.FileConfiguration;

import java.nio.file.Path;

public interface AsurePlugin extends Namespaced {

    String getName();

    FileConfiguration getConfig();

    Path getDataDirectory();

    String getLanguage();
}
