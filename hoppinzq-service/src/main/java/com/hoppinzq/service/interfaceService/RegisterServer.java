package com.hoppinzq.service.interfaceService;

import com.hoppinzq.service.serviceImpl.ServiceWrapper;

import java.util.List;

public interface RegisterServer {

    void insertService(ServiceWrapper serviceWrapper);

    void insertServices(List<ServiceWrapper> serviceWrappers);

    int updateServices(List<ServiceWrapper> serviceWrappers);

    void deleteServices(List<ServiceWrapper> serviceWrappers);

    String queryServices(ServiceWrapper serviceWrapper);

    String serviceOk();
}
