package me.antileaf.signature.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import me.antileaf.signature.utils.EasyUnlock;
import me.antileaf.signature.utils.SignatureHelper;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;

import java.util.ArrayList;

public interface EasyUnlockSubscriber {
	default EasyUnlock receiveOnGameOver(GameOverScreen screen) {
		return null;
	}
}
