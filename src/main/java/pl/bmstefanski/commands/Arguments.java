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
