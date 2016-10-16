package com.foo.dictionary.commands;

import com.foo.dictionary.AppState;

public class VoidCommand implements Command {

    @Override
    public AppState run(AppState currentState) {
        return new AppState(currentState);
    }
}
