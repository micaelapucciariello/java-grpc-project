package com.github.micaelapucciariello.pcbook.service;

import pcbook.PC;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryPCStore implements PCStore {
    private ConcurrentHashMap<String, PC> data;

    public InMemoryPCStore() {
       data = new ConcurrentHashMap<>(0);
    }
    @Override
    public void Save(PC pc) throws Exception {
        if(data.containsKey(pc.getId())){
            throw new AlreadyExistsException("pc ID already exists");
        }

        // deep copy
        PC other = pc.toBuilder().build();
        data.put(other.getId(), other);
    }

    @Override
    public PC Find(String id) {
        if(!data.containsKey(id)){
            return null;
        }

        return data.get(id).toBuilder().build();
    }
}
