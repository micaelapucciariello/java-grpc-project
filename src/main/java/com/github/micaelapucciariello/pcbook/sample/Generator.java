package com.github.micaelapucciariello.pcbook.sample;

import com.google.protobuf.Timestamp;
import pcbook.*;

import java.time.Instant;
import java.util.Objects;
import java.util.Random;

public class Generator {
    private Random rand;

    public Generator() {
        rand = new Random();
    }

    public PC NewPC() {
        Instant now = Instant.now();
        String brand = randomPCBrand();
        double price = randomDouble(100, 2000);
        int releaseYear = randomInt(2017, 2023);
        Memory memory = Memory.newBuilder()
                .setUnit(Memory.Unit.GBYTE)
                .setValue("64")
                .build();

        return PC.newBuilder()
                .setBrand(brand)
                .setCpu(NewCPU())
                .addGpu(NewGPU())
                .setScreen(NewScreen())
                .addMemory(memory)
                .setUsdPrice(price)
                .setReleaseYear(releaseYear)
                .setUpdatedAt(Timestamp.newBuilder().setNanos(now.getNano()).build())
                .build();
    }

    public CPU NewCPU() {
        String brand = randomCPUBrand();
        String name = randomCPUName(brand);
        int cores = randomInt(2, 4);
        int threads = randomInt(cores, 8);
        double min_ghz = randomDouble(2, 3.5);
        double max_ghz = randomDouble(min_ghz, 5);

        return CPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setCores(cores)
                .setThreads(threads)
                .setMinGhz(min_ghz)
                .setMaxGhz(max_ghz)
                .build();
    }

    public GPU NewGPU() {
        String brand = randomGPUBrand();
        int cores = randomInt(2, 4);
        int threads = randomInt(cores, 8);
        double min_ghz = randomDouble(2, 3.5);
        double max_ghz = randomDouble(min_ghz, 5);
        Memory memory = Memory.newBuilder()
                .setUnit(Memory.Unit.GBYTE)
                .setValue("64")
                .build();

        return GPU.newBuilder()
                .setBrand(brand)
                .setName(brand)
                .setCores(cores)
                .setThreads(threads)
                .setMinGhz(min_ghz)
                .setMaxGhz(max_ghz)
                .setMemory(memory)
                .build();
    }

    public Screen NewScreen() {
        int width = randomInt(1024, 4320);
        int height = width * (9 / 16);

        return Screen.newBuilder()
                .setResolution(Screen.Resolution.newBuilder()
                        .setHeight(height)
                        .setWidth(width)
                        .build())
                .setMultitouch(false)
                .setPanel(Screen.Panel.IPS)
                .setSizeInch(randomInt(10, 24))
                .build();
    }

    public Storage NewHDD() {
        Memory memory = Memory.newBuilder()
                .setUnit(Memory.Unit.GBYTE)
                .setValue("128")
                .build();

        return Storage.newBuilder()
                .setDriver(Storage.Driver.HDD)
                .setMemory(memory)
                .build();
    }

    public Storage NewSSD() {
        Memory memory = Memory.newBuilder()
                .setUnit(Memory.Unit.GBYTE)
                .setValue("240")
                .build();

        return Storage.newBuilder()
                .setDriver(Storage.Driver.SSD)
                .setMemory(memory)
                .build();
    }

    public String randomCPUBrand() {
        return randomStringFromSet("AMD, Intel");
    }


    public String randomPCBrand() {
        return randomStringFromSet("Apple",
                "Dell",
                "Acer");
    }

    public String randomGPUBrand() {
        return randomStringFromSet("NVIDIA", "AMD");
    }

    public String randomCPUName(String brand) {
        if (Objects.equals(brand, "Intel")) {
            return randomStringFromSet(
                    "intel-i3",
                    "intel-i5",
                    "intel-i7",
                    "intel-i9");
        }

        return randomStringFromSet(
                "rizen-3-PRO",
                "rizen-5-PRO",
                "rizen-7-PRO");
    }

    private int randomInt(int min, int max) {
        return min + rand.nextInt(max - min + 1);
    }

    private double randomDouble(double min, double max) {
        return min + rand.nextDouble(max - min + 1);
    }

    public String randomStringFromSet(String... s) {
        int n = s.length;
        if (n == 0) {
            return "";
        }
        return s[rand.nextInt(n)];
    }

    public static void main(String[] args) {
        Generator generator = new Generator();
        PC pc = generator.NewPC();
        System.out.println(pc);
    }

}
