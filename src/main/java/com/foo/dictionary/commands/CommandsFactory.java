package com.foo.dictionary.commands;

import com.foo.dictionary.AppState;
import com.foo.dictionary.translations.command.AsyncTranslateCommand;
import com.foo.dictionary.translations.command.BatchTranslateCommand;
import com.foo.dictionary.translations.command.SyncTranslateCommand;

public class CommandsFactory {

    public Command createCommand(AppState state, String commandStr) {
        if (Commands.isNull(commandStr)) {
            return new VoidCommand();
        }

        if (commandStr.trim().equals("exit")) {
            return new ExitCommand(state);
        }

        if (commandStr.trim().startsWith("translate")) {
//            return new SyncTranslateCommand(state, commandStr);
            return new AsyncTranslateCommand(state, commandStr);
        }

        if (commandStr.trim().startsWith("batch")) {
            return new BatchTranslateCommand(state, commandStr);
        }

        return new VoidCommand();
    };

}
