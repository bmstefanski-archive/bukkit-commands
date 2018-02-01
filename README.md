# bukkit-commands [![](https://jitpack.io/v/whippytools/bukkit-commands.svg)](https://jitpack.io/#whippytools/bukkit-commands)
API, through which you can create custom commands, subcommands and completers.  
Whole project based on [CommandFramework by mcardy](https://github.com/mcardy/CommandFramework) (the project was abandoned, so i decided to rewrite it)

# Using Bukkit-Commands
To build with maven, use these commands:
```shell
$ git clone https://github.com/whippytools/bukkit-commands.git
$ mvn clean package
```

You also can download this, as a dependency using the following setup.
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

```xml
	<dependency>
	    <groupId>com.github.whippytools</groupId>
	    <artifactId>bukkit-commands</artifactId>
	    <version>1.0-SNAPSHOT</version>
	</dependency>
```

# Example
```java
public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitCommands bukkitCommands = new BukkitCommands(this);

        bukkitCommands.registerCommands(new CommandTest());
    }
    
}
```

```java
public class CommandTest implements Messageable {

    @Command(name = "test", max = 1)
    @Permission("example.test")
    @GameOnly
    public void test(Arguments arguments) {
        Player player = (Player) arguments.getSender();

        sendMessage(player, "wow, amazing :D only players can see this");
    }

    @Command(name = "test.egg")
    @Permission("example.test.egg")
    @GameOnly(false)
    public void egg(Arguments arguments) {
        sendMessage(arguments.getSender(), "everyone can see this message (player and also console) YaY! :D");
    }

    @Completer("test")
    public List<String> completer(Arguments arguments) {
        if (arguments.getArgs().length == 1) {
            List<String> availableList = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().startsWith(arguments.getArgs(0).toLowerCase())) {
                    availableList.add(player.getName());
                }
            }

            Collections.sort(availableList);
            return availableList;
        }
        return null;
    }

}
```


