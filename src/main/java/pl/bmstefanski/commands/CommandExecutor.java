package pl.bmstefanski.commands;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {

    void execute(CommandSender commandSender, CommandArguments arguments);

}
