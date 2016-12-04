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

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

// TODO: clone this class and make the profanity check call async with supplyAsync
//  after that map it with actual translation with thenCompose() method
//  how can we make the consecutive call not return
//  profanity word translation at all? tip: use a completed Future with
//  CompletableFuture.completedFuture()
public class AsyncTranslateCommand implements Command {

    final Function<List<DictionaryWord>, List<DictionaryWord>> it = Function.identity();

    private static Logger log = LoggerFactory.getLogger(AsyncTranslateCommand.class);
    final String phrase;
    private final AppState state;

    public AsyncTranslateCommand(AppState state, String commandStr) {
        this.state = state;
        this.phrase = Commands.trimCommandWord(commandStr);
    }

    @Deprecated
    public void run_only_with_single_dictionary() {

        // tag::async-code[]
        final ProfanityCheckClient profanityCheck = state.clients().getProfanityClient();
        final DictionaryClient bablaDictionary = state.clients().getBablaDictionary();

        CompletableFuture<List<DictionaryWord>> future =
                supplyAsync(() -> profanityCheck.isObscenityWord(phrase))
                    .thenCompose(b -> b ? completedFuture(emptyList()) :
                            supplyAsync(() -> bablaDictionary.allTranslationsFor(phrase))
                    );

        state.setTranslations(phrase, future.join());
        // end::async-code[]
    }


    @Override
    public void run() {
        final ProfanityCheckClient profanityCheck = state.clients().getProfanityClient();
        final DictionaryClient bablaDictionary = state.clients().getBablaDictionary();
        final DictionaryClient dictDictionary = state.clients().getDictDictionary();


        // tag::async-either[]
        CompletableFuture<Boolean> futureIsObscenity =
            supplyAsync(() -> profanityCheck.isObscenityWord(phrase));
        CompletableFuture<List<DictionaryWord>> futureDictTranslation =
            supplyAsync(() -> dictDictionary.allTranslationsFor(phrase));
        CompletableFuture<List<DictionaryWord>> futureBablaTranslation =
            supplyAsync(() -> bablaDictionary.allTranslationsFor(phrase));

        CompletableFuture<List<DictionaryWord>> future =
            futureIsObscenity
                .thenCompose(b -> b ? completedFuture(emptyList()) :
                    futureDictTranslation.applyToEither(futureBablaTranslation, it)
                );

        state.setTranslations(phrase, future.join());
        // end::async-either[]
    }

}
