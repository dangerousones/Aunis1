package mrjake.aunis.sound;

import mrjake.aunis.Aunis;
import mrjake.aunis.util.EnumKeyInterface;
import mrjake.aunis.util.EnumKeyMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum SoundEventEnum implements EnumKeyInterface<Integer> {

  // ----------------------------------------------------------
  // Stargate - General

  WORMHOLE_GO(0, "wormhole_go", 1.0f),
  WORMHOLE_FLICKER(1, "wormhole_flicker", 0.5f),

  IRIS_HIT(130, "iris_hit", 1.0f),
  IRIS_CLOSING(131, "iris_closing", 1.0f),
  IRIS_OPENING(132, "iris_opening", 1.0f),

  SHIELD_HIT(133, "shield_hit", 1.0f),
  SHIELD_CLOSING(134, "shield_closing", 1.0f),
  SHIELD_OPENING(135, "shield_opening", 1.0f),


  // ----------------------------------------------------------
  // Stargate - Milky Way

  DHD_MILKYWAY_PRESS(10, "dhd_milkyway_press", 0.5f),
  DHD_MILKYWAY_PRESS_BRB(11, "dhd_milkyway_press_brb", 0.5f),

  GATE_MILKYWAY_OPEN(12, "gate_milkyway_open", 0.5f),
  GATE_MILKYWAY_CLOSE(13, "gate_milkyway_close", 0.5f),
  GATE_MILKYWAY_DIAL_FAILED(14, "gate_milkyway_dial_fail", 0.3f),
  GATE_MILKYWAY_DIAL_FAILED_COMPUTER(15, "gate_milkyway_dial_fail_computer", 1.5f),
  GATE_MILKYWAY_INCOMING(16, "gate_milkyway_incoming", 0.5f),

  GATE_MILKYWAY_CHEVRON_SHUT(17, "gate_milkyway_chevron_shut", 1.0f),
  GATE_MILKYWAY_CHEVRON_OPEN(18, "gate_milkyway_chevron_open", 1.0f),


  // ----------------------------------------------------------
  // Stargate - Universe
  GATE_UNIVERSE_DIAL_START(70, "gate_universe_dial_start", 1.0f),
  GATE_UNIVERSE_CHEVRON_LOCK(71, "gate_universe_chevron_lock", 1.0f),
  GATE_UNIVERSE_CHEVRON_TOP_LOCK(72, "gate_universe_chevron_top_lock", 1.0f),
  GATE_UNIVERSE_DIAL_FAILED(73, "gate_universe_fail", 1.0f),
  GATE_UNIVERSE_OPEN(74, "gate_universe_open", 1.0f),
  GATE_UNIVERSE_CLOSE(75, "gate_universe_close", 1.0f),

  // ----------------------------------------------------------
  // Stargate - Pegasus

  DHD_PEGASUS_PRESS(80, "dhd_pegasus_press", 0.5f),
  DHD_PEGASUS_PRESS_BRB(81, "dhd_pegasus_press_brb", 0.5f),

  GATE_PEGASUS_CHEVRON_OPEN(82, "gate_pegasus_chevron_open", 0.25f),
  //GATE_PEGASUS_CHEVRON_SHUT(81, "gate_pegasus_chevron_shut", 0.25f),
  GATE_PEGASUS_OPEN(83, "gate_pegasus_open", 0.5f),
  GATE_PEGASUS_INCOMING(84, "gate_pegasus_incoming", 0.5f),
  GATE_PEGASUS_DIAL_FAILED(85, "gate_pegasus_dial_fail", 0.3f),

  // ----------------------------------------------------------
  // Stargate - Orlin

  GATE_ORLIN_DIAL(90, "gate_orlin_dial", 1.0f),
  GATE_ORLIN_FAIL(91, "gate_orlin_dial_fail", 1.0f),
  GATE_ORLIN_BROKE(92, "gate_orlin_broke", 1.0f),

  // ----------------------------------------------------------
  // Ring transporter

  RINGS_TRANSPORT(100, "rings_transport", 0.8f),
  RINGS_CONTROLLER_BUTTON(101, "rings_controller_button", 0.5f),

  // ----------------------------------------------------------
  // Beamer
  BEAMER_START(110, "beamer_start", 1.0f),
  BEAMER_STOP(111, "beamer_stop", 0.8f),

  // ----------------------------------------------------------
  // Misc
  PAGE_FLIP(120, "page_flip", 1.0f),
  GUI_SEND_CODE_BUTTON_PRESS(121, "gui_send_code_button_press", .5f),
  UNIVERSE_DIALER_MODE_CHANGE(122, "universe_dialer_mode_change", 0.5f),
  UNIVERSE_DIALER_CONNECTED(123, "universe_dialer_connect", 0.8f),
  UNIVERSE_DIALER_START_DIAL(124, "universe_dialer_start_dial", .5f);
  // ----------------------------------------------------------
    public int id;
  public SoundEvent soundEvent;
  public float volume;

  SoundEventEnum(int id, String name, float volume) {
    this.id = id;
    this.volume = volume;
    this.soundEvent = createSoundEvent(name);
  }

  @Override
  public Integer getKey() {
    return id;
  }

  private static EnumKeyMap<Integer, SoundEventEnum> idMap = new EnumKeyMap<Integer, SoundEventEnum>(values());

  public static SoundEventEnum valueOf(int id) {
    return idMap.valueOf(id);
  }

  private static SoundEvent createSoundEvent(String name) {
    ResourceLocation resourceLocation = new ResourceLocation(Aunis.ModID, name);
    return new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
  }
}
