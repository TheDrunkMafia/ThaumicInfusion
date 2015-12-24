package drunkmafia.thaumicinfusion.common.command;

import drunkmafia.thaumicinfusion.common.ThaumicInfusion;
import drunkmafia.thaumicinfusion.common.world.ChunkData;
import drunkmafia.thaumicinfusion.common.world.TIWorldData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.ChunkCoordIntPair;

public class CheckInstability extends CommandBase {

    @Override
    public String getName() {
        return ThaumicInfusion.translate("check.instability");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "check.instability.usage";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        TIWorldData worldData = TIWorldData.getWorldData(sender.getEntityWorld());
        ChunkCoordIntPair playerChunk = new ChunkCoordIntPair(sender.getPosition().getX() >> 4, sender.getPosition().getZ() >> 4);
        ChunkData chunkData = worldData.chunkDatas.get(playerChunk.getCenterXPos(), playerChunk.getCenterZPosition(), null);

        sender.addChatMessage(new ChatComponentText("Instability in chunk (" + playerChunk.getCenterXPos() + ", " + playerChunk.getCenterZPosition() + " is " + (chunkData != null ? chunkData.instability : 0)));
    }
}