package plumeis.stacking.listener;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import plumeis.stacking.command.StackingCommand;

@Mod.EventBusSubscriber
public class RegisterCommandsListener {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event){
        new StackingCommand(event.getDispatcher());
    }
}
