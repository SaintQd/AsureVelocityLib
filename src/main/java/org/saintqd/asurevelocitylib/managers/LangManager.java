package org.saintqd.asurevelocitylib.managers;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.intellij.lang.annotations.Subst;
import org.saintqd.asurevelocitylib.api.AsurePlugin;
import org.saintqd.asurevelocitylib.configuration.ConfigurationSection;
import org.saintqd.asurevelocitylib.configuration.file.YamlConfiguration;
import org.saintqd.asurevelocitylib.utils.AsureUtils;

import java.io.File;
import java.util.HashMap;

public class LangManager {

    public static LangManager INSTANCE = new  LangManager();

    private final HashMap<Key,String> langLines;

    public LangManager() {
        this.langLines = new HashMap<>();
    }

    public void registerLangLines(HashMap<Key,String> langLines) {
        this.langLines.putAll(langLines);
    }

    public HashMap<Key,String> getLangLines() {
        return langLines;
    }

    public HashMap<Key,String> loadLanguageFile(AsurePlugin plugin, String path) {
        HashMap<Key,String> langLines = new HashMap<>();
        File langFile = new File(path);
        if (!langFile.exists()) {
            AsureUtils.sendDebugMessage(0,"<yellow>Lang file "+langFile+" does not exist!");
            return langLines;
        }
        YamlConfiguration langFileYaml = YamlConfiguration.loadConfiguration(langFile);
        ConfigurationSection langFileConfig = langFileYaml.getConfigurationSection("Lang");
        for (String identifier : langFileConfig.getKeys(false)) {
            @Subst("asurevelocitylib.value") String keyValue = identifier.toLowerCase();
            Key langKey = Key.key(plugin,keyValue);
            langLines.put(langKey,langFileConfig.getString(identifier));
        }
        return langLines;
    }

    public Component parseLangString(AsurePlugin plugin, String identifier, String... args) {
        @Subst("asurevelocitylib.value") String keyValue = identifier.toLowerCase();
        Key key = Key.key(plugin,keyValue);
        return parseLangString(key,identifier,args);
    }

    public Component parseLangString(Key key, String identifier, String... args) {
        identifier = identifier.toLowerCase();
        String line;
        //if (langLinesPerPlugin.getOrDefault(plugin,new HashMap<>()).containsKey(identifier))
        //    line = langLinesPerPlugin.getOrDefault(plugin,new HashMap<>()).get(identifier);
        if (langLines.containsKey(key))
            line = langLines.get(key);
        else
            return AsureUtils.parseString(identifier);
        if (args.length == 0) AsureUtils.parseString(line);

        int index = 1;
        for (String arg : args) {
            line = line.replace("{"+index+"}",arg);
            index++;
        }
        return AsureUtils.parseString(line);
    }
}
