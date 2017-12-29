import java.nio.file.Path;

public class TeXFile{
    private Path path;

    public TeXFile(Path path){
        this.path = path;
    }

    public Path getPath(){
        return this.path;
    }
}
