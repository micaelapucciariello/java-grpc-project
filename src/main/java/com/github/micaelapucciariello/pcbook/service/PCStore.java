package com.github.micaelapucciariello.pcbook.service;

import pcbook.PC;

public interface PCStore {
    void Save(PC pc) throws Exception;
    PC Find(String id);
}
