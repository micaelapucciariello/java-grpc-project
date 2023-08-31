package com.github.micaelapucciariello.pcbook.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PCServer {
    public static final Logger logger = Logger.getLogger(PCServer.class.getName());

    private final int port;
    private final Server server;

    public PCServer(int port, PCStore store){
        this(ServerBuilder.forPort(port), port, store);
    }

    public PCServer(ServerBuilder serverBuilder, int port, PCStore store){
        this.port = port;
        PCService service = new PCService(store);
        server = serverBuilder.addService(service).build();

    }

    public void start() throws IOException{
        server.start();
        logger.info("started server on port: " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                System.err.println("shut down gRPC because JVM shuts down");
                try {
                    PCServer.this.stop();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.err.println("server shut down");
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null){
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        InMemoryPCStore inMemoryPCStore = new InMemoryPCStore();
        PCServer pcServer = new PCServer(8080, inMemoryPCStore);
        pcServer.start();
        pcServer.blockUntilShutdown();
    }
}