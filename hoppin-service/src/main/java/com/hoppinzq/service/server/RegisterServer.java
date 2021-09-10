package com.hoppinzq.service.server;

import com.hoppinzq.service.brap.service.ServiceWrapper;

import java.util.List;

public interface RegisterServer {

    void insertService(ServiceWrapper serviceWrapper);

    void insertServices(List<ServiceWrapper> serviceWrappers);
}
