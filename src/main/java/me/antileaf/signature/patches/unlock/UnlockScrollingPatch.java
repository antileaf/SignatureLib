package me.antileaf.signature.patches.unlock;

import basemod.patches.com.megacrit.cardcrawl.screens.CombatRewardScreen.RewardsScrolling;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.neow.NeowUnlockScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class UnlockScrollingPatch {
//	private static class Listener implements ScrollBarListener {
//		public ScrollBar parent = null;
//
//		public float scrollLowerBound;
//		public float scrollUpperBound;
//		public float scrollPosition;
//		public float scrollTarget;
//		public boolean grabbedScreen;
//		public float grabStartY;
//
//		public Listener() {
//			this.scrollLowerBound = 0.0F;
//			this.scrollUpperBound = 0.0F;
//			this.scrollPosition = 0.0F;
//			this.scrollTarget = 0.0F;
//			this.grabbedScreen = false;
//			this.grabStartY = 0.0F;
//		}
//
//		public void updatePosition() {
//			float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound,
//					this.scrollUpperBound, this.scrollPosition);
//			this.parent.parentScrolledToPercent(percent);
//		}
//
//		@Override
//		public void scrolledUsingBar(float v) {
//			this.scrollPosition = MathUtils.lerp(this.scrollLowerBound,
//					this.scrollUpperBound, v);
//			this.scrollTarget = this.scrollPosition;
//			// TODO: position
//			this.updatePosition();
//		}
//	}

//	public static class Fields {
//		public static SpireField<ScrollBar> bar = new SpireField<>(() -> null);
//	}

//	@SpirePatch(clz = NeowUnlockScreen.class, method = "open",
//			paramtypez = {ArrayList.class, boolean.class})
//	public static class OpenPatch {
//		@SpirePostfixPatch
//		public static void Postfix(NeowUnlockScreen _inst, ArrayList<AbstractUnlock> unlock, boolean isVictory) {
//			if (unlock.get(0).type == AbstractUnlock.UnlockType.CARD && unlock.size() > 3) {
//				ScrollBar bar = new ScrollBar(new Listener(),
//						(float) Settings.WIDTH / 2.0F + 270.0F * Settings.scale,
//						(float)Settings.HEIGHT / 2.0F - 86.0F * Settings.scale,
//						500.0F * Settings.scale);
//
//				Listener listener = (Listener) bar.sliderListener;
//				listener.parent = bar;
//
//				listener.scrollUpperBound = Settings.WIDTH * 0.25F * unlock.size() + 1;
//			}
//			else
//				Fields.bar.set(_inst, null);
//		}
//	}

//	@SpirePatch(clz = NeowUnlockScreen.class, method = "update", paramtypez = {})
//	public static class UpdatePatch {
//		@SpirePostfixPatch
//		public static void Postfix(NeowUnlockScreen _inst) {
//			if (Fields.bar.get(_inst) != null) {
//				ScrollBar bar = Fields.bar.get(_inst);
//
//				if (!bar.update()) {
//					Listener listener = (Listener) bar.sliderListener;
//
//					int y = InputHelper.mY;
//					if (!listener.grabbedScreen) {
//						if (InputHelper.scrolledDown)
//							listener.scrollTarget += Settings.SCROLL_SPEED;
//						else if (InputHelper.scrolledUp)
//							listener.scrollTarget -= Settings.SCROLL_SPEED;
//
//						if (InputHelper.justClickedLeft) {
//							listener.grabbedScreen = true;
//							listener.grabStartY = y - listener.scrollTarget;
//						}
//					}
//					else if (InputHelper.isMouseDown)
//						listener.scrollTarget = y - listener.grabStartY;
//					else
//						listener.grabbedScreen = false;
//
//					float prev_scrollPosition = listener.scrollTarget;
//					listener.scrollPosition = MathHelper.scrollSnapLerpSpeed(
//							listener.scrollPosition, listener.scrollTarget);
//
//					if (listener.scrollTarget < 0.0F)
//						listener.scrollTarget = 0.0F;
//					if (listener.scrollTarget > listener.scrollUpperBound)
//						listener.scrollTarget = listener.scrollUpperBound;
//
////					if (listener.scrollPosition != prev_scrollPosition)
//
//					listener.updatePosition();
//				}
//			}
//		}
//	}
}
