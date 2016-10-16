package com.foo.dictionary.translations.command;

import com.foo.dictionary.AppState;
import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.Commands;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;
import com.foo.dictionary.translations.profanity.ProfanityFallbackStubClient;
import com.foo.dictionary.translations.profanity.PurgoProfanityCheckClient;

public class SyncTranslateCommand implements Command {

    final String phrase;

    public SyncTranslateCommand(String commandStr) {
        phrase = Commands.trimCommandWord(commandStr);
    }

    @Override
    public AppState run(AppState currentState) {
        final DictionaryClient client = ClientsFactory.getBablaDictionary();

        final ProfanityCheckClient profanityCheck = ClientsFactory.getProfanityClient();

        if (!profanityCheck.isObscenityWord(phrase)) {
            return new AppState(currentState, client.allTranslationsFor(phrase));
        } else {
            return new AppState(currentState);
        }
    }

}
