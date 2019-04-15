package Model;

import java.sql.Blob;

public abstract class ConverterTemplate {
    public abstract boolean checkFile(String filename);
    public abstract String convertFromBlob(Blob blob, String filename);
}
