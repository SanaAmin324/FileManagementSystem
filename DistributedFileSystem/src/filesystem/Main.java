package filesystem;

import java.util.*; // for Map, HashMap, etc.

public class Main {
    private static FileSystemManager fileSystem = new FileSystemManager();
    private static Map<String, OpenFileHandle> openFiles = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Virtual Distributed File System");

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
                    if (argsList.length >= 1) {
                        OpenFileHandle handle = fileSystem.openFile(argsList[0]);
                        if (handle != null) {
                            openFiles.put(argsList[0], handle);
                        }
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
                default:
                    System.out.println("Unknown command.");
            }
        }
    }
}
