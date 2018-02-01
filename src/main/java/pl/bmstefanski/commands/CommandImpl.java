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
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CommandImpl extends Command {

    protected CompleterImpl completer;
    private CommandExecutor executor;
    private final Plugin plugin;

    protected CommandImpl(String label, CommandExecutor executor, Plugin owner) {
        super(label);
        this.executor = executor;
        this.plugin = owner;
        this.usageMessage = "";
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean success;

        if (!plugin.isEnabled()) {
            return false;
        }

        if (!testPermission(sender)) {
            return true;
        }

        try {
            success = executor.onCommand(sender, this, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }

        if (!success && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }

        return success;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws CommandException, IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        List<String> completions = null;
        try {

            if (completer != null) {
                completions = completer.onTabComplete(sender, this, alias, args);
            }

            if (completions == null && executor instanceof TabCompleter) {
                completions = ((TabCompleter) executor).onTabComplete(sender, this, alias, args);
            }

        } catch (Throwable ex) {

            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');

            for (String arg : args) {
                message.append(arg).append(' ');
            }

            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(plugin.getDescription().getFullName());

            throw new CommandException(message.toString(), ex);
        }

        if (completions == null) {
            return super.tabComplete(sender, alias, args);
        }

        return completions;
    }
}
