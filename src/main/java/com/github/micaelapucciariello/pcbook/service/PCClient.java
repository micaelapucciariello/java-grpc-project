package com.github.micaelapucciariello.pcbook.service;

import com.github.micaelapucciariello.pcbook.sample.Generator;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import pcbook.PC;
import pcbook.PCServiceGrpc;
import pcbook.ServicePc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PCClient {
    public static final Logger logger = Logger.getLogger(PCClient.class.getName());

    private final ManagedChannel channel;
    private final PCServiceGrpc.PCServiceBlockingStub blockingStub;

    public PCClient(String host, int port){
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = PCServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException{
        channel.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }

    public void CreatePC(PC pc){
        ServicePc.CreatePCRequest request = ServicePc.CreatePCRequest.newBuilder().setPc(pc).build();
        ServicePc.CreatePCResponse response = ServicePc.CreatePCResponse.getDefaultInstance();

        try {
            response = blockingStub.createPC(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.ALREADY_EXISTS) {
                logger.log(Level.INFO, "pc id already exists: " + e.getMessage());
            }
            return;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "request failed: " + e.getMessage());
            return;
        }

        logger.info("pc created with id " + response.getId());
    }

    public static void main(String[] args) throws InterruptedException {
        PCClient client = new PCClient("0.0.0.0", 8080);
        Generator generator = new Generator();
        PC pc = generator.NewPC();

        try{
            client.CreatePC(pc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            client.shutdown();
        }
    }
}
