package com.github.micaelapucciariello.pcbook.service;

import io.grpc.Status;
import pcbook.PC;
import pcbook.PCServiceGrpc;

import java.util.UUID;
import java.util.logging.Logger;

public class PCService extends PCServiceGrpc.PCServiceImplBase {
    public static final Logger logger = Logger.getLogger(PCService.logger.getName());

    @Override
    public void createPc(pcbook.ServicePc.CreatePCRequest request,
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

    }
}
