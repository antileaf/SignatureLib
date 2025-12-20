package me.antileaf.signature.patches.unlock;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomUnlock;
import basemod.patches.whatmod.WhatMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.neow.NeowUnlockScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.ui.buttons.UnlockConfirmButton;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import javassist.CannotCompileException;
import javassist.CtBehavior;
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
	
	private static ArrayList<EasyUnlock> unlockItems = new ArrayList<>();
	private static int nextIndex = 0;
	
	private static ArrayList<AbstractUnlock> convert(EasyUnlock item) {
		ArrayList<AbstractUnlock> unlocks = new ArrayList<>();
		
		if (item.cards != null && !item.cards.isEmpty()) {
			for (AbstractCard card : item.cards) {
				AbstractUnlock unlock = new CustomUnlock(card.cardID);
				unlock.card = card;
				
				unlocks.add(unlock);
			}
		}
		
		return unlocks;
	}
	
	private static ArrayList<AbstractUnlock> getNextUnlocks() {
		while (nextIndex < unlockItems.size()) {
			EasyUnlock item = unlockItems.get(nextIndex++);
			
			ArrayList<AbstractUnlock> unlocks = convert(item);
			if (!unlocks.isEmpty())
				return unlocks;
		}
		
		return new ArrayList<>();
	}

	@SpirePatch(clz = GameOverScreen.class, method = "calculateUnlockProgress", paramtypez = {})
	public static class CalculateUnlockProgressPatch {
		@SpirePostfixPatch
		public static void Postfix(GameOverScreen _inst) {
			unlockItems = SignatureHelperInternal.publishOnGameOver(_inst);
			nextIndex = 0;

			if (ReflectionHacks.getPrivate(_inst, GameOverScreen.class, "unlockBundle") == null) {
				ArrayList<AbstractUnlock> unlocks = getNextUnlocks();

				if (!unlocks.isEmpty()) {
					ReflectionHacks.setPrivate(_inst, GameOverScreen.class, "unlockBundle",
							unlocks);
					
					patched.put(unlocks, unlockItems.get(nextIndex - 1));
				}
			}
		}
	}

	private static UIStrings uiStrings =
			CardCrawlGame.languagePack.getUIString("SignatureLib:GameOverScreen");

	public static boolean handle(NeowUnlockScreen screen) {
		if (patched.containsKey(screen.unlockBundle)) {
			String title = patched.get(screen.unlockBundle).title;
			if (title == null || title.isEmpty())
				title = uiStrings.TEXT[0];

			AbstractDungeon.dynamicBanner.appear(title);

			return true;
		}

		return false;
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "open", paramtypez = {ArrayList.class, boolean.class})
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
		
		@SpirePostfixPatch
		public static void Postfix(NeowUnlockScreen _inst, ArrayList<AbstractUnlock> unlocks, boolean isVictory) {
			UnlockScreenReOpenPatch.Postfix(_inst);
		}
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "reOpen")
	public static class UnlockScreenReOpenPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return UnlockScreenOpenPatch.Instrument();
		}
		
		@SpirePostfixPatch
		public static void Postfix(NeowUnlockScreen _inst) {
			if (patched.containsKey(_inst.unlockBundle)) {
				if (_inst.unlockBundle.size() == 4 &&
						_inst.unlockBundle.get(0).type == AbstractUnlock.UnlockType.CARD) {
					for (int i = 0; i < _inst.unlockBundle.size(); i++) {
						AbstractCard c = _inst.unlockBundle.get(i).card;
						c.current_x = Settings.WIDTH * 0.2F * (i + 1);
						c.target_x = Settings.WIDTH * 0.2F * (i + 1);
					}
				}
			}
		}
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class UnlockScreenRenderPatch {
		@SpireInsertPatch(rloc = 36)
		public static SpireReturn<Void> Insert(NeowUnlockScreen _inst, SpriteBatch sb) {
			if (patched.containsKey(_inst.unlockBundle)) {
				String tip = patched.get(_inst.unlockBundle).tip;
				if (tip == null || tip.isEmpty())
					tip = uiStrings.TEXT[1];

				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont,
						tip,
						(float) Settings.WIDTH / 2.0F,
						(float)Settings.HEIGHT / 2.0F - 330.0F * Settings.scale,
						Settings.CREAM_COLOR);

				_inst.button.render(sb);
				
				if (WhatMod.enabled) {
					Class<?>[] classes = _inst.unlockBundle.stream()
							.filter(u -> u.card != null)
							.map(u -> u.card)
							.map(Object::getClass)
							.toArray(Class<?>[]::new);
					
					ReflectionHacks.privateMethod(WhatMod.class, "renderModTooltip",
							SpriteBatch.class, float.class, float.class, Class[].class)
							.invoke(null, sb, 1500.0F * Settings.scale, 900.0F * Settings.scale, classes);
				}

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(clz = UnlockConfirmButton.class, method = "update", paramtypez = {})
	public static class UnlockConfirmButtonUpdatePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(UnlockConfirmButton.class, "hide"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(UnlockConfirmButton _inst) {
			patched.clear();
			ArrayList<AbstractUnlock> unlocks = getNextUnlocks();
			
			if (!unlocks.isEmpty()) {
				CardCrawlGame.sound.stop("UNLOCK_SCREEN", AbstractDungeon.gUnlockScreen.id);
				patched.put(unlocks, unlockItems.get(nextIndex - 1));
				AbstractDungeon.gUnlockScreen.open(unlocks, false); // isVictory is not used
				
				Color textColor = ReflectionHacks.getPrivate(_inst, UnlockConfirmButton.class, "textColor");
				float target_a = ReflectionHacks.getPrivate(_inst, UnlockConfirmButton.class, "target_a");
				Color btnColor = ReflectionHacks.getPrivate(_inst, UnlockConfirmButton.class, "btnColor");
				
				textColor.a = MathHelper.fadeLerpSnap(textColor.a, target_a);
				btnColor.a = textColor.a;
				
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
}
