package pl.bmstefanski.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Arguments {

    private CommandSender sender;
    private Command command;
    private String label;
    private String[] args;

    protected Arguments(CommandSender sender, Command command, String label, String[] args, int subCommand) {
        String[] modArgs = new String[args.length - subCommand];

        for (int i = 0; i < args.length - subCommand; i++) {
            modArgs[i] = args[i + subCommand];
        }

        StringBuilder builder = new StringBuilder();
        builder.append(label);
        for (int x = 0; x < subCommand; x++) {
            builder.append("." + args[x]);
        }

        String cmdLabel = builder.toString();
        this.sender = sender;
        this.command = command;
        this.label = cmdLabel;
        this.args = modArgs;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Command getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArgs(int index) {
        return args[index];
    }

    public int length() {
        return args.length;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            return null;
        }
    }
}
