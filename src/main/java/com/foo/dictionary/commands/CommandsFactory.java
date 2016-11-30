package com.foo.dictionary.commands;

import com.foo.dictionary.translations.command.BatchTranslateCommand;
import com.foo.dictionary.translations.command.SyncTranslateCommand;

public class CommandsFactory {

    public Command createCommand(String commandStr) {
        if (Commands.isNull(commandStr)) {
            return new VoidCommand();
        }

        if (commandStr.trim().equals("exit")) {
            return new ExitCommand();
        }

        if (commandStr.trim().startsWith("translate")) {
            return new SyncTranslateCommand(commandStr);
        }

        if (commandStr.trim().startsWith("batch")) {
            return new BatchTranslateCommand(commandStr);
        }

        return new VoidCommand();
    };

}
