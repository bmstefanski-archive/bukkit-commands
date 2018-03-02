# bukkit-commands [![](https://jitpack.io/v/whippytools/bukkit-commands.svg)](https://jitpack.io/#whippytools/bukkit-commands)
API, through which you can create custom commands.  

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
	    <version>1.4</version>
	</dependency>
```

# Example
```java
public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitCommands bukkitCommands = new BukkitCommands(this);

        bukkitCommands.register(new TestCommand());
    }

}
```

```java
public class TestCommand implements CommandExecutor {

    @Command(name = "test", max = 1)
    @Permission("some.permission")
    @GameOnly
    public void execute(CommandSender commandSender, CommandArguments commandArguments) {

        commandSender.sendMessage("hihi");

        if (commandArguments.getParam(0).equalsIgnoreCase("second")) {
            commandSender.sendMessage("hihi, second.");
        }

    }

}
```


