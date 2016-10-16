package com.foo.dictionary;

import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.CommandsFactory;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println("A simple online dictionary. Enter command or {help} for available commands");

        CommandsFactory commandsFactory = new CommandsFactory();

        AppState state = new AppState(true);
        while(state.isRunning) {
            System.out.print(">> ");
            Scanner s = new Scanner(System.in);

            String commandStr = s.nextLine();
            Command command = commandsFactory.createCommand(commandStr);
            state = command.run(state);
        }
    }

}
