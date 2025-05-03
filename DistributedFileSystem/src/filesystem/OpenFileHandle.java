package filesystem;

public class OpenFileHandle {
    private VirtualFileInfo file;
    private String mode;

    public OpenFileHandle(VirtualFileInfo file, String mode) {
        this.file = file;
        this.mode = mode;
    }

    public void writeToFile(String text) {
        if (!mode.contains("w")) {
            System.out.println("File not opened in write mode!");
            return;
        }
        file.appendData(text); // Ensure appendData is correctly implemented in VirtualFileInfo
    }

    public void writeToFile(int position, String text) {
        if (!mode.contains("w")) {
            System.out.println("File not opened in write mode!");
            return;
        }
        file.writeAt(position, text); // Ensure writeAt is correctly implemented in VirtualFileInfo
    }

    public String readFromFile() {
        if (!mode.contains("r")) {
            return "File not opened in read mode!";
        }
        return file.getContent();
    }

    public String readFromFile(int start, int size) {
        if (!mode.contains("r")) {
            return "File not opened in read mode!";
        }
        return file.readFrom(start, size);
    }

    public void moveWithinFile(int from, int to, int size) {
        if (!mode.contains("w")) {
            System.out.println("File not opened in write mode!");
            return;
        }
        file.moveData(from, to, size);
    }

    public void truncateFile(int size) {
        if (!mode.contains("w")) {
            System.out.println("File not opened in write mode!");
            return;
        }
        file.truncate(size);
    }

    public void close() {
        System.out.println("File '" + file.getFileName() + "' has been closed.");
    }
}
