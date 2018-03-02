package pl.bmstefanski.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandArguments {

    private final String[] args;
    private final List<String> params;

    public CommandArguments(String[] args) {
        this.args = args;
        this.params = new ArrayList<>(Arrays.asList(args));
    }

    public String getParam(int index) {
        if (this.params.size() <= (index)) {
            return "";
        }

        return this.params.get(index);
    }

    public List<String> getParams() {
        return new ArrayList<>(params);
    }

    public int getSize() {
        return this.params.size();
    }

    public int getArgs() {
        return args.length;
    }

}
