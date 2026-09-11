package org.saintqd.asurevelocitylib.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.saintqd.asurevelocitylib.AsureVelocityLib;

public class AsureUtils {

    public static Component parseString(String text) {
        if (text.contains("<italic>") || text.contains("<i>"))
            return MiniMessage.miniMessage().deserialize(text);
        else
            return MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static void sendDebugMessage(int selectedDebugLevel, String message) {
        int debugLevel = AsureVelocityLib.inst().getDebugLevel();
        if (debugLevel >= selectedDebugLevel) {
            AsureVelocityLib.inst().getServer().getConsoleCommandSource().sendMessage(MiniMessage.miniMessage()
                    .deserialize("<blue>["+ AsureVelocityLib.inst().getName()+" [Debug - Level "+ debugLevel + "/" + selectedDebugLevel + "] <gray>"+message));
        }
    }
}
