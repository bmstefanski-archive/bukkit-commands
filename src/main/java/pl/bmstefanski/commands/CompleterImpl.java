package pl.bmstefanski.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleterImpl implements TabCompleter {

    private Map<String, Map.Entry<Method, Object>> completers = new HashMap<>();

    public void addCompleter(String label, Method m, Object obj) {
        completers.put(label, new AbstractMap.SimpleEntry<>(m, obj));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {

            StringBuilder builder = new StringBuilder();
            builder.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                if (!args[x].equals("") && !args[x].equals(" ")) {
                    builder.append("." + args[x].toLowerCase());
                }
            }
            String cmdLabel = builder.toString();
            if (completers.containsKey(cmdLabel)) {
                Map.Entry<Method, Object> entry = completers.get(cmdLabel);
                try {
                    return (List<String>) entry.getKey().invoke(entry.getValue(),
                            new Arguments(sender, command, label, args, cmdLabel.split("\\.").length - 1));
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
