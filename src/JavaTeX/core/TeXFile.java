package JavaTeX.core;

import java.nio.file.Path;

/**
 * This class represents a TeX source file.
 */
public class TeXFile{
    private Path path;

    public TeXFile(Path path){
        this.path = path;
    }

    public Path getPath(){
        return this.path;
    }
}
