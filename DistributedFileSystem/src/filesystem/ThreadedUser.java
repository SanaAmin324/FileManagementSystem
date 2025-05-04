package filesystem;

import java.io.*;
import java.util.*;

public class ThreadedUser implements Runnable {
    private String inputFileName;
    private String outputFileName;
    private static FileSystemManager fileSystem = new FileSystemManager();
    private static Map<String, OpenFileHandle> openFiles = new HashMap<>();

    public ThreadedUser(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
                PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] input = line.split(" ", 2);
                String command = input[0].toLowerCase();
                String[] argsList = input.length > 1 ? input[1].split(" ", 3) : new String[0];

                switch (command) {
                    case "create":
                        if (argsList.length >= 1) {
                            fileSystem.createFile(argsList[0]);
                            writer.println("File created: " + argsList[0]);
                        }
                        break;
                    case "open":
                        if (argsList.length >= 2) {
                            String filename = argsList[0];
                            String mode = argsList[1];
                            OpenFileHandle handle = fileSystem.openFile(filename, mode);
                            if (handle != null) {
                                openFiles.put(filename, handle);
                                writer.println("File opened: " + filename + " with mode: " + mode);
                            } else {
                                writer.println("Failed to open file: " + filename);
                            }
                        } else {
                            writer.println("Invalid open command.");
                        }
                        break;
                    case "write":
                        if (argsList.length >= 2) {
                            String filename = argsList[0];
                            String data = argsList[1].replaceAll("^\"|\"$", "");
                            if (openFiles.containsKey(filename)) {
                                openFiles.get(filename).writeToFile(data);
                                writer.println("Data written to " + filename);
                            } else {
                                writer.println("File not open: " + filename);
                            }
                        } else {
                            writer.println("Invalid write_to_file command.");
                        }
                        break;
                    case "close":
                        if (argsList.length >= 1) {
                            String filename = argsList[0];
                            fileSystem.closeFile(filename);
                            writer.println("File closed: " + filename);
                        }
                        break;
                    default:
                        writer.println("Unknown command: " + command);
                }
            }
        } catch (IOException e) {
            System.err.println("Error in thread processing: " + inputFileName);
            e.printStackTrace();
        }
    }
}
