package filesystem;

import java.io.*;

public class FileSystemManager {
    private VirtualDirectory root;
    private VirtualDirectory currentDirectory;
    private final String dataFilePath = "data/sample.dat";

    public FileSystemManager() {
        loadFromDisk();
        if (root == null) {
            root = new VirtualDirectory("root", null);
        }
        currentDirectory = root;
    }

    public void createFile(String fileName) {
        if (currentDirectory.getFile(fileName) == null) {
            VirtualFileInfo file = new VirtualFileInfo(fileName);
            currentDirectory.addFile(file);
            System.out.println("File created: " + fileName);
        } else {
            System.out.println("File already exists.");
        }
    }

    public void deleteFile(String fileName) {
        boolean removed = currentDirectory.removeFile(fileName);
        if (removed) {
            System.out.println("File deleted: " + fileName);
        } else {
            System.out.println("File not found.");
        }
    }

    public void mkdir(String dirName) {
        if (currentDirectory.getSubdirectory(dirName) == null) {
            VirtualDirectory newDir = new VirtualDirectory(dirName, currentDirectory);
            currentDirectory.addSubdirectory(newDir);
            System.out.println("Directory created: " + dirName);
        } else {
            System.out.println("Directory already exists.");
        }
    }

    public void chdir(String dirName) {
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

    public void showMemoryMap() {
        System.out.println("Memory Map (File System Structure):");
        printDirectoryContents(root, 0); // Start from the root directory
    }

    private void printDirectoryContents(VirtualDirectory directory, int indentLevel) {
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

    public void listFiles() {
        System.out.println("Listing files in " + currentDirectory.getName() + ":");
        for (VirtualFileInfo file : currentDirectory.getFiles()) {
            System.out.println("  File: " + file.getFileName());
        }
    }

    public void listContents() {
        currentDirectory.listContents();
    }

    public void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFilePath))) {
            oos.writeObject(root);
            System.out.println("File system saved.");
        } catch (IOException e) {
            System.out.println("Error saving to disk: " + e.getMessage());
        }
    }

    public void loadFromDisk() {
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

    public OpenFileHandle openFile(String fileName) {
        VirtualFileInfo file = currentDirectory.getFile(fileName);
        if (file != null) {
            return new OpenFileHandle(file);
        } else {
            System.out.println("File not found.");
            return null;
        }
    }

    public String getCurrentDirectoryPath() {
        return currentDirectory.getName();
    }
}
