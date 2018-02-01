package pl.bmstefanski.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.bmstefanski.commands.exception.IllegalSenderException;
import pl.bmstefanski.commands.exception.InvalidLengthException;
import pl.bmstefanski.commands.exception.PermissionException;

public class ErrorHandler {

    public void handlePermissionException(PermissionException exception, CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Nie masz uprawnien do wykonania tego polecenia! " + ChatColor.GRAY +  "(" + exception.getPermission() + ")");
    }

    public void handleIllegalSenderException(IllegalSenderException exception, CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Ta komenda moze zostac wykonana tylko przez gracza!");
    }

    public void handleInvalidLengthException(InvalidLengthException exception, CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Poprawne uzycie: /" + StringUtils.replace(exception.getName(), ".", " ") + " " + exception.getUsage());
    }

}
