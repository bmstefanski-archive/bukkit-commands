package pl.bmstefanski.commands.exception;

import org.bukkit.command.CommandException;

public class PermissionException extends CommandException {

    private final String permission;

    public PermissionException(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
