package mrjake.aunis.command;

import net.minecraft.command.CommandBase;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.Arrays;
import java.util.List;

public final class AunisCommands {

  private static final List<CommandBase> commands = Arrays.asList(
          new CommandStargateQuery(),
          new CommandPrepare(),
          new CommandStargateCloseAll(),
          new CommandStargateSetAddress(),
          new CommandPageGive(),
          new CommandStargateLinkDHD(),
          new CommandDebug(),
          new CommandFix(),
          new CommandFixNether(),
          new CommandGenerateIncoming()
  );

  public static void registerCommands(FMLServerStartingEvent event) {
    for (CommandBase command : commands) {
      event.registerServerCommand(command);
    }
  }
}
