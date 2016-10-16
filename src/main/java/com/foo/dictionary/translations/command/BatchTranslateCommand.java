package com.foo.dictionary.translations.command;

import com.foo.dictionary.AppState;
import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.Commands;
import com.foo.dictionary.translations.DictionaryWord;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;
import com.foo.dictionary.translations.profanity.ProfanityFallbackStubClient;
import com.foo.dictionary.translations.profanity.PurgoProfanityCheckClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BatchTranslateCommand implements Command {

    private static Logger log = LoggerFactory.getLogger(BatchTranslateCommand.class);
    final static String DEFAULT_FILE = "/batch.csv";
    final String file;

    public BatchTranslateCommand(String commandStr) {
        file = Commands.trimCommandWord(commandStr);
    }

    @Override
    public AppState run(AppState currentState) {
        final DictionaryClient client = ClientsFactory.getBablaDictionary();

        final InputStream fileStream = openFileOrDefault(file);

        List<String> wordsToTranslate = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(fileStream)
        );

        try {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                wordsToTranslate.add(line);
            }
        } catch (IOException e) {
            log.info("Problem reading stream");
        }

        List<DictionaryWord> translations = new ArrayList<DictionaryWord>();
        for (String s: wordsToTranslate) {
            final ProfanityCheckClient profanityCheck = new ProfanityFallbackStubClient(
                    new PurgoProfanityCheckClient()
            );

            if (!profanityCheck.isObscenityWord(s)) {
                translations.add(client.firstTranslationFor(s));
            }
        }

        return currentState.
                fromCurrent().
                withDefaultWords(translations).
                build();
    }

    private InputStream openFileOrDefault(String filename) {
        try {
            return new FileInputStream(new File(filename));
        } catch (FileNotFoundException e) {
            log.info("Cannot open file={} reason={} - fallback to default", file, e.getMessage());
            return getClass().getResourceAsStream(DEFAULT_FILE);
        }
    }
}
