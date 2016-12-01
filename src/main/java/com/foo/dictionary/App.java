package com.foo.dictionary;

import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.CommandsFactory;
import com.foo.dictionary.printing.PrintingTemplate;
import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.BablaDictionaryClient;
import com.foo.dictionary.translations.client.DictDictionaryClient;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.client.wrappers.ArtificialSleepWrapper;
import com.foo.dictionary.translations.client.wrappers.FallbackStubClient;
import com.foo.dictionary.translations.client.wrappers.LoggingWrapper;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;
import com.foo.dictionary.translations.profanity.ProfanityFallbackStubClient;
import com.foo.dictionary.translations.profanity.PurgoProfanityCheckClient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class App {

    final private View view;
    final private AppState state;

    public static void main(String[] args) {
        View view = new View();
        AppState state = new AppStateImpl(view);
        App app = new App(view, state);
        app.run();
    }

    public App(View view, AppState state) {
        this.view = view;
        this.state = state;
    }

    public void run() {
        view.help();
        CommandsFactory commandsFactory = new CommandsFactory();

        while (state.isRunning()) {
            view.prompt();
            Scanner s = new Scanner(System.in);

            String commandStr = s.nextLine();
            Command command = commandsFactory.createCommand(state, commandStr);
            command.run();
        }
    }

}

class AppStateImpl implements AppState {
    final private View view;
    private boolean running = true;
    private Map<String, List<DictionaryWord>> translations = new ConcurrentHashMap<String, List<DictionaryWord>>();

    public AppStateImpl(View view) {
        this.view = view;
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setTranslations(String phrase, List<DictionaryWord> translations) {
        this.translations.put(phrase, translations);
        view.printTranslations(translations);
    }

    @Override
    public void setTranslation(String phrase, DictionaryWord translation) {
        this.translations.put(phrase, Arrays.asList(translation));
    }

    @Override
    public Map<String, List<DictionaryWord>> getTranslations() {
        return new HashMap<String, List<DictionaryWord>>(translations);
    }

    @Override
    public ClientsFactory clients() {
        return new ClientsFactoryImpl(translations);
    }
}

class View {

    public void help() {
        System.out.println("A simple online dictionary. Enter command or {help} for available commands");
    }

    public void prompt() {
        System.out.print(">> ");
    }

    public void printTranslations(List<DictionaryWord> translations) {
        PrintingTemplate template = new PrintingTemplate(translations);
        String txt = template.draw();
        System.out.println(txt);
    }
}

class ClientsFactoryImpl implements AppState.ClientsFactory {

    final private Map<String, List<DictionaryWord>> fallbackTranslations;

    public ClientsFactoryImpl(Map<String, List<DictionaryWord>> fallbackTranslations) {
        this.fallbackTranslations = fallbackTranslations;
    }

    public DictionaryClient getDictDictionary() {
        return new FallbackStubClient(fallbackTranslations,
                new LoggingWrapper(
                        new ArtificialSleepWrapper(
                                new DictDictionaryClient()
                        )
                )
        );
    }

    public DictionaryClient getBablaDictionary() {
        return new FallbackStubClient(fallbackTranslations,
                new LoggingWrapper(
                        new ArtificialSleepWrapper(
                                new BablaDictionaryClient()
                        )
                )
        );
    }

    public ProfanityCheckClient getProfanityClient() {
        return new ProfanityFallbackStubClient(
                new com.foo.dictionary.translations.profanity.LoggingWrapper(
                        new PurgoProfanityCheckClient()
                )
        );
    }

}