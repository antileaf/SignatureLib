package me.antileaf.signature.patches.unlock;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomUnlock;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.neow.NeowUnlockScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.signature.utils.EasyUnlock;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.WeakHashMap;

@SuppressWarnings("unused")
public class EasyUnlockPatch {
	private static final Logger logger = LogManager.getLogger(EasyUnlockPatch.class.getName());

	private static WeakHashMap<ArrayList<?>, EasyUnlock> patched = new WeakHashMap<>();

	@SpirePatch(clz = GameOverScreen.class, method = "calculateUnlockProgress", paramtypez = {})
	public static class CalculateUnlockProgressPatch {
		@SpirePostfixPatch
		public static void Postfix(GameOverScreen _inst) {
			EasyUnlock easyUnlock = SignatureHelperInternal.publishOnGameOver(_inst);

			if (ReflectionHacks.getPrivate(_inst, GameOverScreen.class, "unlockBundle") == null &&
					easyUnlock != null && easyUnlock.cards != null && !easyUnlock.cards.isEmpty()) {
				ArrayList<AbstractUnlock> unlocks = new ArrayList<>();

				for (AbstractCard card : easyUnlock.cards) {
					AbstractUnlock item = new CustomUnlock(card.cardID);
					item.card = card;

					unlocks.add(item);
				}

				ReflectionHacks.setPrivate(_inst, GameOverScreen.class, "unlockBundle",
						unlocks);

				patched.put(unlocks, easyUnlock);
			}
		}
	}

	private static UIStrings uiStrings = null;

	private static UIStrings getUIStrings() {
		if (uiStrings == null)
			uiStrings = CardCrawlGame.languagePack.getUIString("SignatureLib:GameOverScreen");
		return uiStrings;
	}

	public static boolean handle(NeowUnlockScreen screen) {
		if (patched.containsKey(screen.unlockBundle)) {
			String title = patched.get(screen.unlockBundle).title;
			if (title == null || title.isEmpty())
				title = getUIStrings().TEXT[0];

			AbstractDungeon.dynamicBanner.appear(title);

			return true;
		}

		return false;
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "open")
	public static class UnlockScreenOpenPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("appearInstantly"))
						m.replace(" { if (" + EasyUnlockPatch.class.getName() +
								".handle(this)) { } else { $_ = $proceed($$); } }");
				}
			};
		}
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "reOpen")
	public static class UnlockScreenReOpenPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return UnlockScreenOpenPatch.Instrument();
		}
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class UnlockScreenRenderPatch {
		@SpireInsertPatch(rloc = 36)
		public static SpireReturn<Void> Insert(NeowUnlockScreen _inst, SpriteBatch sb) {
			if (patched.containsKey(_inst.unlockBundle)) {
				String tip = patched.get(_inst.unlockBundle).tip;
				if (tip == null || tip.isEmpty())
					tip = getUIStrings().TEXT[1];

				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont,
						tip,
						(float) Settings.WIDTH / 2.0F,
						(float)Settings.HEIGHT / 2.0F - 330.0F * Settings.scale,
						Settings.CREAM_COLOR);

				_inst.button.render(sb);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}
}
