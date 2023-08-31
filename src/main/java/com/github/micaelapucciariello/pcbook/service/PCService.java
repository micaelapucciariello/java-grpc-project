package com.github.micaelapucciariello.pcbook.service;

import io.grpc.Status;
import pcbook.PC;
import pcbook.PCServiceGrpc;
import pcbook.ServicePc;

import java.util.UUID;
import java.util.logging.Logger;

public class PCService extends PCServiceGrpc.PCServiceImplBase {
    public static final Logger logger = Logger.getLogger(PCService.class.getName());

    private PCStore store;

    public PCService(PCStore store){
        this.store = store;
    }

    @Override
    public void createPC(pcbook.ServicePc.CreatePCRequest request,
                         io.grpc.stub.StreamObserver<pcbook.ServicePc.CreatePCResponse> responseObserver){
         PC pc = request.getPc();

         String id = pc.getId();

         UUID uuid = null;
         if (id.isEmpty()){
             uuid = UUID.randomUUID();
         } else {
             try {
                 uuid = UUID.fromString(pc.getId());
             } catch (IllegalArgumentException e){
                 responseObserver.onError(Status.INVALID_ARGUMENT.
                         withDescription(e.getMessage()).
                         asRuntimeException());
             }
         }

        assert uuid != null;
        PC other = pc.toBuilder().setId(uuid.toString()).build();
        try {
            store.Save(other);
        } catch (AlreadyExistsException e){
            Status.ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException();

        } catch (Exception e){
            Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException();
        }

        ServicePc.CreatePCResponse response = ServicePc.CreatePCResponse.newBuilder().setId(other.getId()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        logger.info("pc saved with id: " + pc.getId());
    }
}
