package me.antileaf.signature.devcommands;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import me.antileaf.signature.utils.SignatureHelper;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;

import java.util.ArrayList;
import java.util.Map;

public class SignatureDisable extends SignatureManipulator {
    public SignatureDisable() {
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (tokens[2].equals("all")) {
            for (AbstractCard card : CardLibrary.getAllCards())
                if (SignatureHelperInternal.hasSignature(card) && SignatureHelper.isEnabled(card.cardID))
                    SignatureHelper.enable(card.cardID, false);
        } else
            try {
                SignatureHelper.enable(this.getCardID(tokens), false);
            } catch (Exception e) {
                errorMsg();
            }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> entry : CardLibrary.cards.entrySet())
            if (SignatureHelperInternal.hasSignature(entry.getValue()) && SignatureHelper.isEnabled(entry.getKey()))
                result.add(entry.getKey().replace(' ', '_'));
        result.add("all");
        return result;
    }
}
