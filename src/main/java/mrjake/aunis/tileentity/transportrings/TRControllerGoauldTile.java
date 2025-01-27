package mrjake.aunis.tileentity.transportrings;

import mrjake.aunis.renderer.biomes.BiomeOverlayEnum;
import mrjake.aunis.renderer.transportrings.TRControllerGoauldRenderer;

public class TRControllerGoauldTile extends TRControllerAbstractTile {
    // todo(Mine): controller

    @Override
    public void onLoad(){
        if (world.isRemote) {
            setRenderer(new TRControllerGoauldRenderer(this));
            setBiomeOverlay(BiomeOverlayEnum.updateBiomeOverlay(world, pos, SUPPORTED_OVERLAYS));
        }
    }
}
