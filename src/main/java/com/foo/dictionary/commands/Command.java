package com.foo.dictionary.commands;

import com.foo.dictionary.AppState;

public interface Command {

    AppState run(AppState currentState);

}
