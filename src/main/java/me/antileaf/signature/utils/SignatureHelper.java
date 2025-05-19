package me.antileaf.signature.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.signature.interfaces.EasyUnlockSubscriber;
import me.antileaf.signature.interfaces.SignatureSubscriber;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class SignatureHelper {
	public static final Style DEFAULT_STYLE;

	public static void register(String id, Info info) {
		SignatureHelperInternal.register(id, info);
	}

	public static void unlock(String id, boolean unlock) {
		SignatureHelperInternal.unlock(id, unlock);
	}

	public static void enable(String id, boolean enable) {
		SignatureHelperInternal.enable(id, enable);
	}

	public static boolean shouldUseSignature(AbstractCard card) {
		return SignatureHelperInternal.shouldUseSignature(card);
	}

	public static void noDebugging(String id) {
		SignatureHelperInternal.noDebugging(id);
	}

	public static void noDebuggingPrefix(String prefix) {
		SignatureHelperInternal.noDebuggingPrefix(prefix);
	}

	public static void addUnlockConditions(String jsonFile) {
		SignatureHelperInternal.addUnlockConditions(jsonFile);
	}

	public static void addIllustrators(String jsonFile) {
		SignatureHelperInternal.addIllustrators(jsonFile);
	}

	public static boolean isUnlocked(String id) {
		return SignatureHelperInternal.isUnlocked(id);
	}

	public static boolean isEnabled(String id) {
		return SignatureHelperInternal.isEnabled(id);
	}

	public static void subscribe(SignatureSubscriber subscriber) {
		SignatureHelperInternal.subscribe(subscriber);
	}

	public static void unsubscribe(SignatureSubscriber subscriber) {
		SignatureHelperInternal.unsubscribe(subscriber);
	}

	public static void registerEasyUnlock(EasyUnlockSubscriber subscriber) {
		SignatureHelperInternal.registerEasyUnlock(subscriber);
	}

	public static class Info {
//		public final String img, portrait;
		public final Function<AbstractCard, String> img, portrait;
		public final Function<AbstractCard, String> energyOrb, energyOrbP;
		public final Predicate<AbstractCard> hideTitle;
		public final Predicate<AbstractCard> hideFrame;
		public final Predicate<AbstractCard> shouldUseSignature;
		public final String parentID;
		public final boolean hideSCVPanel, dontAvoidSCVPanel;
		public final Style style;

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Function<AbstractCard, String> energyOrb, Function<AbstractCard, String> energyOrbP,
					Predicate<AbstractCard> hideTitle, Predicate<AbstractCard> hideFrame,
					Predicate<AbstractCard> shouldUseSignature,
					String parentID, boolean hideSCVPanel, boolean dontAvoidSCVPanel, Style style) {
			this.img = img;
			this.portrait = portrait;
			this.energyOrb = energyOrb;
			this.energyOrbP = energyOrbP;
			this.hideTitle = hideTitle;
			this.hideFrame = hideFrame;
			this.shouldUseSignature = shouldUseSignature;
			this.parentID = parentID;
			this.hideSCVPanel = hideSCVPanel;
			this.dontAvoidSCVPanel = dontAvoidSCVPanel;
			this.style = style;
		}

		@Deprecated
		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature,
					boolean hideSCVPanel, boolean dontAvoidSCVPanel) {
			this(img, portrait, c -> null, c -> null,
					c -> false, c -> false, shouldUseSignature,
					null, hideSCVPanel, dontAvoidSCVPanel, DEFAULT_STYLE);
		}

		@Deprecated
		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature, Style style) {
			this(img, portrait, c -> null, c -> null,
					c -> false, c -> false, shouldUseSignature,
					null, false, false, style);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature) {
			this(img, portrait, c -> null, c -> null,
					c -> false, c -> false, shouldUseSignature,
					null, false, false, DEFAULT_STYLE);
		}

		@Deprecated
		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait, Style style) {
			this(img, portrait, c -> null, c -> null,
					c -> false, c -> false, c -> true,
					null, false, false, style);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait) {
			this(img, portrait, c -> null, c -> null,
					c -> false, c -> false, c -> true,
					null, false, false, DEFAULT_STYLE);
		}

		@Deprecated
		public Info(String img, String portrait, Predicate<AbstractCard> shouldUseSignature, Style style) {
			this(c -> img, c -> portrait,
					c -> null, c -> null,
					c -> false, c -> false, shouldUseSignature,
					null, false, false, style);
		}

		public Info(String img, String portrait, Predicate<AbstractCard> shouldUseSignature) {
			this(c -> img, c -> portrait,
					c -> null, c -> null,
					c -> false, c -> false, shouldUseSignature,
					null, false, false, DEFAULT_STYLE);
		}

		public Info(String img, String portrait) {
			this(c -> img, c -> portrait, c -> true);
		}

		@Deprecated
		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature, String parentID, Style style) {
			this(img, portrait, c -> null, c -> null,
					c -> false, c -> false, shouldUseSignature,
					parentID, false, false, style);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature, String parentID) {
			this(img, portrait, c -> null, c -> null,
					c -> false, c -> false, shouldUseSignature,
					parentID, false, false, DEFAULT_STYLE);
		}

		@Deprecated
		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					String parentID, Style style) {
			this(img, portrait, c -> true, parentID, style);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait, String parentID) {
			this(img, portrait, c -> true, parentID);
		}

		public Info(String img, String portrait, Predicate<AbstractCard> shouldUseSignature, String parentID) {
			this(c -> img, c -> portrait, shouldUseSignature, parentID);
		}

		@Deprecated
		public Info(String img, String portrait, String parentID, Style style) {
			this(c -> img, c -> portrait, parentID, style);
		}

		public Info(String img, String portrait, String parentID) {
			this(c -> img, c -> portrait, parentID);
		}
	}

	public static class InfoBuilder {
		private static final Logger logger = LogManager.getLogger(InfoBuilder.class.getName());

		public Function<AbstractCard, String> img, portrait;
		public Function<AbstractCard, String> energyOrb, energyOrbP;
		public Predicate<AbstractCard> hideTitle;
		public Predicate<AbstractCard> hideFrame;
		public Predicate<AbstractCard> shouldUseSignature;
		public String parentID;
		public boolean hideSCVPanel, dontAvoidSCVPanel;
		public Style style;

		public InfoBuilder() {
			this.img = null;
			this.portrait = null;
			this.energyOrb = null;
			this.energyOrbP = null;
			this.hideTitle = c -> false;
			this.hideFrame = c -> false;
			this.shouldUseSignature = c -> true;
			this.parentID = null;
			this.hideSCVPanel = false;
			this.dontAvoidSCVPanel = false;
			this.style = DEFAULT_STYLE;
		}

		public InfoBuilder img(Function<AbstractCard, String> img) {
			this.img = img;
			return this;
		}

		public InfoBuilder img(String img) {
			this.img = c -> img;
			return this;
		}

		public InfoBuilder portrait(Function<AbstractCard, String> portrait) {
			this.portrait = portrait;
			return this;
		}

		public InfoBuilder portrait(String portrait) {
			this.portrait = c -> portrait;
			return this;
		}

		public InfoBuilder energyOrb(Function<AbstractCard, String> energyOrb) {
			this.energyOrb = energyOrb;
			return this;
		}

		public InfoBuilder energyOrb(String energyOrb) {
			this.energyOrb = c -> energyOrb;
			return this;
		}

		public InfoBuilder energyOrbP(Function<AbstractCard, String> energyOrbP) {
			this.energyOrbP = energyOrbP;
			return this;
		}

		public InfoBuilder energyOrbP(String energyOrbP) {
			this.energyOrbP = c -> energyOrbP;
			return this;
		}

		public InfoBuilder hideTitle(Predicate<AbstractCard> hideTitle) {
			this.hideTitle = hideTitle;
			return this;
		}

		public InfoBuilder hideTitle(boolean hideTitle) {
			this.hideTitle = c -> hideTitle;
			return this;
		}

		public InfoBuilder hideFrame(Predicate<AbstractCard> hideFrame) {
			this.hideFrame = hideFrame;
			return this;
		}

		public InfoBuilder hideFrame(boolean hideFrame) {
			this.hideFrame = c -> hideFrame;
			return this;
		}

		public InfoBuilder shouldUseSignature(Predicate<AbstractCard> shouldUseSignature) {
			this.shouldUseSignature = shouldUseSignature;
			return this;
		}

		public InfoBuilder parentID(String parentID) {
			this.parentID = parentID;
			return this;
		}

		public InfoBuilder hideSCVPanel(boolean hideSCVPanel) {
			this.hideSCVPanel = hideSCVPanel;
			return this;
		}

		public InfoBuilder dontAvoidSCVPanel(boolean dontAvoidSCVPanel) {
			this.dontAvoidSCVPanel = dontAvoidSCVPanel;
			return this;
		}

		public InfoBuilder style(Style style) {
			this.style = style;
			return this;
		}

		public Info build() {
			if (this.portrait == null)
				this.portrait = c -> this.img.apply(c).replace(".png", "_p.png");

			if (this.energyOrb == null) {
				this.energyOrb = c -> null;
				this.energyOrbP = c -> null;
			}
			else if (this.energyOrbP == null) {
				this.energyOrbP = c -> {
					String energyOrb = this.energyOrb.apply(c);

					if (energyOrb == null)
						return null;

					return energyOrb.replace("512", "1024");
				};
			}

			if (this.parentID != null && this.hideSCVPanel)
				logger.warn("parentID != null. hideSCVPanel will be ignored.");

			return new Info(this.img,
					this.portrait,
					this.energyOrb,
					this.energyOrbP,
					this.hideTitle,
					this.hideFrame,
					this.shouldUseSignature,
					this.parentID,
					this.hideSCVPanel,
					this.dontAvoidSCVPanel,
					this.style);
		}
	}

	public static class Style {
		public String cardTypeAttackCommon, cardTypeAttackUncommon, cardTypeAttackRare;
		public String cardTypeSkillCommon, cardTypeSkillUncommon, cardTypeSkillRare;
		public String cardTypePowerCommon, cardTypePowerUncommon, cardTypePowerRare;
		public String cardTypeAttackCommonP, cardTypeAttackUncommonP, cardTypeAttackRareP;
		public String cardTypeSkillCommonP, cardTypeSkillUncommonP, cardTypeSkillRareP;
		public String cardTypePowerCommonP, cardTypePowerUncommonP, cardTypePowerRareP;
		public String descShadow, descShadowP;
		public String descShadowSmall, descShadowSmallP;

		public Style() {}

		public Style(String cardTypeAttackCommon, String cardTypeAttackUncommon, String cardTypeAttackRare,
					 String cardTypeSkillCommon, String cardTypeSkillUncommon, String cardTypeSkillRare,
					 String cardTypePowerCommon, String cardTypePowerUncommon, String cardTypePowerRare,
					 String cardTypeAttackCommonP, String cardTypeAttackUncommonP, String cardTypeAttackRareP,
					 String cardTypeSkillCommonP, String cardTypeSkillUncommonP, String cardTypeSkillRareP,
					 String cardTypePowerCommonP, String cardTypePowerUncommonP, String cardTypePowerRareP,
					 String descShadow, String descShadowP,
					 String descShadowSmall, String descShadowSmallP) {
			this.cardTypeAttackCommon = cardTypeAttackCommon;
			this.cardTypeAttackUncommon = cardTypeAttackUncommon;
			this.cardTypeAttackRare = cardTypeAttackRare;
			this.cardTypeSkillCommon = cardTypeSkillCommon;
			this.cardTypeSkillUncommon = cardTypeSkillUncommon;
			this.cardTypeSkillRare = cardTypeSkillRare;
			this.cardTypePowerCommon = cardTypePowerCommon;
			this.cardTypePowerUncommon = cardTypePowerUncommon;
			this.cardTypePowerRare = cardTypePowerRare;
			this.cardTypeAttackCommonP = cardTypeAttackCommonP;
			this.cardTypeAttackUncommonP = cardTypeAttackUncommonP;
			this.cardTypeAttackRareP = cardTypeAttackRareP;
			this.cardTypeSkillCommonP = cardTypeSkillCommonP;
			this.cardTypeSkillUncommonP = cardTypeSkillUncommonP;
			this.cardTypeSkillRareP = cardTypeSkillRareP;
			this.cardTypePowerCommonP = cardTypePowerCommonP;
			this.cardTypePowerUncommonP = cardTypePowerUncommonP;
			this.cardTypePowerRareP = cardTypePowerRareP;
			this.descShadow = descShadow;
			this.descShadowP = descShadowP;
			this.descShadowSmall = descShadowSmall;
			this.descShadowSmallP = descShadowSmallP;
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
}
