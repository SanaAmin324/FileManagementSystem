package filesystem;

import java.io.Serializable;

public class VirtualFileInfo implements Serializable {
    private String fileName;
    private StringBuilder content;

    public VirtualFileInfo(String fileName) {
        this.fileName = fileName;
        this.content = new StringBuilder();
    }

    public String getFileName() {
        return fileName;
    }

    public int getSize() {
        return content.length();
    }

    public String getContent() {
        return content.toString();
    }

    // Appends data at the end
    public void appendData(String text) {
        content.append(text);
    }

    // Writes data at a specific position (overwrites)
    public void writeAt(int position, String text) {
        if (position < 0 || position > content.length()) {
            System.out.println("Invalid position");
            return;
        }

        // Expand content if necessary
        while (content.length() < position) {
            content.append(" ");
        }

        // Overwrite text
        content.replace(position, position + text.length(), text);
    }

    // Reads data from a specific start and size
    public String readFrom(int start, int size) {
        if (start < 0 || start >= content.length()) {
            return "";
        }

        int end = Math.min(start + size, content.length());
        return content.substring(start, end);
    }

    // Truncate content
    public void truncate(int maxSize) {
        if (maxSize < content.length()) {
            content.setLength(maxSize);
        }
    }

    // Move a block of content within the file
    public void moveData(int from, int to, int size) {
        if (from < 0 || to < 0 || from + size > content.length()) {
            System.out.println("Invalid move range");
            return;
        }

        String dataToMove = content.substring(from, from + size);
        // First, delete the data from original location
        content.delete(from, from + size);

        // Adjust 'to' position if necessary (since content got shorter)
        if (to > from) {
            to -= size;
        }

        // Insert data at the new location
        content.insert(to, dataToMove);
    }
}
