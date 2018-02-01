package pl.bmstefanski.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface Messageable {

    default String fixColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    default void sendMessage(Player player, String message) {
        player.sendMessage(fixColor(message));
    }

    default void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(fixColor(message));
    }

    default void sendMessage(CommandSender sender, String... messages) {
        for (String message : messages) {
            sender.sendMessage(fixColor(message));
        }
    }

    default void sendMessage(Player player, String... messages) {
        for (String message : messages) {
            player.sendMessage(fixColor(message));
        }
    }

    default String listToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        String result = null;

        for (String string : list) {
            stringBuilder.append(string + "\n");
        }

        return fixColor(stringBuilder.toString());
    }
}
