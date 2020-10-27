import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommitGenerator {
    @Parameter(names = "-n", description = "number of commits")
    int commitsNum =0;

    @Parameter(names = "-r", description = "Path to repo")
    String repoPath;

    public static void main(String[] args){
        CommitGenerator commitGenerator = new CommitGenerator();
        JCommander.newBuilder().addObject(commitGenerator).build().parse(args);

        if((commitGenerator.repoPath!=null)&&(commitGenerator.commitsNum > 0))
            commitGenerator.generateCommits();
        else
            System.out.println("Define correct parameters!");
    }

    protected void generateCommits() {
        File repoDir = new File(repoPath);
        if (!repoDir.exists()){
            System.out.println("Path to repo doesn't exist");
            return;
        }
        else{
            String[] listDirFiles = repoDir.list();
            List<String> gitRepo = Arrays.asList(listDirFiles).stream().filter(f->f.equals(".git")).collect(Collectors.toList());
            if(gitRepo.size()==0){
                System.out.println("Not a git directory");
                return;
            }
            else{
                
            }

        }


    }
}
