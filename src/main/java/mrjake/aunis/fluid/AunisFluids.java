package mrjake.aunis.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class AunisFluids {

	public static MoltenFluid moltenSiliconBlack = new MoltenFluid(
			"silicon_molten_black"
	);
	public static MoltenFluid moltenSiliconRed = new MoltenFluid(
			"silicon_molten_red"
	);
	public static MoltenFluid moltenSiliconBlue = new MoltenFluid(
			"silicon_molten_blue"
	);
	public static MoltenFluid moltenSiliconEnder = new MoltenFluid(
			"silicon_molten_ender"
	);
	public static MoltenFluid moltenSiliconYellow = new MoltenFluid(
			"silicon_molten_yellow"
	);
	public static MoltenFluid moltenSiliconWhite = new MoltenFluid(
			"silicon_molten_white"
	);

	public static MoltenFluid moltenNaquadahRaw = new MoltenFluid(
			"naquadah_molten_raw"
	);
	public static MoltenMaterial moltenNaquadahRefined = new MoltenMaterial(
			"naquadah_molten_refined",
			"NaquadahRaw"
	);
	public static MoltenMaterial moltenNaquadahAlloy = new MoltenMaterial(
			"naquadah_molten_alloy",
			"NaquadahRefined"
	);

	public static MoltenMaterial moltenTitanium = new MoltenMaterial(
			"titanium_molten",
			"Titanium"
	);
	public static MoltenMaterial moltenTrinium = new MoltenMaterial(
			"trinium_molten",
			"Trinium"
	);

	private static Fluid[] fluids = {
			moltenSiliconBlack,
			moltenSiliconRed,
			moltenSiliconBlue,
			moltenSiliconEnder,
			moltenSiliconYellow,
			moltenSiliconWhite,

			moltenNaquadahRaw,
			moltenNaquadahRefined,
			moltenNaquadahAlloy,

			moltenTitanium,
			moltenTrinium
	};

	public static Map<String, AunisBlockFluid> blockFluidMap = new HashMap<>();

	public static void registerFluids() {
		registerFluids(fluids);
	}

	public static void registerFluids(Fluid[] fluids) {
		for (Fluid fluid : fluids) {
			FluidRegistry.registerFluid(fluid);
			FluidRegistry.addBucketForFluid(fluid);

			AunisBlockFluid blockFluid = new AunisBlockFluid(fluid, fluid.getName());
			ForgeRegistries.BLOCKS.register(blockFluid);
			blockFluidMap.put(fluid.getName(), blockFluid);
		}
	}
}
