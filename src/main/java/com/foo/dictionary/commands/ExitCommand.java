package com.foo.dictionary.commands;

import com.foo.dictionary.App;

public class ExitCommand implements Command {

    @Override
    public void run() {
        App.APPLICATION_STATE.stop();
    }
}
