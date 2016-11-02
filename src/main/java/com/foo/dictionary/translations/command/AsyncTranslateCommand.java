package com.foo.dictionary.translations.command;

import ch.qos.logback.core.util.ExecutorServiceUtil;
import com.foo.dictionary.App;
import com.foo.dictionary.AppState;
import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.Commands;
import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

// TODO: clone this class and make the profanity check call async with supplyAsync
//  after that map it with actual translation with thenCompose() method
//  how can we make the consecutive call not return
//  profanity word translation at all? tip: use a completed Future with
//  CompletableFuture.completedFuture()
public class AsyncTranslateCommand implements Command {

    private static Logger log = LoggerFactory.getLogger(AsyncTranslateCommand.class);
    final String phrase;
    private final AppState state;

    public AsyncTranslateCommand(AppState state, String commandStr) {
        this.state = state;
        this.phrase = Commands.trimCommandWord(commandStr);
    }

    @Override
    public void run() {
        final ProfanityCheckClient profanityCheck = state.clients().getProfanityClient();
        final DictionaryClient bablaDictionary = state.clients().getBablaDictionary();
        final DictionaryClient dictDictionary = state.clients().getDictDictionary();

        // maybe the second call isn't made
        CompletableFuture<List<DictionaryWord>> future =
                CompletableFuture.supplyAsync(() -> profanityCheck.isObscenityWord(phrase))
                    .thenCompose(b -> b ? CompletableFuture.completedFuture(Collections.emptyList()) :
                            CompletableFuture.supplyAsync(() -> dictDictionary.allTranslationsFor(phrase))
                                    .applyToEither(CompletableFuture.supplyAsync(() -> bablaDictionary.allTranslationsFor(phrase)),
                                            Function.identity())
                    );

        state.setTranslations(phrase, future.join());
    }

}
