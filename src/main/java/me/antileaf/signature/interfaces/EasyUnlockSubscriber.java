package me.antileaf.signature.interfaces;

import com.megacrit.cardcrawl.screens.GameOverScreen;
import me.antileaf.signature.utils.EasyUnlock;

import java.util.ArrayList;

public interface EasyUnlockSubscriber {
	default EasyUnlock receiveOnGameOver(GameOverScreen screen) {
		return null;
	}
	
	default ArrayList<EasyUnlock> receiveOnGameOverMultiUnlocks(GameOverScreen screen) {
		return null;
	}
}
