package com.github.micaelapucciariello.pcbook.serializer;

import com.github.micaelapucciariello.pcbook.sample.Generator;
import com.google.protobuf.util.JsonFormat;
import pcbook.PC;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Serializer {
    public void WriteBinaryFile(PC pc, String fileName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(fileName);
        pc.writeTo(outputStream);
        outputStream.close();
    }

    public PC ReadBinaryFile(String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        PC pc = PC.parseFrom(inputStream);
        inputStream.close();
        return pc;
    }

    public void WriteJSONFile(PC pc, String fileName) throws IOException {
        JsonFormat.Printer printer = JsonFormat.printer()
                .includingDefaultValueFields()
                .preservingProtoFieldNames();

        String jsonString = printer.print(pc);
        FileOutputStream outputStream = new FileOutputStream(fileName);
        outputStream.write(jsonString.getBytes());
        outputStream.close();
    }

    public static void main(String[] args) throws IOException {
        Serializer serializer = new Serializer();
        PC pc = new Generator().NewPC();
        serializer.WriteBinaryFile(pc, "pc.bin");
        PC pcto = serializer.ReadBinaryFile("pc.bin");
        serializer.WriteJSONFile(pcto, "pc.json");
    }
}
