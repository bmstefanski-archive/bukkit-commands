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
