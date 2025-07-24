package net.oredebug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {

    private static final File configFile = new File("config/oredebug.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static class ConfigData {
        public Set<String> blockIds = new HashSet<>();
        public int scanRange = 24;
    }

    public static void save() {
        try {
            ConfigData data = new ConfigData();
            data.blockIds = OreDebugClient.getVisibleBlockIds();
            data.scanRange = OreDebugClient.scanRange;

            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(configFile);
            gson.toJson(data, writer);
            writer.close();
        } catch (Exception e) {
            System.err.println("[OreDebug] Nie można zapisać konfiguracji: " + e.getMessage());
        }
    }

    public static void load() {
        try {
            if (!configFile.exists()) return;

            FileReader reader = new FileReader(configFile);
            ConfigData data = gson.fromJson(reader, ConfigData.class);
            reader.close();

            OreDebugClient.getVisibleBlockIds().clear();
            OreDebugClient.getVisibleBlockIds().addAll(data.blockIds);
            OreDebugClient.scanRange = data.scanRange;
        } catch (Exception e) {
            System.err.println("[OreDebug] Nie można wczytać konfiguracji: " + e.getMessage());
        }
    }
}
