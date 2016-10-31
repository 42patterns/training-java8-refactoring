package com.foo.dictionary.translations.command;

import com.foo.dictionary.AppState;
import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.Commands;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;

// TODO: clone this class and make the profanity check call async with supplyAsync
//  after that map it with actual translation with thenCompose() method
//  at first the translation will be always called - can you make it not return
//  profanity word translation at all? tip: use a completed Future with
//  CompletableFuture.completedFuture()
public class SyncTranslateCommand implements Command {

    private final AppState state;
    private final String phrase;

    public SyncTranslateCommand(AppState state, String commandStr) {
        this.state = state;
        this.phrase = Commands.trimCommandWord(commandStr);
    }

    @Override
    public void run() {
        final DictionaryClient client = state.clients().getBablaDictionary();
        final ProfanityCheckClient profanityCheck = state.clients().getProfanityClient();

        if (!profanityCheck.isObscenityWord(phrase)) {
            state.setTranslations(phrase, client.allTranslationsFor(phrase));
        }
    }

}
