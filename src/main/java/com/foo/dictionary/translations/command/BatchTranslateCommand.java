package com.foo.dictionary.translations.command;

import com.foo.dictionary.AppState;
import com.foo.dictionary.commands.Command;
import com.foo.dictionary.commands.Commands;
import com.foo.dictionary.translations.client.DictionaryClient;
import com.foo.dictionary.translations.profanity.ProfanityCheckClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.AbstractMap;
import java.util.Optional;
import java.util.stream.Stream;

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

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(openFileOrDefault(file))
        )) {

            reader.lines()
                    .skip(1)
                    .filter(s -> !profanityCheck.isObscenityWord(s))
                    .map(s -> new Tuple<>(s, client.firstTranslationFor(s)))
                    .map(t -> t.getValue().map(w ->
                            Optional.of(new Tuple<>(t.getKey(), w))).orElse(Optional.empty()))
                    .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
//                    .filter(e -> e.getValue().isPresent())
                    .forEach(e -> state.setTranslation(e.getKey(), e.getValue()));

        } catch (IOException e) {
            log.info("Problem reading stream: ", e);
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

class Tuple<T, U> extends AbstractMap.SimpleEntry<T, U> {

    public Tuple(T key, U value) {
        super(key, value);
    }
}