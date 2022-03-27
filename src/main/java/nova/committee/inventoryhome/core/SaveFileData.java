package nova.committee.inventoryhome.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fml.loading.FMLPaths;
import nova.committee.inventoryhome.InventoryHome;
import nova.committee.inventoryhome.util.UserUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 11:36
 * Version: 1.0
 */
public class SaveFileData {
    public static Map<String, SaveEntity> homeMap = new ConcurrentHashMap<>();
    static String path = FMLPaths.CONFIGDIR.get().resolve(InventoryHome.MOD_ID).toString();
    static File directory = new File(path);
    static File jsonConfig = new File(path, "saveFile.json");
    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

    public static void init() {
        homeMap = initFile();
    }

    private static Map<String, SaveEntity> initFile() {

        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            if (!jsonConfig.exists() && jsonConfig.createNewFile()) {
                Map<String, SaveEntity> homeMap = new ConcurrentHashMap<>();
                homeMap.put("DEFAULT", new SaveEntity(Util.NIL_UUID, BlockPos.ZERO));
                FileUtils.write(jsonConfig, gson.toJson(homeMap, new TypeToken<Map<String, SaveEntity>>() {
                }.getType()), StandardCharsets.UTF_8);
            }
            return gson.fromJson(FileUtils.readFileToString(jsonConfig, StandardCharsets.UTF_8), new TypeToken<Map<String, SaveEntity>>() {
            }.getType());
        } catch (IOException e) {
            InventoryHome.LOGGER.error("Error load data.");
        }
        return null;
    }

    public static void saveFile() {
        try {
            FileUtils.write(jsonConfig, gson.toJson(homeMap, new TypeToken<Map<String, SaveEntity>>() {
            }.getType()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void addElements(String name, BlockPos pos) {
        if (homeMap.containsKey(name)) {
            homeMap.replace(name, new SaveEntity(UserUtil.getUUIDByName(name), pos));
        } else {
            homeMap.put(name, new SaveEntity(UserUtil.getUUIDByName(name), pos));
        }
        saveFile();
    }

    private static SaveEntity getSaveEntityById(List<SaveEntity> list, UUID uuid) {
        for (SaveEntity marketItem : list) {
            if (marketItem.uuid == uuid) {
                return marketItem;
            }
        }
        return null;
    }

    public static BlockPos getPoseFromList(List<SaveEntity> list, UUID uuid) {

        BlockPos pos = BlockPos.ZERO;
        SaveEntity marketItem = getSaveEntityById(list, uuid);

        if (marketItem != null) {
            return marketItem.pos;
        }

        return pos;
    }

    public static class SaveEntity {
        public UUID uuid;
        public BlockPos pos;

        public SaveEntity(UUID uuid, BlockPos pos) {
            this.pos = pos;
            this.uuid = uuid;
        }
    }

}
