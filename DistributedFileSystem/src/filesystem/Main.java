package filesystem;

import java.util.*;
import java.io.*;

public class Main {
    private static FileSystemManager fileSystem = new FileSystemManager();
    private static Map<String, OpenFileHandle> openFiles = new HashMap<>();

    public static void main(String[] args) {
        // ---------- Threaded User Execution ----------
        if (args.length == 1) {
            try {
                int numThreads = Integer.parseInt(args[0]);

                for (int i = 1; i <= numThreads; i++) {
                    String inputFileName = "data/input_thread" + i + ".txt";
                    String outputFileName = "data/output_thread" + i + ".txt";

                    Thread thread = new Thread(new ThreadedUser(inputFileName, outputFileName));
                    thread.start();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of threads. Please provide a valid integer.");
                return;
            }
        }

        // ---------- Interactive Shell ----------
        Scanner scanner = new Scanner(System.in);
        System.out.println("             ***** WELCOME TO THE VIRTUAL DISTRIBUTED FILE SYSTEM! *****");
        System.out.println(
                "----------------------------------------------------------------------------------------------");

        while (true) {
            System.out.print("\n> ");
            String[] input = scanner.nextLine().split(" ", 2);
            String command = input[0];
            String[] argsList = input.length > 1 ? input[1].split(" ") : new String[0];

            switch (command.toLowerCase()) {
                case "mkdir":
                    if (argsList.length >= 1)
                        fileSystem.mkdir(argsList[0]);
                    break;
                case "chdir":
                    if (argsList.length >= 1)
                        fileSystem.chdir(argsList[0]);
                    break;
                case "create":
                    if (argsList.length >= 1)
                        fileSystem.createFile(argsList[0]);
                    break;
                case "delete":
                    if (argsList.length >= 1)
                        fileSystem.deleteFile(argsList[0]);
                    break;
                case "open":
                    if (argsList.length >= 2) {
                        String filename = argsList[0];
                        String mode = argsList[1];
                        OpenFileHandle handle = fileSystem.openFile(filename, mode);
                        if (handle != null) {
                            openFiles.put(filename, handle);
                        }
                    } else {
                        System.out.println("Usage: open <filename> <mode>");
                    }
                    break;
                case "close":
                    if (argsList.length >= 1)
                        openFiles.remove(argsList[0]);
                    break;
                case "write":
                    if (argsList.length >= 2 && openFiles.containsKey(argsList[0])) {
                        openFiles.get(argsList[0]).writeToFile(argsList[1]);
                    }
                    break;
                case "writeat":
                    if (argsList.length >= 3 && openFiles.containsKey(argsList[0])) {
                        int position = Integer.parseInt(argsList[1]);
                        openFiles.get(argsList[0]).writeToFile(position, argsList[2]);
                    }
                    break;
                case "read":
                    if (argsList.length >= 1 && openFiles.containsKey(argsList[0])) {
                        System.out.println(openFiles.get(argsList[0]).readFromFile());
                    }
                    break;
                case "readfrom":
                    if (argsList.length >= 3 && openFiles.containsKey(argsList[0])) {
                        int start = Integer.parseInt(argsList[1]);
                        int size = Integer.parseInt(argsList[2]);
                        System.out.println(openFiles.get(argsList[0]).readFromFile(start, size));
                    }
                    break;
                case "move":
                    if (argsList.length >= 4 && openFiles.containsKey(argsList[0])) {
                        int from = Integer.parseInt(argsList[1]);
                        int to = Integer.parseInt(argsList[2]);
                        int size = Integer.parseInt(argsList[3]);
                        openFiles.get(argsList[0]).moveWithinFile(from, to, size);
                    }
                    break;
                case "truncate":
                    if (argsList.length >= 2 && openFiles.containsKey(argsList[0])) {
                        int size = Integer.parseInt(argsList[1]);
                        openFiles.get(argsList[0]).truncateFile(size);
                    }
                    break;
                case "memmap":
                    fileSystem.showMemoryMap();
                    break;
                case "ls":
                    fileSystem.listFiles();
                    break;
                case "exit":
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    return;
                case "movefile":
                    if (argsList.length >= 2)
                        fileSystem.moveFile(argsList[0], argsList[1]);
                    break;
                default:
                    System.out.println("Unknown command.");
            }
        }
    }
}
