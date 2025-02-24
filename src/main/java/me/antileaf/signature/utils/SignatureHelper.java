package me.antileaf.signature.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import me.antileaf.signature.card.AbstractSignatureCard;
import me.antileaf.signature.patches.SignaturePatch;
import me.antileaf.signature.patches.card.UnlockConditionPatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class SignatureHelper {
	private static final Logger logger = LogManager.getLogger(SignatureHelper.class);

	public static final float FADE_DURATION = 0.3F;
	public static final float FORCED_FADE_DURATION = 0.5F;

	public static final Style DEFAULT_STYLE;

	private static final Map<String, Info> registered = new HashMap<>();

	private static final Map<String, Boolean> unlocked = new HashMap<>();
	private static final Map<String, Boolean> enabled = new HashMap<>();
	private static final Map<String, Texture> cache = new HashMap<>();

	private static final Set<String> noDebugging = new HashSet<>();
	private static final Set<String> noDebuggingPrefixes = new HashSet<>();

	public static TextureAtlas.AtlasRegion load(String path) {
		Texture t;

		if (cache.containsKey(path))
			t = cache.get(path);
		else {
			if (Gdx.files.internal(path).exists())
				t = ImageMaster.loadImage(path);
			else
				t = null;

			cache.put(path, t);
		}

		return t == null ? null :
				new TextureAtlas.AtlasRegion(t, 0, 0, t.getWidth(), t.getHeight());
	}

	public static void register(String id, Info info) {
		AbstractCard card = CardLibrary.getCard(id);

		if (card == null) {
			logger.error("Card with ID {} not found", id);
			return;
		}

		if (card instanceof AbstractSignatureCard) {
			logger.warn("Please do not register a card that already extends AbstractSignatureCard");
			return;
		}

		if (!Gdx.files.internal(info.img).exists()) {
			logger.error("Image file {} not found", info.img);
			return;
		}

		registered.put(id, info);
	}

	public static boolean isRegistered(String id) {
		return registered.containsKey(id);
	}

	public static Info getInfo(String id) {
		return registered.get(id);
	}

	public static Style getStyle(AbstractCard card) {
		return card instanceof AbstractSignatureCard ?
				((AbstractSignatureCard) card).style :
				getInfo(card.cardID).style;
	}

	public static boolean hasSignature(AbstractCard card) {
		if (card instanceof AbstractSignatureCard)
			return ((AbstractSignatureCard) card).hasSignature;
		else
			return isRegistered(card.cardID);
	}

	public static boolean isUnlocked(String id) {
		if (ConfigHelper.enableDebugging() && !noDebugging.contains(id) &&
				noDebuggingPrefixes.stream().noneMatch(id::startsWith))
			return true;

		if (!unlocked.containsKey(id))
			unlocked.put(id, ConfigHelper.isSignatureUnlocked(id));

		return unlocked.get(id);
	}

	public static void setDebugging(String id, boolean available) {
		if (available)
			noDebugging.add(id);
		else
			noDebugging.remove(id);
	}

	public static void noDebugging(String id) {
		noDebugging.add(id);
	}

	public static void noDebuggingPrefix(String prefix) {
		noDebuggingPrefixes.add(prefix);
	}

	public static void unlock(String id, boolean unlock) {
		ConfigHelper.setSignatureUnlocked(id, unlock);
		unlocked.put(id, unlock);
	}

	public static String getUnlockCondition(String id) {
		return UnlockConditionPatch.Fields.unlockCondition.get(CardCrawlGame.languagePack.getCardStrings(id));
	}

	public static void addUnlockConditions(String jsonFile) {
		Map<String, String> conditions = (new Gson()).fromJson(
				Gdx.files.internal(jsonFile).readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, String>>() {}).getType());

		for (Map.Entry<String, String> entry : conditions.entrySet())
			UnlockConditionPatch.Fields.unlockCondition.set(
					CardCrawlGame.languagePack.getCardStrings(entry.getKey()),
					entry.getValue());
	}

	public static boolean isEnabled(String id) {
		if (!enabled.containsKey(id))
			enabled.put(id, ConfigHelper.isSignatureEnabled(id));

		if (!isUnlocked(id) && !ConfigHelper.enableDebugging())
			enable(id, false);

		return enabled.get(id);
	}

	public static void enable(String id, boolean enable) {
		ConfigHelper.setSignatureEnabled(id, enable);
		enabled.put(id, enable);
	}

	public static boolean shouldUseSignature(AbstractCard card) {
		return hasSignature(card) && isUnlocked(card.cardID) && isEnabled(card.cardID);
	}

	public static class Info {
		public final String img, portrait;
		public final boolean dontAvoidSCVPanel;
		public final Style style;

		public Info(String img, String portrait, boolean dontAvoidSCVPanel, Style style) {
			this.img = img;
			this.portrait = portrait;
			this.dontAvoidSCVPanel = dontAvoidSCVPanel;
			this.style = style;
		}

		public Info(String img, String portrait, boolean dontAvoidSCVPanel) {
			this(img, portrait, dontAvoidSCVPanel, DEFAULT_STYLE);
		}

		public Info(String img, String portrait, Style style) {
			this(img, portrait, false, style);
		}

		public Info(String img, String portrait) {
			this(img, portrait, false);
		}
	}

	public static class Style {
		public TextureAtlas.AtlasRegion cardTypeAttackCommon, cardTypeAttackUncommon, cardTypeAttackRare;
		public TextureAtlas.AtlasRegion cardTypeSkillCommon, cardTypeSkillUncommon, cardTypeSkillRare;
		public TextureAtlas.AtlasRegion cardTypePowerCommon, cardTypePowerUncommon, cardTypePowerRare;
		public TextureAtlas.AtlasRegion cardTypeAttackCommonP, cardTypeAttackUncommonP, cardTypeAttackRareP;
		public TextureAtlas.AtlasRegion cardTypeSkillCommonP, cardTypeSkillUncommonP, cardTypeSkillRareP;
		public TextureAtlas.AtlasRegion cardTypePowerCommonP, cardTypePowerUncommonP, cardTypePowerRareP;
		public TextureAtlas.AtlasRegion descShadow, descShadowP;
		public TextureAtlas.AtlasRegion descShadowSmall, descShadowSmallP;

		public Style() {}

		public Style(String cardTypeAttackCommon, String cardTypeAttackUncommon, String cardTypeAttackRare,
					 String cardTypeSkillCommon, String cardTypeSkillUncommon, String cardTypeSkillRare,
					 String cardTypePowerCommon, String cardTypePowerUncommon, String cardTypePowerRare,
					 String cardTypeAttackCommonP, String cardTypeAttackUncommonP, String cardTypeAttackRareP,
					 String cardTypeSkillCommonP, String cardTypeSkillUncommonP, String cardTypeSkillRareP,
					 String cardTypePowerCommonP, String cardTypePowerUncommonP, String cardTypePowerRareP,
					 String descShadow, String descShadowP,
					 String descShadowSmall, String descShadowSmallP) {
			this.cardTypeAttackCommon = load(cardTypeAttackCommon);
			this.cardTypeAttackUncommon = load(cardTypeAttackUncommon);
			this.cardTypeAttackRare = load(cardTypeAttackRare);
			this.cardTypeSkillCommon = load(cardTypeSkillCommon);
			this.cardTypeSkillUncommon = load(cardTypeSkillUncommon);
			this.cardTypeSkillRare = load(cardTypeSkillRare);
			this.cardTypePowerCommon = load(cardTypePowerCommon);
			this.cardTypePowerUncommon = load(cardTypePowerUncommon);
			this.cardTypePowerRare = load(cardTypePowerRare);
			this.cardTypeAttackCommonP = load(cardTypeAttackCommonP);
			this.cardTypeAttackUncommonP = load(cardTypeAttackUncommonP);
			this.cardTypeAttackRareP = load(cardTypeAttackRareP);
			this.cardTypeSkillCommonP = load(cardTypeSkillCommonP);
			this.cardTypeSkillUncommonP = load(cardTypeSkillUncommonP);
			this.cardTypeSkillRareP = load(cardTypeSkillRareP);
			this.cardTypePowerCommonP = load(cardTypePowerCommonP);
			this.cardTypePowerUncommonP = load(cardTypePowerUncommonP);
			this.cardTypePowerRareP = load(cardTypePowerRareP);
			this.descShadow = load(descShadow);
			this.descShadowP = load(descShadowP);
			this.descShadowSmall = load(descShadowSmall);
			this.descShadowSmallP = load(descShadowSmallP);
		}
	}

	static {
		 DEFAULT_STYLE = new Style(
				"SignatureLib/512/attack_common.png",
				"SignatureLib/512/attack_uncommon.png",
				"SignatureLib/512/attack_rare.png",
				"SignatureLib/512/skill_common.png",
				"SignatureLib/512/skill_uncommon.png",
				"SignatureLib/512/skill_rare.png",
				"SignatureLib/512/power_common.png",
				"SignatureLib/512/power_uncommon.png",
				"SignatureLib/512/power_rare.png",
				"SignatureLib/1024/attack_common.png",
				"SignatureLib/1024/attack_uncommon.png",
				"SignatureLib/1024/attack_rare.png",
				"SignatureLib/1024/skill_common.png",
				"SignatureLib/1024/skill_uncommon.png",
				"SignatureLib/1024/skill_rare.png",
				"SignatureLib/1024/power_common.png",
				"SignatureLib/1024/power_uncommon.png",
				"SignatureLib/1024/power_rare.png",
				"SignatureLib/512/desc_shadow.png",
				"SignatureLib/1024/desc_shadow.png",
				"SignatureLib/512/desc_shadow_small.png",
				"SignatureLib/1024/desc_shadow_small.png"
		);
	}

	public static boolean usePatch(AbstractCard card) {
		return SignatureHelper.isRegistered(card.cardID) &&
				SignatureHelper.shouldUseSignature(card);
	}

	public static void forceToShowDescription(AbstractCard card) {
		if (card instanceof AbstractSignatureCard)
			((AbstractSignatureCard) card).forceToShowDescription();
		else
			SignaturePatch.forceToShowDescription(card);
	}
}
