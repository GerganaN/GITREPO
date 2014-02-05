/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaterPlantStatisticsServer {

    public static void main(String[] args) {
        Database database = new Database();

        try {
            int port = convertPort(args[0]);
            ServerSocket serverListener = new ServerSocket(port);
            ExecutorService pool = Executors.newCachedThreadPool();
            while (true) {
                Socket clientConnection = serverListener.accept();
                pool.execute(new ClientServiceThread(clientConnection, database));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static int convertPort(String portString) throws IllegalArgumentException {
        int port = Integer.parseInt(portString);
        if (port < 0 || port > 64000) {
            throw new IllegalArgumentException();
        }
        return port;
    }
}
