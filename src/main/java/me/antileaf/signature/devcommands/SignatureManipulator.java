package me.antileaf.signature.devcommands;

import basemod.BaseMod;
import basemod.devcommands.ConsoleCommand;
import basemod.helpers.ConvertHelper;

import java.util.Arrays;

public abstract class SignatureManipulator extends ConsoleCommand {
    public SignatureManipulator() {
        minExtraTokens = 1;
    }

    protected String getCardID(String[] tokens) {
        return this.getCardID(tokens, this.countIndex(tokens));
    }

    protected String getCardID(String[] tokens, int countIndex) {
        String[] cardNameArray = (String[]) Arrays.copyOfRange(tokens, 2, countIndex + 1);
        String cardName = String.join(" ", cardNameArray);
        if (BaseMod.underScoreCardIDs.containsKey(cardName)) {
            cardName = (String)BaseMod.underScoreCardIDs.get(cardName);
        }

        return cardName;
    }

    protected int countIndex(String[] tokens) {
        int countIndex;
        for(countIndex = tokens.length - 1; ConvertHelper.tryParseInt(tokens[countIndex]) != null; --countIndex) {
        }

        return countIndex;
    }

    public void errorMsg() {
        SignatureCommand.cmdHelp();
    }
}
