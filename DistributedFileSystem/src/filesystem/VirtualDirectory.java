package filesystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VirtualDirectory implements Serializable {
    private String name;
    private VirtualDirectory parent;
    private List<VirtualDirectory> subdirectories;
    private List<VirtualFileInfo> files;

    public VirtualDirectory(String name, VirtualDirectory parent) {
        this.name = name;
        this.parent = parent;
        this.subdirectories = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public void addFile(VirtualFileInfo file) {
        files.add(file);
    }

    public boolean removeFile(String fileName) {
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getFileName().equals(fileName)) {
                files.remove(i);
                return true;
            }
        }
        return false;
    }

    public void addSubdirectory(VirtualDirectory dir) {
        subdirectories.add(dir);
    }

    public VirtualDirectory getSubdirectory(String dirName) {
        for (VirtualDirectory dir : subdirectories) {
            if (dir.getName().equals(dirName)) {
                return dir;
            }
        }
        return null;
    }

    public VirtualFileInfo getFile(String fileName) {
        for (VirtualFileInfo file : files) {
            if (file.getFileName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    public VirtualDirectory getParent() {
        return parent;
    }

    public void listContents() {
        System.out.println("Directory: " + name);
        System.out.println("Subdirectories:");
        for (VirtualDirectory dir : subdirectories) {
            System.out.println("  " + dir.getName());
        }
        System.out.println("Files:");
        for (VirtualFileInfo file : files) {
            System.out.println("  " + file.getFileName() + " (Size: " + file.getSize() + ")");
        }
    }

    public String getName() {
        return name;
    }

    public List<VirtualDirectory> getSubdirectories() {
        return subdirectories;
    }

    public List<VirtualFileInfo> getFiles() {
        return files;
    }
}
