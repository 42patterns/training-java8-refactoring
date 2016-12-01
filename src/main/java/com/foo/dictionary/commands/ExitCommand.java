package com.foo.dictionary.commands;

import com.foo.dictionary.App;
import com.foo.dictionary.AppState;

public class ExitCommand implements Command {


    private final AppState state;

    public ExitCommand(AppState state) {
        this.state = state;
    }

    @Override
    public void run() {
        state.stop();
    }
}
