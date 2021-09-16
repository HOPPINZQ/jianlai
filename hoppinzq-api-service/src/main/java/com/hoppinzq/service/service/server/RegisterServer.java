package com.hoppinzq.service.service.server;



import com.hoppinzq.service.service.ServiceWrapper;

import java.util.List;

public interface RegisterServer {

    void insertService(ServiceWrapper serviceWrapper);

    void insertServices(List<ServiceWrapper> serviceWrappers);

    int updateServices(List<ServiceWrapper> serviceWrappers);

    void deleteServices(List<ServiceWrapper> serviceWrappers);

    void queryServices(ServiceWrapper serviceWrapper);
}
