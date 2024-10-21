import java.nio.file.Files;
import java.nio.file.Path;
import java.util.spi.ToolProvider;

public final class Build {
    public static void main(String[] args) {
        var javac = ToolProvider.findFirst("javac").orElseThrow();
        var jar = ToolProvider.findFirst("jar").orElseThrow();

        int javacResult = javac.run(
            System.out, 
            System.err,
            "-g",
            "--release", "23",
            "-d", "processor_target",
            "src/ex/Processor.java",
            "src/ex/Marker.java"
        );
        
        if (javacResult != 0) {
            System.exit(javacResult);
        }

        jar.run(
            System.out,
            System.err,
            "--create",
            "--file",
            "./processor.jar",
            "-C", "processor_target", "."
        );

        int nextResult = javac.run(
            System.out, 
            System.err,
            "-g",
            "--release", "23",
            "--processor-path", "./processor.jar",
            "--class-path", "./processor.jar",
            "-proc:full",
            "-d", "target",
            "src/ex/Ex.java",
            "src/ex/TypeUse.java",
            "src/ex/TypeUseTwo.java"
        );

        if (nextResult != 0) {
            System.exit(javacResult);
        }
    }
}
