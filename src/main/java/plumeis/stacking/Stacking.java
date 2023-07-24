package plumeis.stacking;

import ca.weblite.objc.Client;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.util.thread.NamedThreadFactory;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import plumeis.stacking.listener.RegisterCommandsListener;

import java.util.concurrent.ThreadFactory;


@Mod(Stacking.MOD_ID)
public class Stacking {
    public static final String MOD_ID = "stacking";

    public static final String MOD_NAME = "Stacking";
    public static KeyMapping stackingKey =  new KeyMapping("key.stacking", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "name.stacking");
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ThreadFactory threadFactory = new NamedThreadFactory(MOD_NAME);

    public Stacking() {
        MinecraftForge.EVENT_BUS.register(new RegisterCommandsListener());
        ClientRegistry.registerKeyBinding(stackingKey);
    }
}
