package com.foo.dictionary;

import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.CommandsFactory;
import com.foo.dictionary.printing.PrintingTemplate;
import com.foo.dictionary.translations.DictionaryWord;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class App {

    final private static View view = new View();
    final public static AppState APPLICATION_STATE = new AppStateImpl(view);

    public static void main(String[] args) {
        view.help();
        CommandsFactory commandsFactory = new CommandsFactory();

        while (APPLICATION_STATE.isRunning()) {
            view.prompt();
            Scanner s = new Scanner(System.in);

            String commandStr = s.nextLine();
            Command command = commandsFactory.createCommand(commandStr);
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
    public void setDefaults(String phrase, DictionaryWord translation) {
        this.translations.put(phrase, Arrays.asList(translation));
    }

    @Override
    public Map<String, List<DictionaryWord>> getTranslations() {
        return new HashMap<String, List<DictionaryWord>>(translations);
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

