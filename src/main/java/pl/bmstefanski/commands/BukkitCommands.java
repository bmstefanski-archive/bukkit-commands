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

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import pl.bmstefanski.commands.annotation.Command;
import pl.bmstefanski.commands.annotation.GameOnly;
import pl.bmstefanski.commands.annotation.Permission;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BukkitCommands {

    private CommandMap commandMap;

    public BukkitCommands() {

        if (commandMap == null) {
            try {
                Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                this.commandMap = (CommandMap) field.get(Bukkit.getServer());
                field.setAccessible(false);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void register(CommandExecutor commandExecutor) {
        Validate.notNull(commandExecutor);

        for (Method method : commandExecutor.getClass().getDeclaredMethods()) {

            if (method.isAnnotationPresent(Command.class)) {

                Command commandAnnotation = method.getAnnotation(Command.class);
                GameOnly gameOnlyAnnotation = method.getAnnotation(GameOnly.class);
                Permission permissionAnnotation = method.getAnnotation(Permission.class);

                org.bukkit.command.Command command = new BukkitCommand(
                        commandAnnotation.name(),
                        commandAnnotation.description(),
                        commandAnnotation.usage(),
                        Arrays.asList(commandAnnotation.aliases()
                )) {

                    @Override
                    public boolean execute(CommandSender commandSender, String s, String[] strings) {

                        CommandArguments commandArguments = new CommandArguments(strings);

                        if (!commandSender.hasPermission(permissionAnnotation.value())) {
                            commandSender.sendMessage(ChatColor.RED + "Nie masz uprawnien do wykonania tego polecenia! "
                                    + ChatColor.GRAY + "(" + permissionAnnotation.value() + ")");
                            return true;
                        }

                        if (gameOnlyAnnotation.value() && !(commandSender instanceof Player)) {
                            commandSender.sendMessage(ChatColor.RED + "Ta komenda moze zostac wykonana tylko przez gracza!");
                            return true;
                        }

                        if ((commandArguments.getSize() < commandAnnotation.min()) || (commandArguments.getSize() > commandAnnotation.max())) {
                            commandSender.sendMessage(ChatColor.RED + "Poprawne uzycie komendy: /" + ChatColor.GRAY
                                    + commandAnnotation.name() + " " + commandAnnotation.usage());
                            return true;
                        }

                        commandExecutor.execute(commandSender, commandArguments);
                        return true;
                    }

                };

                this.commandMap.register("", command);

            }

        }

    }

}
