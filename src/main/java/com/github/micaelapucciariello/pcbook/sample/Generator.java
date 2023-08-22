package com.github.micaelapucciariello.pcbook.sample;

import pcbook.CPU;

import java.util.Objects;
import java.util.Random;

public class Generator {
    private Random rand;

    public Generator() {
        rand = new Random();
    }

    public CPU NewCPU() {
        String brand = randomCPUBrand();
        String name = randomCPUName(brand);
        int cores = randomInt(2, 4);
        int threads = randomInt(cores, 8);
        double min_ghz = randomDouble(2,3.5);
        double max_ghz = randomDouble(min_ghz,5);

        return CPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setCores(cores)
                .setThreads(threads)
                .setMinGhz(min_ghz)
                .setMaxGhz(max_ghz)
                .build();
    }

    public String randomCPUBrand() {
        return randomStringFromSet("AMD, Intel");
    }

    public String randomGPUBrand() {
        return randomStringFromSet("NVIDIA","AMD");
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

    private int randomInt(int min, int max){
        return min + rand.nextInt(max-min+1);
    }

    private double randomDouble(double min, double max){
        return min + rand.nextDouble(max-min+1);
    }

    public String randomStringFromSet(String... s) {
        int n = s.length;
        if (n == 0) {
            return "";
        }
        return s[rand.nextInt(n)];
    }
}
