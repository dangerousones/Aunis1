package mrjake.aunis.gui.container.zpmhub;

import io.netty.buffer.ByteBuf;
import mrjake.aunis.state.State;
import mrjake.aunis.tileentity.util.ReactorStateEnum;

public class ZPMHubContainerGuiUpdate extends State {
	public ZPMHubContainerGuiUpdate() {}

	public int fluidAmount;
	public int tankCapacity;
	public ReactorStateEnum reactorState;
	public boolean isLinked;

	public ZPMHubContainerGuiUpdate(int fluidAmount, int tankCapacity, ReactorStateEnum reactorState, boolean isLinked) {
		this.fluidAmount = fluidAmount;
		this.tankCapacity = tankCapacity;
		this.reactorState = reactorState;
		this.isLinked = isLinked;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(fluidAmount);
		buf.writeInt(tankCapacity);
		buf.writeShort(reactorState.getKey());
		buf.writeBoolean(isLinked);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		fluidAmount = buf.readInt();
		tankCapacity = buf.readInt();
		reactorState = ReactorStateEnum.valueOf(buf.readShort());
		isLinked = buf.readBoolean();
	}

}
