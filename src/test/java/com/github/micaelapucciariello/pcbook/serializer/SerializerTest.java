package com.github.micaelapucciariello.pcbook.serializer;

import com.github.micaelapucciariello.pcbook.sample.Generator;
import org.junit.Assert;
import org.junit.Test;
import pcbook.PC;

import java.io.IOException;

import static org.junit.Assert.*;

public class SerializerTest {

    @Test
    public void writeAndReadBinaryFile() throws IOException {
        String file = "pc.bin";
        Generator generator = new Generator();
        PC pc = generator.NewPC();

        Serializer serializer = new Serializer();
        serializer.WriteBinaryFile(pc, file);

        PC pc1 = serializer.ReadBinaryFile(file);
        Assert.assertEquals(pc, pc1);
    }
}