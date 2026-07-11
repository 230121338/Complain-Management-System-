package tut.ac.za.complaint.model;

/** Raw binary image together with its MIME content type. */
public class ImageData {

    private final byte[] data;
    private final String contentType;

    public ImageData(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }
}
