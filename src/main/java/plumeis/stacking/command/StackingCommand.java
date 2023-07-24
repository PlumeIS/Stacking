package plumeis.stacking.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.w3c.dom.Text;
import plumeis.stacking.Stacking;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StackingCommand {
    public StackingCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("stacking").executes(StackingCommand::executor));
    }

    private static int executor(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer sender = context.getSource().getPlayerOrException();
            stacking(sender, 10);
            return 1;
        } catch (CommandSyntaxException ignored) {
            return 0;
        }
    }

    public static void stacking(ServerPlayer player, int radius) {
        for (ChestBlockEntity chest: searchChest(player.blockPosition(), player.getLevel(), radius)){
            Inventory inventory = player.getInventory();
            for (int pi = 0; pi < inventory.getContainerSize(); pi++) {
                ItemStack playerItem = inventory.getItem(pi);
                if (playerItem.isEmpty()){
                    continue;
                }

                if (hasSameItemInChest(playerItem, chest)){
                    int count = playerItem.getCount();
                    for (int ci = 0; ci<chest.getContainerSize(); ci++) {
                        ItemStack chestItem = chest.getItem(ci);

                        if (isSameItem(chestItem, playerItem)) {
                            if (chestItem.getCount() + count <= chestItem.getMaxStackSize()) {
                                chestItem.setCount(chestItem.getCount() + count);
                                count = 0;
                                break;
                            } else {
                                count -= (chestItem.getMaxStackSize() - chestItem.getCount());
                                chestItem.setCount(chestItem.getMaxStackSize());
                            }

                        } else if (chestItem.isEmpty()){
                            ItemStack copiedPlayerItem = playerItem.copy();
                            if (count <= copiedPlayerItem.getMaxStackSize()){
                                copiedPlayerItem.setCount(count);
                                count = 0;
                                chest.setItem(ci, copiedPlayerItem);
                                break;
                            } else {
                                count -= copiedPlayerItem.getMaxStackSize();
                                chestItem.setCount(chestItem.getMaxStackSize());
                                chest.setItem(ci, copiedPlayerItem);
                            }
                        }
                    }

                    if (count==0){
                        inventory.removeItem(inventory.getItem(pi));
                    }else {
                        playerItem.setCount(count);
                        inventory.setItem(pi, playerItem);
                    }
                }
            }
        }
    }

    public static boolean hasSameItemInChest(ItemStack itemStack, ChestBlockEntity chest){
        for (int i = 0; i < chest.getContainerSize(); i++) {
            if (isSameItem(itemStack, chest.getItem(i))){
                return true;
            }
        }
        return false;
    }

    public static boolean isSameItem(ItemStack itemStack1, ItemStack itemStack2){
        if (itemStack1.sameItemStackIgnoreDurability(itemStack2)){
            CompoundTag itemStack1Tag = itemStack1.getTag();
            CompoundTag itemStack2Tag = itemStack2.getTag();

            if (Objects.isNull(itemStack1Tag)&&Objects.isNull(itemStack2Tag)){
                return true;
            }
            if (itemStack1Tag.equals(itemStack2Tag)){
                return true;
            }
            return false;
        }
        return false;
    }

    public static List<ChestBlockEntity> searchChest(BlockPos blockPos,ServerLevel level, int radius){
        List<ChestBlockEntity> chests = new ArrayList<>();
        for (int x = blockPos.getX()-radius; x <= blockPos.getX()+radius; x++){
            for (int y = blockPos.getY()-radius; y <= blockPos.getY()+radius; y++){
                for (int z = blockPos.getZ()-radius; z <= blockPos.getZ()+radius; z++){
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof ChestBlockEntity chest){
                        chests.add(chest);
                    }
                }
            }
        }
        return chests;
    }
}
