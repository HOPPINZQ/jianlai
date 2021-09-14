package com.hoppinzq.service.Proxy;

public class User_proxy implements IUser {
    @Override
    public void add() {
        System.out.println("===add===");
    }
}