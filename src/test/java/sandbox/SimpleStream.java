package sandbox;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleStream {

    final URL url = getClass().getResource("/dictionary.log");

    @Test
    public void iterations() throws IOException, URISyntaxException {

        // tag::simple-iterations[]
        List<String> warnings = new ArrayList<>();
        int counter = 0;

        File f = new File(url.toURI());
        BufferedReader reader = new BufferedReader(new FileReader(f));

        String line = reader.readLine();
        while (counter < 10 && line != null) {
            if (line.contains("WARN")) {
                warnings.add(line);
                counter++;
            }
            line = reader.readLine();
        }
        // end::simple-iterations[]

        warnings.forEach(System.out::println);
    }

    @Test
    public void stream() throws IOException, URISyntaxException {

        // tag::simple-stream[]
        List<String> warnings = Files.lines(Paths.get(url.toURI()))
                .filter(l -> l.contains("WARN"))
                .limit(10)
                .collect(Collectors.toList());
        // end::simple-stream[]

        warnings.forEach(System.out::println);
    }

}
