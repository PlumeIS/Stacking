package plumeis.stacking.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.lwjgl.glfw.GLFW;
import plumeis.stacking.Stacking;

import static plumeis.stacking.command.StackingCommand.stacking;

@Mod.EventBusSubscriber
public class KeyPressListener {
    private static boolean isKKeyPressed = false;

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        if (Stacking.stackingKey.isDown()) {
            isKKeyPressed = true;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (isKKeyPressed) {
                // 获取当前玩家对象
                LocalPlayer player = Minecraft.getInstance().player;
                player.chat("/stacking");
                isKKeyPressed = false; // 重置按键状态，避免多次触发
            }
        }
    }
}
