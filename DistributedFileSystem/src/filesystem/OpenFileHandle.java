package filesystem;

import java.io.Serializable;

public class OpenFileHandle implements Serializable {
    private VirtualFileInfo fileInfo;

    public OpenFileHandle(VirtualFileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    // Append mode: write to end
    public void writeToFile(String text) {
        fileInfo.appendData(text);
        System.out.println("Appended to file: " + fileInfo.getFileName());
    }

    // Write at a specific position
    public void writeToFile(int position, String text) {
        fileInfo.writeAt(position, text);
        System.out.println("Written to file at position " + position + ": " + fileInfo.getFileName());
    }

    // Read full content
    public String readFromFile() {
        return fileInfo.getContent();
    }

    // Read from specific position and length
    public String readFromFile(int start, int size) {
        return fileInfo.readFrom(start, size);
    }

    // Truncate file
    public void truncateFile(int maxSize) {
        fileInfo.truncate(maxSize);
        System.out.println("File truncated: " + fileInfo.getFileName());
    }

    // Move content within file
    public void moveWithinFile(int from, int to, int size) {
        fileInfo.moveData(from, to, size);
        System.out.println("Moved data in file: " + fileInfo.getFileName());
    }
}
