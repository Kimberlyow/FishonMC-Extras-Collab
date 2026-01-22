package io.github.markassk.fishonmcextras.FOMC;

import io.github.markassk.fishonmcextras.FOMC.Types.Defaults;
import io.github.markassk.fishonmcextras.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static io.github.markassk.fishonmcextras.FOMC.Types.Defaults.DEFAULT_COLOR;

public enum Constant {
	// Fish Size
	BABY("baby", Text.literal("ʙᴀʙʏ").withColor(0x468CE7), 0x468CE7),
	JUVENILE("juvenile", Text.literal("ᴊᴜᴠᴇɴɪʟᴇ").withColor(0x22EA08), 0x22EA08),
	ADULT("adult", Text.literal("ᴀᴅᴜʟᴛ").withColor(0x1C7DA0), 0x1C7DA0),
	LARGE("large", Text.literal("ʟᴀʀɢᴇ").withColor(0xFF9000), 0xFF9000),
	GIGANTIC("gigantic", Text.literal("ɢɪɢᴀɴᴛɪᴄ").withColor(0xAF3333), 0xAF3333),

	// Rarity
	COMMON("common", Text.literal("\uF033").formatted(Formatting.WHITE), 0xFFFFFF),
	RARE("rare", Text.literal("\uF034").formatted(Formatting.WHITE), 0x2B85C4),
	EPIC("epic", Text.literal("\uF035").formatted(Formatting.WHITE), 0x1CD832),
	LEGENDARY("legendary", Text.literal("\uF036").formatted(Formatting.WHITE), 0xD98103),
	MYTHICAL("mythical", Text.literal("\uF037").formatted(Formatting.WHITE), 0xC93832),

	// Event Rarities
	SPECIAL("special", Text.literal("\uF092").formatted(Formatting.WHITE), 0xDD7ACF),

	// Location
	SPAWNHUB("spawnhub", Text.literal("Cypress Lake").withColor(0x5CAE65), DEFAULT_COLOR),
	CYPRESS_LAKE("spawn", Text.literal("Cypress Lake").withColor(0x5CAE65), Defaults.DEFAULT_COLOR),
	KENAI_RIVER("kenai", Text.literal("Kenai River").withColor(0x68D499), Defaults.DEFAULT_COLOR),
	LAKE_BIWA("biwa", Text.literal("Lake Biwa").withColor(0xFBC0FA), Defaults.DEFAULT_COLOR),
	MURRAY_RIVER("murray", Text.literal("Murray River").withColor(0xCD5916), Defaults.DEFAULT_COLOR),
	EVERGLADES("everglades", Text.literal("Everglades").withColor(0x2EBB8D), Defaults.DEFAULT_COLOR),
	KEY_WEST("keywest", Text.literal("Key West").withColor(0xFBF17C), Defaults.DEFAULT_COLOR),
	TOLEDO_BEND("toledobend", Text.literal("Toledo Bend Reservoir").withColor(0x99A7D0), Defaults.DEFAULT_COLOR),
	GREAT_LAKES("greatlakes", Text.literal("Great Lakes").withColor(0x3CABF3), Defaults.DEFAULT_COLOR),
	DANUBE_RIVER("danube", Text.literal("Danube River").withColor(0xFBC598), Defaults.DEFAULT_COLOR),
	OIL_RIG("oilrig", Text.literal("Oil Rig").withColor(0xFCEB47), Defaults.DEFAULT_COLOR),
	AMAZON_RIVER("amazon", Text.literal("Amazon River").withColor(0x3EA729), Defaults.DEFAULT_COLOR),
	MEDITERRANEAN_SEA("mediterranean", Text.literal("Mediterranean Sea").withColor(0xF0FB37), Defaults.DEFAULT_COLOR),
	CAPE_COD("capecod", Text.literal("Cape Cod").withColor(0xBBF5FB), Defaults.DEFAULT_COLOR),
	HAWAII("hawaii", TextHelper.concat(
			Text.literal("H").withColor(0xFB933B),
			Text.literal("a").withColor(0xFCB140),
			Text.literal("w").withColor(0xEACD4D),
			Text.literal("a").withColor(0xB2E66C),
			Text.literal("i").withColor(0x75F0A6),
			Text.literal("i").withColor(0x35F4EF)), Defaults.DEFAULT_COLOR),
	CAIRNS("cairns", Text.literal("Cairns").withColor(0xA1C2FB), Defaults.DEFAULT_COLOR),
	CREW_ISLAND("crewisland", Text.literal("Crew Island"), Defaults.DEFAULT_COLOR),
	UNKNOWN("unknown", Text.literal("Unknown"), Defaults.DEFAULT_COLOR),

	// Pet Base
	LOCATION_BASE("lbase", Text.empty(), Defaults.DEFAULT_COLOR),
	CLIMATE_BASE("cbase", Text.empty(), Defaults.DEFAULT_COLOR),

	// Variants
	NORMAL("normal", Text.empty(), Defaults.DEFAULT_COLOR),
	ALBINO("albino", Text.literal("\uF041").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
	MELANISTIC("melanistic", Text.literal("\uF042").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
	TROPHY("trophy", Text.literal("\uF043").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
	FABLED("fabled", Text.literal("\uF044").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),

	// Event Variants
	ALTERNATE("alternate", Text.literal("\uF098").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
	SPOOKY("spooky", Text.literal("\uF102").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
	FROZEN("frozen", Text.literal("\uF179").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),

	// Rare Catches
	LIGHTNING_BOTTLE("lightning bottle", TextHelper.concat(
			Text.literal("L").withColor(0xEFE038),
			Text.literal("i").withColor(0xEFC32C),
			Text.literal("g").withColor(0xEABC34),
			Text.literal("h").withColor(0xE4B43C),
			Text.literal("t").withColor(0xEABC34),
			Text.literal("n").withColor(0xEFC32C),
			Text.literal("i").withColor(0xEFE038),
			Text.literal("n").withColor(0xEFFC44),
			Text.literal("g in a Bottle").withColor(0xE4B43C)), Defaults.DEFAULT_COLOR),
	INFUSION_CAPSULE("infusion capsule", Text.literal("Infusion Capsule").formatted(Formatting.WHITE),
			Defaults.DEFAULT_COLOR),
	SHARD("shard", Text.literal("Shard").formatted(Formatting.GOLD), Defaults.DEFAULT_COLOR),
	PROSPECTING_AMULET("prospecting amulet", TextHelper.concat(
			Text.literal("P").withColor(0xE4CC2B),
			Text.literal("r").withColor(0xE3CE2D),
			Text.literal("o").withColor(0xE3D030),
			Text.literal("s").withColor(0xE2D332),
			Text.literal("p").withColor(0xE1D534),
			Text.literal("e").withColor(0xE1D737),
			Text.literal("c").withColor(0xE0D939),
			Text.literal("t").withColor(0xDFDC3B),
			Text.literal("i").withColor(0xDFDE3E),
			Text.literal("n").withColor(0xDEE040),
			Text.literal("g ").withColor(0xDEE243),
			Text.literal("A").withColor(0xDCE747),
			Text.literal("m").withColor(0xDCE94A),
			Text.literal("u").withColor(0xDBEB4C),
			Text.literal("l").withColor(0xDAEE4E),
			Text.literal("e").withColor(0xDAF051),
			Text.literal("t").withColor(0xD9F253)), Defaults.DEFAULT_COLOR),
	// Bigfoot Drops
	BIGFOOT_FUR("bigfoot fur", Text.literal("Bigfoot Fur").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),
	BIGFOOT_TOOTH("bigfoot tooth", Text.literal("Bigfoot Tooth").formatted(Formatting.WHITE), Defaults.DEFAULT_COLOR),

	// Pet Rating
	SICKLY("sickly", Text.literal("sɪᴄᴋʟʏ").withColor(0xFF74403B), 0xFF74403B),
	BAD("bad", Text.literal("ʙᴀᴅ").withColor(0xFFFF5555), 0xFFFF5555),
	BELOW_AVERAGE("below_average", Text.literal("ʙᴇʟᴏᴡ ᴀᴠᴇʀᴀɢᴇ").withColor(0xFFFCFC54), 0xFFFCFC54),
	AVERAGE("average", Text.literal("ᴀᴠᴇʀᴀɢᴇ").withColor(0xFFFCA800), 0xFFFCA800),
	GOOD("good", Text.literal("ɢᴏᴏᴅ").withColor(0xFF54FC54), 0xFF54FC54),
	GREAT("great", Text.literal("ɢʀᴇᴀᴛ").withColor(0xFF00A800), 0xFF00A800),
	EXCELLENT("excellent", Text.literal("ᴇxᴄᴇʟʟᴇɴᴛ").withColor(0xFF54FCFC), 0xFF54FCFC),
	AMAZING("amazing", Text.literal("ᴀᴍᴀᴢɪɴɢ").withColor(0xFFFC54FC), 0xFFFC54FC),
	PERFECT("perfect", Text.literal("ᴘᴇʀꜰᴇᴄᴛ").withColor(0xFFA800A8), 0xFFA800A8),

	// Pets
	PET("pet", Text.literal("Pet").withColor(0xFD95F6), Defaults.DEFAULT_COLOR),
	BULLFROG("bullfrog", TextHelper.concat(
			Text.literal("B").withColor(0x84CA54),
			Text.literal("u").withColor(0x7FC054),
			Text.literal("l").withColor(0x79B754),
			Text.literal("l").withColor(0x74AD54),
			Text.literal("f").withColor(0x6FA354),
			Text.literal("r").withColor(0x6A9954),
			Text.literal("o").withColor(0x649054),
			Text.literal("g Pet").withColor(0x5F8654)), Defaults.DEFAULT_COLOR),
	BEAR("bear", TextHelper.concat(
			Text.literal("B").withColor(0x593E3B),
			Text.literal("e").withColor(0x583C3A),
			Text.literal("a").withColor(0x573B3A),
			Text.literal("r Pet").withColor(0x563939)), Defaults.DEFAULT_COLOR),
	FOX("fox", TextHelper.concat(
			Text.literal("F").withColor(0xF99752),
			Text.literal("o").withColor(0xF2A75D),
			Text.literal("x Pet").withColor(0xEBB668)), Defaults.DEFAULT_COLOR),
	KANGAROO("kangaroo", TextHelper.concat(
			Text.literal("K").withColor(0xD19E58),
			Text.literal("a").withColor(0xD1A460),
			Text.literal("n").withColor(0xD1AA69),
			Text.literal("g").withColor(0xD1B071),
			Text.literal("a").withColor(0xD1B779),
			Text.literal("r").withColor(0xD1BD81),
			Text.literal("o").withColor(0xD1C38A),
			Text.literal("o Pet").withColor(0xD1C992)), Defaults.DEFAULT_COLOR),
	MARSH_RABBIT("marshrabbit", TextHelper.concat(
			Text.literal("M").withColor(0x968F73),
			Text.literal("a").withColor(0x928E71),
			Text.literal("r").withColor(0x8D8D70),
			Text.literal("s").withColor(0x898C6E),
			Text.literal("h").withColor(0x858B6D),
			Text.literal(""),
			Text.literal("R").withColor(0x7C896A),
			Text.literal("a").withColor(0x788868),
			Text.literal("b").withColor(0x748767),
			Text.literal("b").withColor(0x708665),
			Text.literal("i").withColor(0x6B8564),
			Text.literal("t Pet").withColor(0x678462)), Defaults.DEFAULT_COLOR),
	SEA_TURTLE("seaturtle", TextHelper.concat(
			Text.literal("S").withColor(0x69BE7B),
			Text.literal("e").withColor(0x71C27E),
			Text.literal("a").withColor(0x79C781),
			Text.literal(""),
			Text.literal("T").withColor(0x89D087),
			Text.literal("u").withColor(0x92D48A),
			Text.literal("r").withColor(0x9AD98D),
			Text.literal("t").withColor(0xA2DD90),
			Text.literal("l").withColor(0xAAE293),
			Text.literal("e Pet").withColor(0xB2E696)), Defaults.DEFAULT_COLOR),
	DUCK("duck", TextHelper.concat(
			Text.literal("D").withColor(0xEBEAA8),
			Text.literal("u").withColor(0xE2E2A5),
			Text.literal("c").withColor(0xD9DAA3),
			Text.literal("k Pet").withColor(0xD0D2A0)), Defaults.DEFAULT_COLOR),
	EAGLE("eagle", TextHelper.concat(
			Text.literal("E").withColor(0xBEBEBE),
			Text.literal("a").withColor(0xBAB8B6),
			Text.literal("g").withColor(0xB5B3AE),
			Text.literal("l").withColor(0xB1ADA5),
			Text.literal("e Pet ").withColor(0xACA79D)), Defaults.DEFAULT_COLOR),
	WOLF("wolf", TextHelper.concat(
			Text.literal("W").withColor(0x818587),
			Text.literal("o").withColor(0x7C8083),
			Text.literal("l").withColor(0x787B7F),
			Text.literal("f Pet").withColor(0x73767B)), Defaults.DEFAULT_COLOR),
	PELICAN("pelican", TextHelper.concat(
			Text.literal("P").withColor(0xD9CBA6),
			Text.literal("e").withColor(0xDFC59B),
			Text.literal("l").withColor(0xE6BE90),
			Text.literal("i").withColor(0xECB886),
			Text.literal("c").withColor(0xF2B27B),
			Text.literal("a").withColor(0xF9AB70),
			Text.literal("n Pet").withColor(0xFFA565)), Defaults.DEFAULT_COLOR),
	CAPYBARA("capybara", TextHelper.concat(
			Text.literal("C").withColor(0x725E39),
			Text.literal("a").withColor(0x7F663F),
			Text.literal("p").withColor(0x8C6E45),
			Text.literal("y").withColor(0x99764B),
			Text.literal("b").withColor(0xA77D51),
			Text.literal("a").withColor(0xB48557),
			Text.literal("r").withColor(0xC18D5D),
			Text.literal("a Pet").withColor(0xCE9563)), Defaults.DEFAULT_COLOR),
	LYNX("lynx", TextHelper.concat(
			Text.literal("L").withColor(0xA1A278),
			Text.literal("y").withColor(0xA4A571),
			Text.literal("n").withColor(0xA6A96A),
			Text.literal("x Pet").withColor(0xA9AC63)), Defaults.DEFAULT_COLOR),
	SHARK("shark", TextHelper.concat(
			Text.literal("S").withColor(0x6C8BE4),
			Text.literal("h").withColor(0x7190DB),
			Text.literal("a").withColor(0x7694D2),
			Text.literal("r").withColor(0x7B99C9),
			Text.literal("k Pet").withColor(0x809DC0)), Defaults.DEFAULT_COLOR),
	DOLPHIN("dolphin", TextHelper.concat(
			Text.literal("D").withColor(0xBAC7E4),
			Text.literal("o").withColor(0xB8C7DE),
			Text.literal("l").withColor(0xB6C6D8),
			Text.literal("p").withColor(0xB4C6D2),
			Text.literal("h").withColor(0xB1C6CB),
			Text.literal("i").withColor(0xAFC5C5),
			Text.literal("n Pet").withColor(0xADC5BF)), Defaults.DEFAULT_COLOR),
	KOALA("koala", TextHelper.concat(
			Text.literal("K").withColor(0xAEBFD1),
			Text.literal("o").withColor(0xB1C4D0),
			Text.literal("a").withColor(0xB3C8CF),
			Text.literal("l").withColor(0xB6CDCE),
			Text.literal("a Pet").withColor(0xB8D1CD)), Defaults.DEFAULT_COLOR),

	// Water Types
	FRESHWATER("freshwater", Text.literal("Freshwater").withColor(0x3F87EF), DEFAULT_COLOR),
	SALTWATER("saltwater", Text.literal("Saltwater").withColor(0x86D9E6), DEFAULT_COLOR),
	ANY_WATER("any", Text.literal("Any"), DEFAULT_COLOR),
	GLOBAL_WATER("global", Text.literal("Anywhere"), DEFAULT_COLOR),

	// Text Tags
	TEXTCOMMON("textcommon", Text.literal("\uEEE4\uEEE1 퀃 \uEEE8\uEEE7\uEEE5\uEEE2 "), DEFAULT_COLOR),
	TEXTRARE("textrare", Text.literal("\uEEE4\uEEE1 퀇 \uEEE8\uEEE7\uEEE5\uEEE2 "), DEFAULT_COLOR),
	TEXTEPIC("textepic", Text.literal("\uEEE4\uEEE1 퀑 \uEEE8\uEEE7\uEEE5\uEEE2 "), DEFAULT_COLOR),
	TEXTLEGENDARY("textlegendary", Text.literal("\uEEE4\uEEE1 퀕 \uEEE8\uEEE7\uEEE5\uEEE2 "), DEFAULT_COLOR),
	TEXTMYTHICAL("textmythical", Text.literal("\uEEE4\uEEE1 퀙 \uEEE8\uEEE7\uEEE5\uEEE2 "), DEFAULT_COLOR),
	TEXTSPECIAL("textspecial", Text.literal("\uEEE4\uEEE1 퀃 \uEEE8\uEEE7\uEEE5\uEEE2 ").withColor(0xC746B4),
			DEFAULT_COLOR),
	TEXTDEFAULT("textdefault", Text.literal("\uEEE4\uEEE1 퀃 \uEEE8\uEEE7\uEEE5\uEEE2 ").withColor(0x5C4B34),
			DEFAULT_COLOR),

	// Ranks
	ANGLER("angler", Text.literal("\uF032"), DEFAULT_COLOR),
	SAILOR("sailor", Text.literal("\uF031"), DEFAULT_COLOR),
	MARINER("mariner", Text.literal("\uF030"), DEFAULT_COLOR),
	CAPTAIN("captain", Text.literal("\uF029"), DEFAULT_COLOR),
	ADMIRAL("admiral", Text.literal("\uF028"), DEFAULT_COLOR),
	STAFF("staff", Text.literal("\uF024"), DEFAULT_COLOR),
	DESIGNER("designer", Text.literal("\uF026"), DEFAULT_COLOR),
	BUILDER("builder", Text.literal("\uF027"), DEFAULT_COLOR),
	MANAGER("manager", Text.literal("\uF023"), DEFAULT_COLOR),
	ADMIN("admin", Text.literal("\uF022"), DEFAULT_COLOR),
	OWNER("owner", Text.literal("\uF021"), DEFAULT_COLOR),
	COMMUNITYMANAGER("communitymanager", Text.literal("\uF088"), DEFAULT_COLOR),
	FOE("foe", Text.literal("\uE00B"), DEFAULT_COLOR),

	// Weather Types
	RAIN("☂", Text.literal("☂"), 0x5555FF),
	SUN("☀", Text.literal("☀"), 0xFFFF55),
	THUNDERSTORM("⚡", Text.literal("⚡"), 0xFFFF55),
	BLOOMINGOASIS("♣", Text.literal("♣"), 0xFC54FC),
	FABLEDWEATHER("⭐", Text.literal("⭐"), 0xF7453E),
	GOLDRUSH("⚠", Text.literal("⚠"), 0xF7EA3E),
	MOON("○", Text.literal("○"), 0x5FC0E6),

	// Climate (changed duplicate of #ClimateConstants.java)
	SUBTROPICAL("subtropical_climate", TextHelper.concat(
			Text.literal("S").withColor(0x4FB07A),
			Text.literal("u").withColor(0x4FB683),
			Text.literal("b").withColor(0x4EBC8D),
			Text.literal("t").withColor(0x4EC296),
			Text.literal("r").withColor(0x4DC8A0),
			Text.literal("o").withColor(0x4DCEA9),
			Text.literal("p").withColor(0x4CD6B2),
			Text.literal("i").withColor(0x4BDEBB),
			Text.literal("c").withColor(0x49E7C4),
			Text.literal("a").withColor(0x48EFCD),
			Text.literal("l").withColor(0x47F7D6)), Defaults.DEFAULT_COLOR),
	SUBARCTIC("subarctic_climate", TextHelper.concat(
			Text.literal("S").withColor(0x53A1C1),
			Text.literal("u").withColor(0x64AAC8),
			Text.literal("b").withColor(0x75B3CF),
			Text.literal("a").withColor(0x86BBD5),
			Text.literal("r").withColor(0x97C4DC),
			Text.literal("c").withColor(0x97C1D8),
			Text.literal("t").withColor(0x98BED3),
			Text.literal("i").withColor(0x98BACF),
			Text.literal("c").withColor(0x98B7CA)), Defaults.DEFAULT_COLOR),
	SEMI_ARID("semi-arid_climate", TextHelper.concat(
			Text.literal("S").withColor(0xE6902E),
			Text.literal("e").withColor(0xE59833),
			Text.literal("m").withColor(0xE5A038),
			Text.literal("i").withColor(0xE4A73C),
			Text.literal("-").withColor(0xE3AF41),
			Text.literal("A").withColor(0xE3B14C),
			Text.literal("r").withColor(0xE4B357),
			Text.literal("i").withColor(0xE4B562),
			Text.literal("d").withColor(0xE4B76D)), Defaults.DEFAULT_COLOR),
	SAVANNA("savanna_climate", TextHelper.concat(
			Text.literal("S").withColor(0xBAC153),
			Text.literal("a").withColor(0xC8CB5A),
			Text.literal("v").withColor(0xD7D661),
			Text.literal("a").withColor(0xE5E068),
			Text.literal("n").withColor(0xE4DF6F),
			Text.literal("n").withColor(0xE3DE77),
			Text.literal("a").withColor(0xE2DD7E)), Defaults.DEFAULT_COLOR),
	CONTINENTAL("continental_climate", TextHelper.concat(
			Text.literal("C").withColor(0xA4A9AB),
			Text.literal("o").withColor(0xABB2B2),
			Text.literal("n").withColor(0xB2BAB9),
			Text.literal("t").withColor(0xB8C3BF),
			Text.literal("i").withColor(0xBFCBC6),
			Text.literal("n").withColor(0xC6D4CD),
			Text.literal("e").withColor(0xCCD9D2),
			Text.literal("n").withColor(0xD2DDD8),
			Text.literal("t").withColor(0xD9E2DD),
			Text.literal("a").withColor(0xDFE6E3),
			Text.literal("l").withColor(0xE5EBE8)), Defaults.DEFAULT_COLOR),
	RAINFOREST("rainforest_climate", TextHelper.concat(
			Text.literal("R").withColor(0x569579),
			Text.literal("a").withColor(0x4C9E7A),
			Text.literal("i").withColor(0x42A87B),
			Text.literal("n").withColor(0x39B17C),
			Text.literal("f").withColor(0x2FBA7D),
			Text.literal("o").withColor(0x2AC27F),
			Text.literal("r").withColor(0x2AC983),
			Text.literal("e").withColor(0x2AD086),
			Text.literal("s").withColor(0x2AD68A),
			Text.literal("t").withColor(0x2ADD8E)), Defaults.DEFAULT_COLOR),
	MEDITERRANEAN("mediterranean_climate", TextHelper.concat(
			Text.literal("M").withColor(0x80C4EF),
			Text.literal("e").withColor(0x85C6EF),
			Text.literal("d").withColor(0x8AC8EF),
			Text.literal("i").withColor(0x8FCAEF),
			Text.literal("t").withColor(0x94CCEE),
			Text.literal("e").withColor(0x99CEEE),
			Text.literal("r").withColor(0x9ED0EE),
			Text.literal("r").withColor(0xA1D1EF),
			Text.literal("a").withColor(0xA4D3F0),
			Text.literal("n").withColor(0xA7D4F1),
			Text.literal("e").withColor(0xAAD5F1),
			Text.literal("a").withColor(0xADD7F2),
			Text.literal("n").withColor(0xB0D8F3)), Defaults.DEFAULT_COLOR),
	OCEANIC("oceanic_climate", TextHelper.concat(
			Text.literal("O").withColor(0x397FAC),
			Text.literal("c").withColor(0x3A85B4),
			Text.literal("e").withColor(0x3C8CBD),
			Text.literal("a").withColor(0x3D92C5),
			Text.literal("n").withColor(0x3995CF),
			Text.literal("i").withColor(0x3599D9),
			Text.literal("c").withColor(0x319CE3)), Defaults.DEFAULT_COLOR),
	MONSOON("monsoon_climate", TextHelper.concat(
			Text.literal("M").withColor(0x6141DF),
			Text.literal("o").withColor(0x654FE0),
			Text.literal("n").withColor(0x6A5CE0),
			Text.literal("s").withColor(0x6E6AE1),
			Text.literal("o").withColor(0x7278E1),
			Text.literal("o").withColor(0x7785E2),
			Text.literal("n").withColor(0x7B93E2)), Defaults.DEFAULT_COLOR),

	DEFAULT("default", Text.empty(), Defaults.DEFAULT_COLOR);

	public final String ID;
	public final Text TAG;
	public final int COLOR;

	Constant(String id, Text tag, int color) {
		this.ID = id;
		this.TAG = tag;
		this.COLOR = color;
	}

	public static Constant valueOfId(String id) {
		for (Constant c : values()) {
			if (c.ID.equals(id.toLowerCase())) {
				return c;
			}
		}
		return DEFAULT;
	}

	public static Constant valueOfTag(String tag) {
		for (Constant c : values()) {
			if (c.TAG.getString().equals(tag)) {
				return c;
			}
		}
		return DEFAULT;
	}

	public static int colorOfId(String id) {
		for (Constant c : values()) {
			if (c.ID.equals(id.toLowerCase())) {
				return c.COLOR;
			}
		}
		return Defaults.DEFAULT_COLOR;
	}

	@Override
	public String toString() {
		return this.ID;
	}

}