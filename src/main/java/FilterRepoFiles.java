import java.io.File;
import java.io.FilenameFilter;

public class FilterRepoFiles implements FilenameFilter{

    @Override
    public boolean accept(File dir, String name) {
        if (dir.isDirectory())
            return false;
        else
            return true;
    }
}
