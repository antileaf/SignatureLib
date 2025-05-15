package me.antileaf.signature.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class EasyUnlock {
	private static final Logger logger = LogManager.getLogger(EasyUnlock.class.getName());

	public ArrayList<AbstractCard> cards = null;
	public String title = null;
	public String tip = null;

	public EasyUnlock() {

	}

	public EasyUnlock cards(ArrayList<AbstractCard> cards) {
		this.cards = cards;
		return this;
	}

	public EasyUnlock cards(AbstractCard... cards) {
		this.cards = new ArrayList<>();
		this.cards.addAll(Arrays.asList(cards));
		return this;
	}

	public EasyUnlock IDs(String... IDs) {
		if (IDs == null)
			this.cards = null;
		else {
			this.cards = new ArrayList<>();
			for (String id : IDs) {
				AbstractCard card = CardLibrary.getCopy(id);
				if (SignatureHelperInternal.hasSignature(card)) {
					if (!SignatureHelper.isUnlocked(card.cardID))
						SignatureHelper.unlock(card.cardID, true);
					if (!SignatureHelper.isEnabled(card.cardID))
						SignatureHelper.enable(card.cardID, true);

					this.cards.add(card);
				}
				else
					logger.warn("There is no signature for card {}!", id);
			}
		}

		return this;
	}

	public EasyUnlock IDs(ArrayList<String> IDs) {
		if (IDs != null)
			return this.IDs(IDs.toArray(new String[0]));
		else
			return this.IDs((String[]) null);
	}

	public EasyUnlock title(String title) {
		this.title = title;
		return this;
	}

	public EasyUnlock tip(String tip) {
		this.tip = tip;
		return this;
	}
}
