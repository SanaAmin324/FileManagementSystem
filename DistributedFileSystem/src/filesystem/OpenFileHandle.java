package filesystem;

public class OpenFileHandle {
    private VirtualFileInfo file;
    private String mode; // New field to store mode

    public OpenFileHandle(VirtualFileInfo file, String mode) {
        this.file = file;
        this.mode = mode;
    }

    public void writeToFile(String text) {
        if (!mode.contains("w")) {
            System.out.println("File not opened in write mode!");
            return;
        }
        file.appendData(text);
    }

    public void writeToFile(int position, String text) {
        if (!mode.contains("w")) {
            System.out.println("File not opened in write mode!");
            return;
        }
        file.writeAt(position, text);
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
}
