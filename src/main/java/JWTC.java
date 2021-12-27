import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "jwtc", mixinStandardHelpOptions = true, version = "jwtc 0.1",
        description = "Encode and decode JWT tokens.")
@Slf4j
public class JWTC implements Callable<Integer> {

    @Parameters(index = "0", description = "Specify encode or decode.")
    String option;

    @Parameters(index = "1", description = "User provided payload.")
    String payload;

    @Option(names = {"-s", "--secret"}, description = "Secret key.")
    String secret;

    @Option(names = {"-a", "--algorithm"}, description = "Algorithm to be used.")
    String algorithm;

    @Override
    public Integer call() throws Exception {
        if (option.equals("encode")) {
            log.info("Encoding payload: {}", payload);
            log.info("Using secret: {}", secret);
            log.info("Using algorithm: {}", algorithm);
        } else if (option.equals("decode")) {
            log.info("Decoding payload: {}", payload);
            log.info("Using secret: {}", secret);
            log.info("Using algorithm: {}", algorithm);
        } else {
            log.error("Invalid option: {}", option);
        }
        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new JWTC()).execute(args);
        System.exit(exitCode);
    }
}
