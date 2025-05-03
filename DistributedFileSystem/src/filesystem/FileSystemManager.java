package filesystem;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileSystemManager {
    private VirtualDirectory root;
    private VirtualDirectory currentDirectory;
    private final String dataFilePath = "data/sample.dat";
    private Map<String, OpenFileHandle> openFiles = new HashMap<>();

    public FileSystemManager() {
        loadFromDisk();
        if (root == null) {
            root = new VirtualDirectory("root", null);
        }
        currentDirectory = root;
    }

    // Synchronize methods to ensure thread-safety
    public synchronized void createFile(String fileName) {
        if (currentDirectory.getFile(fileName) == null) {
            VirtualFileInfo file = new VirtualFileInfo(fileName);
            currentDirectory.addFile(file);
            System.out.println("File created: " + fileName);
        } else {
            System.out.println("File already exists.");
        }
    }

    public synchronized void deleteFile(String fileName) {
        boolean removed = currentDirectory.removeFile(fileName);
        if (removed) {
            System.out.println("File deleted: " + fileName);
        } else {
            System.out.println("File not found.");
        }
    }

    public synchronized void mkdir(String dirName) {
        if (currentDirectory.getSubdirectory(dirName) == null) {
            VirtualDirectory newDir = new VirtualDirectory(dirName, currentDirectory);
            currentDirectory.addSubdirectory(newDir);
            System.out.println("Directory created: " + dirName);
        } else {
            System.out.println("Directory already exists.");
        }
    }

    public synchronized void chdir(String dirName) {
        if (dirName.equals("..")) {
            if (currentDirectory.getParent() != null) {
                currentDirectory = currentDirectory.getParent();
            } else {
                System.out.println("Already at root directory.");
            }
        } else {
            VirtualDirectory target = currentDirectory.getSubdirectory(dirName);
            if (target != null) {
                currentDirectory = target;
            } else {
                System.out.println("Directory not found.");
            }
        }
    }

    public synchronized String showMemoryMap() {
        System.out.println("Memory Map (File System Structure):");
        printDirectoryContents(root, 0); // Start from the root directory
        return dataFilePath;
    }

    private synchronized void printDirectoryContents(VirtualDirectory directory, int indentLevel) {
        // Print current directory
        String indent = "  ".repeat(indentLevel);
        System.out.println(indent + "Directory: " + directory.getName());

        // Print files in the current directory
        for (VirtualFileInfo file : directory.getFiles()) {
            System.out.println(indent + "  File: " + file.getFileName());
        }

        // Recursively print subdirectories
        for (VirtualDirectory subDir : directory.getSubdirectories()) {
            printDirectoryContents(subDir, indentLevel + 1);
        }
    }

    public synchronized void listFiles() {
        System.out.println("Listing files in " + currentDirectory.getName() + ":");
        for (VirtualFileInfo file : currentDirectory.getFiles()) {
            System.out.println("  File: " + file.getFileName());
        }
    }

    public synchronized void listContents() {
        currentDirectory.listContents();
    }

    // Ensure thread-safe saving and loading
    public synchronized void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFilePath))) {
            oos.writeObject(root);
            System.out.println("File system saved.");
        } catch (IOException e) {
            System.out.println("Error saving to disk: " + e.getMessage());
        }
    }

    public synchronized void loadFromDisk() {
        File file = new File(dataFilePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                root = (VirtualDirectory) ois.readObject();
                System.out.println("File system loaded from disk.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading from disk: " + e.getMessage());
            }
        }
    }

    // Synchronize file opening for thread safety
    public synchronized OpenFileHandle openFile(String fileName, String mode) {
        VirtualFileInfo file = currentDirectory.getFile(fileName);
        if (file == null) {
            System.out.println("File not found.");
            return null;
        }
        OpenFileHandle handle = new OpenFileHandle(file, mode);
        openFiles.put(fileName, handle); // Ensure that files are opened in a thread-safe way
        return handle;
    }

    public synchronized String getCurrentDirectoryPath() {
        return currentDirectory.getName();
    }

    // Synchronize move operation
    public synchronized void moveFile(String fileName, String targetDirName) {
        VirtualFileInfo file = currentDirectory.getFile(fileName);
        if (file == null) {
            System.out.println("File not found: " + fileName);
            return;
        }

        VirtualDirectory targetDir = currentDirectory.getSubdirectory(targetDirName);
        if (targetDir == null) {
            System.out.println("Target directory not found: " + targetDirName);
            return;
        }

        // First remove from current directory
        boolean removed = currentDirectory.removeFile(fileName);
        if (!removed) {
            System.out.println("Error removing file from current directory.");
            return;
        }

        // THEN add to target directory
        targetDir.addFile(file);

        System.out.println("File moved successfully.");
        System.out.println("Saving to disk...");
        showMemoryMap(); // Show current state
        saveToDisk();
    }

    // Synchronize writeToFile method
    public synchronized void writeToFile(String filename, String content) {
        OpenFileHandle handle = openFiles.get(filename);
        if (handle == null) {
            System.out.println("File not open: " + filename);
            return;
        }
        handle.writeToFile(content);
    }

    // Synchronize closeFile method
    public synchronized void closeFile(String filename) {
        if (!openFiles.containsKey(filename)) {
            System.out.println("File is not open: " + filename);
            return;
        }

        OpenFileHandle handle = openFiles.get(filename);
        handle.close(); // Optional: implement a `close` method in OpenFileHandle if needed
        openFiles.remove(filename);
        System.out.println("File closed: " + filename);
    }
}
