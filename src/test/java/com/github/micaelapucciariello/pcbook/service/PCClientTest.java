package com.github.micaelapucciariello.pcbook.service;

import com.github.micaelapucciariello.pcbook.sample.Generator;
import io.grpc.ManagedChannel;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pcbook.PC;
import pcbook.PCServiceGrpc;
import pcbook.ServicePc;

import static org.junit.Assert.*;

public class PCClientTest {

    private PCStore store;
    private PCServer server;
    private ManagedChannel channel;

    @Rule
    public final GrpcCleanupRule cleanupRule = new GrpcCleanupRule();

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        InProcessServerBuilder serverBuilder = InProcessServerBuilder.forName(serverName).directExecutor();
        store = new InMemoryPCStore();
        server = new PCServer(serverBuilder, 0, store);
        server.start();

        channel = cleanupRule.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void createPCWithValidID(){
        Generator generator = new Generator();
        PC pc = generator.NewPC();
        ServicePc.CreatePCRequest request = ServicePc.CreatePCRequest.newBuilder().setPc(pc).build();

        PCServiceGrpc.PCServiceBlockingStub blockingStub = PCServiceGrpc.newBlockingStub(channel);
        ServicePc.CreatePCResponse response = blockingStub.createPC(request);
        assertNotNull(response);
        assertEquals(pc.getId(), response.getId());

        PC found = store.Find(pc.getId());
        assertEquals(pc, found);
    }

    @Test
    public void createPCWithEmptyID(){
        Generator generator = new Generator();
        PC pc = generator.NewPC().toBuilder().setId("").build();
        ServicePc.CreatePCRequest request = ServicePc.CreatePCRequest.newBuilder().setPc(pc).build();

        PCServiceGrpc.PCServiceBlockingStub blockingStub = PCServiceGrpc.newBlockingStub(channel);
        ServicePc.CreatePCResponse response = blockingStub.createPC(request);
        assertNotNull(response);
        assertFalse(response.getId().isEmpty());

        PC found = store.Find(response.getId());
        assertNotNull(found);
    }

    @Test(expected = StatusRuntimeException.class)
    public void createPCWithInvalidID(){
        Generator generator = new Generator();
        PC pc = generator.NewPC().toBuilder().setId("invalid").build();
        ServicePc.CreatePCRequest request = ServicePc.CreatePCRequest.newBuilder().setPc(pc).build();

        PCServiceGrpc.PCServiceBlockingStub blockingStub = PCServiceGrpc.newBlockingStub(channel);
        ServicePc.CreatePCResponse response = blockingStub.createPC(request);
        assertNotNull(response);
        assertFalse(response.getId().isEmpty());
    }

    @Test(expected = StatusRuntimeException.class)
    public void createPCWithAlreadyExistsID() throws Exception {
        Generator generator = new Generator();
        PC pc = generator.NewPC().toBuilder().setId("invalid").build();
        store.Save(pc);
        ServicePc.CreatePCRequest request = ServicePc.CreatePCRequest.newBuilder().setPc(pc).build();

        PCServiceGrpc.PCServiceBlockingStub blockingStub = PCServiceGrpc.newBlockingStub(channel);
        ServicePc.CreatePCResponse response = blockingStub.createPC(request);
        assertNotNull(response);
        assertFalse(response.getId().isEmpty());
    }
}