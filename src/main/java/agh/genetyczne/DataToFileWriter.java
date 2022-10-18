package agh.genetyczne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataToFileWriter {
    private String filename;
    private String data;

    void writeToFile() throws IOException {
        DataOutputStream output = new DataOutputStream(new FileOutputStream(filename));
        output.writeChars(data);
        output.close();
    }
}
