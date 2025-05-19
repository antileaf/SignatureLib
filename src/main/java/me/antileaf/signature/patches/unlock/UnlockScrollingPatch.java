package me.antileaf.signature.patches.unlock;

import basemod.patches.com.megacrit.cardcrawl.screens.CombatRewardScreen.RewardsScrolling;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;

@SuppressWarnings("unused")
public class UnlockScrollingPatch {
	private static class Listener implements ScrollBarListener {
		public ScrollBar parent = null;

		public float scrollLowerBound;
		public float scrollUpperBound;
		public float scrollPosition;
		public float scrollTarget;
		public boolean grabbedScreen;
		public float grabStartY;

		public void updatePosition() {
			float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound,
					this.scrollUpperBound, this.scrollPosition);
			this.parent.parentScrolledToPercent(percent);
		}

		@Override
		public void scrolledUsingBar(float v) {
			this.scrollPosition = MathUtils.lerp(this.scrollLowerBound,
					this.scrollUpperBound, v);
			this.scrollTarget = this.scrollPosition;
			// TODO: position
			this.updatePosition();
		}
	}

	// TODO: WIP
//	public static class Fields {
//		public static SpireField<ScrollBar> bar = new SpireField<>(() -> null);
//	}
}
