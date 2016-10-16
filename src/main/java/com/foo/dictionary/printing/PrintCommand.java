package com.foo.dictionary.printing;

import com.foo.dictionary.AppState;
import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.Commands;
import com.foo.dictionary.translations.DictionaryWord;

public class PrintCommand implements Command {

    private final Boolean defaults;

    public PrintCommand(String commandStr) {
        this.defaults = "default".equals(Commands.trimCommandWord(commandStr));
    }

    @Override
    public AppState run(AppState currentState) {

        PrintingTemplate template = new PrintingTemplate(defaults ?
                currentState.defaultWords :
                currentState.words
        );
        System.out.println(template.draw());

        return new AppState(currentState);
    }
}
