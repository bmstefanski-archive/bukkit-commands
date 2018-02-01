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

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import pl.bmstefanski.commands.annotation.Command;
import pl.bmstefanski.commands.annotation.Completer;
import pl.bmstefanski.commands.annotation.GameOnly;
import pl.bmstefanski.commands.annotation.Permission;
import pl.bmstefanski.commands.exception.IllegalSenderException;
import pl.bmstefanski.commands.exception.InvalidLengthException;
import pl.bmstefanski.commands.exception.PermissionException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitCommands implements CommandExecutor {

    private Map<String, Map.Entry<Method, Object>> commandMap = new HashMap<>();
    private CommandMap map;
    private Plugin plugin;

    public BukkitCommands(Plugin plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {

            SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();

            try {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                map = (CommandMap) field.get(manager);
                field.setAccessible(false);
            } catch (IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuilder builder = new StringBuilder();
            builder.append(label.toLowerCase());

            for (int x = 0; x < i; x++) {
                builder.append("." + args[x].toLowerCase());
            }

            String cmdLabel = builder.toString();

            if (commandMap.containsKey(cmdLabel)) {
                Method method = commandMap.get(cmdLabel).getKey();
                Object methodObject = commandMap.get(cmdLabel).getValue();

                Command commandAnnotation = method.getAnnotation(Command.class);
                Permission permissionAnnotation = method.getAnnotation(Permission.class);
                GameOnly gameOnlyAnnotation = method.getAnnotation(GameOnly.class);

                Arguments arguments = new Arguments(sender, cmd, label, args, cmdLabel.split("\\.").length - 1);
                ErrorHandler errorHandler = new ErrorHandler();

                try {
                    if (!permissionAnnotation.value().equals("") && !sender.hasPermission(permissionAnnotation.value())) {
                        throw new PermissionException(permissionAnnotation.value());
                    }
                } catch (PermissionException ex) {
                    errorHandler.handlePermissionException(ex, sender);
                    return true;
                }

                try {
                    if (gameOnlyAnnotation.value() && !(sender instanceof Player)) {
                        throw new IllegalSenderException();
                    }
                } catch (IllegalSenderException ex) {
                    errorHandler.handleIllegalSenderException(ex, sender);
                    return true;
                }

                try {
                    if ((arguments.getArgs().length < commandAnnotation.min()) || (arguments.getArgs().length > commandAnnotation.max())) {
                        throw new InvalidLengthException(commandAnnotation.name(), commandAnnotation.usage());
                    }
                } catch (InvalidLengthException ex) {
                    errorHandler.handleInvalidLengthException(ex, sender);
                    return true;
                }


                try {
                    method.invoke(methodObject, new Arguments(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        this.defaultCommand(new Arguments(sender, cmd, label, args, 0));
        return true;
    }

    public void registerCommands(Object obj) {
        for (Method method : obj.getClass().getMethods()) {

            if (method.getAnnotation(Command.class) != null) {
                Command commandAnnotation = method.getAnnotation(Command.class);

                if (method.getParameterTypes().length > 1 || method.getParameterTypes()[0] != Arguments.class) {
                    System.out.println("Unable to register command " + method.getName() + ". Unexpected method arguments");
                    continue;
                }

                registerCommand(commandAnnotation, commandAnnotation.name(), method, obj);

                for (String alias : commandAnnotation.aliases()) {
                    registerCommand(commandAnnotation, alias, method, obj);
                }
            }

            else if (method.getAnnotation(Completer.class) != null) {

                Completer completerAnnotation = method.getAnnotation(Completer.class);

                if (method.getParameterTypes().length > 1 || method.getParameterTypes().length == 0
                        || method.getParameterTypes()[0] != Arguments.class) {
                    System.out.println("Unable to register tab completer " + method.getName()
                            + ". Unexpected method arguments");
                    continue;
                }

                if (method.getReturnType() != List.class) {
                    System.out.println("Unable to register tab completer " + method.getName() + ". Unexpected return type");
                    continue;
                }

                registerCompleter(completerAnnotation.value(), method, obj);
            }
        }
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {

        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));

        String cmdLabel = label.split("\\.")[0].toLowerCase();

        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new CommandImpl(cmdLabel, this, plugin);
            map.register(plugin.getName(), cmd);
        }

        if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setDescription(command.description());
        }

        if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
            map.getCommand(cmdLabel).setUsage(command.usage());
        }
    }

    public void registerCompleter(String label, Method m, Object obj) {

        String cmdLabel = label.split("\\.")[0].toLowerCase();

        if (map.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command command = new CommandImpl(cmdLabel, this, plugin);
            map.register(plugin.getName(), command);
        }

        if (map.getCommand(cmdLabel) instanceof BukkitCommand) {
            CommandImpl command = (CommandImpl) map.getCommand(cmdLabel);

            if (command.completer == null) {
                command.completer = new CompleterImpl();
            }

            command.completer.addCompleter(label, m, obj);
        }

        else if (map.getCommand(cmdLabel) instanceof PluginCommand) {
            try {
                Object command = map.getCommand(cmdLabel);
                Field field = command.getClass().getDeclaredField("completer");
                field.setAccessible(true);

                if (field.get(command) == null) {
                    CompleterImpl completer = new CompleterImpl();
                    completer.addCompleter(label, m, obj);
                    field.set(command, completer);
                }

                else if (field.get(command) instanceof CompleterImpl) {
                    CompleterImpl completer = (CompleterImpl) field.get(command);
                    completer.addCompleter(label, m, obj);
                }

                else {
                    System.out.println("Unable to register tab completer " + m.getName()
                            + ". A tab completer is already registered for that command!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void defaultCommand(Arguments arguments) {
        arguments.getSender().sendMessage(arguments.getLabel() + " is not handled! Oh noes!");
    }
}
