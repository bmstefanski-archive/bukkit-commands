package pl.bmstefanski.commands.exception;

import org.bukkit.command.CommandException;

public class InvalidLengthException extends CommandException {

    private final String name;
    private final String usage;

    public InvalidLengthException(String name, String usage) {
        this.name = name;
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }
}
