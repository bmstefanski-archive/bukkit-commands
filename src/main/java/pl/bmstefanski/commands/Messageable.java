/*
MIT License

Copyright (c) 2018 Bartłomiej Stefański

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
