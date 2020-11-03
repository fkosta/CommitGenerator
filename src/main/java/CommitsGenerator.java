import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommitsGenerator {
    @Parameter(names = "-n", description = "number of commits")
    int commitsNum;

    @Parameter(names = "-r", description = "repo path")
    String repoPath;

    @Parameter(names = "-f", description = "File with commits")
    String commitsFile;

    public static void main(String[] args) throws IOException {
        CommitsGenerator commitsGenerator = new CommitsGenerator();
        JCommander.newBuilder().addObject(commitsGenerator).build().parse(args);
        commitsGenerator.generateCommits();
    }

    private void generateCommits() throws IOException {
        File repoDir = new File(repoPath);
        File commitsCSV = new File(commitsFile);
        String[] command;
        String pattern;
        String parsedData;
        int fileIndex=0;
        String previousCommit, currentCommit, currentTree;
        Random rand = new Random();
        if((new File(repoPath+"\\.git")).exists()){
            String[] listOfRepo = repoDir.list();
            List<String> listOfRepoFiles = Arrays.stream(listOfRepo).filter(f->(isFile(f)==true)).collect(Collectors.toList());
            if(listOfRepoFiles.size() > 0){
               BufferedWriter bW = new BufferedWriter(new FileWriter(commitsCSV));
               command = new String[]{"git", "log", "--max-count=1", "","",""};
               pattern = "^commit\\s(.+)Author:(.+)";
               previousCommit = executeCommand(command, pattern);
               for(int i0=0;i0<commitsNum;i0++){
                   fileIndex = rand.nextInt(listOfRepoFiles.size());
                   command = new String[]{"cmd","/c","echo","\"Random string\"",">>",repoPath+"\\"+listOfRepoFiles.get(fileIndex)};
                   pattern="";
                   parsedData = executeCommand(command, pattern);
                   command = new String[]{"git","add",".","","",""};
                   pattern="";
                   parsedData = executeCommand(command, pattern);
                   command = new String[]{"git","commit","-m","GP-01 new commit from Alpharetta","",""};
                   pattern="";
                   parsedData = executeCommand(command, pattern);
                   command = new String[]{"git", "log", "--max-count=1", "","",""};
                   pattern = "^commit\\s(.+)Author:(.+)";
                   currentCommit = executeCommand(command, pattern);
                   command = new String[]{"git","cat-file","-p","HEAD","",""};
                   pattern="tree\\s(.+)parent(.+)";
                   currentTree = executeCommand(command, pattern);
                   bW.write(previousCommit+","+currentCommit+","+currentTree+"\n");
                   previousCommit = currentCommit;
               }
               bW.close();
            }
        }
        else
            return;
    }

    private boolean isFile(String f) {
        File testFile = new File(repoPath+"\\"+f);
        if(testFile.isFile())
            return true;
        else
            return false;
    }

    private String executeCommand(String[] command, String patternString) {
        String line = "";
        String commandOutput = "";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command[0],command[1],command[2],command[3],command[4],command[5]);
            processBuilder.directory(new File(repoPath));
            Process process = processBuilder.start();
            InputStreamReader isReader = new InputStreamReader(process.getInputStream());
            BufferedReader bR = new BufferedReader(isReader);
            StringBuilder sB = new StringBuilder();

            while((line = bR.readLine())!=null){
                sB.append(line);
            }

            String response = sB.toString();

            if(!patternString.equals("")){
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(response);
                if(matcher.find())
                    commandOutput = matcher.group(1);
            }

            int exitVal = process.waitFor();

        }
        catch (IOException | InterruptedException ioE){
            ioE.printStackTrace();
        }
        return commandOutput;
    }
}
