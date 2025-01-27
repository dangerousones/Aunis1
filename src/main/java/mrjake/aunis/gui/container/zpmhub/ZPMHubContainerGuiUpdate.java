package mrjake.aunis.gui.container.zpmhub;

import io.netty.buffer.ByteBuf;
import mrjake.aunis.state.State;
import mrjake.aunis.tileentity.util.ReactorStateEnum;

public class ZPMHubContainerGuiUpdate extends State {
	public ZPMHubContainerGuiUpdate() {}

	public int zpmsCount;
	public long energyTransferedLastTick;

	public ZPMHubContainerGuiUpdate(int zpmsCount, long energyTransferedLastTick) {
		this.zpmsCount = zpmsCount;
		this.energyTransferedLastTick = energyTransferedLastTick;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(zpmsCount);
		buf.writeLong(energyTransferedLastTick);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		zpmsCount = buf.readInt();
		energyTransferedLastTick = buf.readLong();
	}

}
