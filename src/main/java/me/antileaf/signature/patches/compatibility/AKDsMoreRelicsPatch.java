package me.antileaf.signature.patches.compatibility;

import AKDsMoreRelics.relics.StoneOfResonance;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.WeakHashMap;

@SuppressWarnings("unused")
public class AKDsMoreRelicsPatch {
	private static final WeakHashMap<AbstractCard, Boolean> sorMap = new WeakHashMap<>();
	
	@SpirePatch(clz = StoneOfResonance.class, method = "onUseCard",
			paramtypez = {AbstractCard.class, UseCardAction.class}, requiredModId = "AKDsMoreRelics")
	public static class StoneOfResonanceUpdatePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.NewExprMatcher(NewQueueCardAction.class));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class, localvars = {"c2"})
		public static void Insert(StoneOfResonance _inst, AbstractCard card, UseCardAction action, AbstractCard c2) {
			c2.cardID = card.cardID;
			sorMap.put(c2, true);
		}
	}
	
	@SpirePatch(clz = StoneOfResonance.UseCardActionBuryPatch.class, method = "Insert",
			paramtypez = {UseCardAction.class}, requiredModId = "AKDsMoreRelics")
	public static class StoneOfResonancePatchPatch {
		@SpirePrefixPatch
		public static SpireReturn<SpireReturn<Void>> Prefix(UseCardAction action) {
			AbstractCard card = ReflectionHacks.getPrivate(action, UseCardAction.class, "targetCard");
			return SpireReturn.Return(sorMap.containsKey(card) ? SpireReturn.Return() : SpireReturn.Continue());
		}
	}
}
