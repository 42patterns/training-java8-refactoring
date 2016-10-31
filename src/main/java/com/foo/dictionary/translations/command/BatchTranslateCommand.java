package com.foo.dictionary.translations.command;

import com.foo.dictionary.AppState;
import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.Commands;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BatchTranslateCommand implements Command {

    private final static String DEFAULT_FILE = "/batch.csv";
    private static Logger log = LoggerFactory.getLogger(BatchTranslateCommand.class);
    private final String file;
    private final AppState state;

    public BatchTranslateCommand(AppState state, String commandStr) {
        this.state = state;
        this.file = Commands.trimCommandWord(commandStr);
    }

    //TODO: replace CSV handling with a BufferedReader.lines()
    //      - use skip() if not all lines are required
    @Override
    public void run() {
        final DictionaryClient client = state.clients().getBablaDictionary();
        final ProfanityCheckClient profanityCheck = state.clients().getProfanityClient();
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

        for (String s : wordsToTranslate) {
            if (!profanityCheck.isObscenityWord(s)) {
                client.firstTranslationFor(s).ifPresent(word ->
                        state.setTranslation(s, word));
            }
        }
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
