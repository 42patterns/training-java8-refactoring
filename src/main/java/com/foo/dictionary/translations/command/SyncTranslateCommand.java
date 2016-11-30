package com.foo.dictionary.translations.command;

import com.foo.dictionary.App;
import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.Commands;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;

public class SyncTranslateCommand implements Command {

    final String phrase;

    public SyncTranslateCommand(String commandStr) {
        phrase = Commands.trimCommandWord(commandStr);
    }

    @Override
    public void run() {
        final DictionaryClient client = ClientsFactory.getBablaDictionary();

        final ProfanityCheckClient profanityCheck = ClientsFactory.getProfanityClient();

        if (!profanityCheck.isObscenityWord(phrase)) {
            App.APPLICATION_STATE.setTranslations(phrase, client.allTranslationsFor(phrase));
        }
    }

}
