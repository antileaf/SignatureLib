package me.antileaf.signature.interfaces;

import com.megacrit.cardcrawl.screens.GameOverScreen;
import me.antileaf.signature.utils.EasyUnlock;

public interface EasyUnlockSubscriber {
	default EasyUnlock receiveOnGameOver(GameOverScreen screen) {
		return null;
	}
}
