package com.oliver.bingo90.file;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileService {

        private static FileService fileService;

        private FileService()
        {

        }
        public static FileService getInstance()
        {
                if(fileService==null)
                {
                        fileService = new FileService();
                }
                return fileService;
        }
        public void saveTextToFile(List<String> textToSave) throws IOException {

                FileWriter writer = new FileWriter("Tickets.txt");
                for(String str: textToSave) {
                        writer.write(str + System.lineSeparator());
                }
                writer.close();
        }
}
