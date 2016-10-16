package com.foo.dictionary.commands;

import com.foo.dictionary.AppState;

public class ExitCommand implements Command {

    @Override
    public AppState run(AppState currentState) {
        return new AppState(false);
    }
}
