package com.gdc.nms.setup.util.os;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gdc.nms.setup.util.Environment;

public class UnixOS implements OperatingSystem {

    @Override
    public Path getDefaultInstallationPath() {
        return Environment.getUserHome();
    }

    @Override
    public String getArchitecture() {
        return System.getProperty("os.arch");
    }

    private void createShellScript(Path path, String executable, String args) throws IOException {
        Files.createFile(path);
        OutputStream stream = Files.newOutputStream(path);
        try {
            stream.write("#!/bin/bash\n".getBytes());
            stream.flush();
            stream.write(Environment.getJava().toString().concat(" -jar " + executable + " " + args + "\n").getBytes());
            stream.flush();
        } finally {
            stream.close();
        }

    }

    @Override
    public boolean createLinks(Path installationPath, String client) throws Exception {
        Path path = installationPath.resolve("clients");
        Files.createDirectories(path);
        Path uninstaller = path.resolve("uninstaller.sh");
        if (Files.notExists(uninstaller)) {
            this.createShellScript(uninstaller, installationPath.resolve("uninstaller.jar").toString(), "");
        }
        Path clientPath = path.resolve(client + ".sh");
        if (Files.notExists(clientPath)) {
            this.createShellScript(clientPath, installationPath.resolve("executor.jar").toString(), "execute " + client);
        }

        Path newClient = path.resolve("NewClient.sh");
        if (Files.notExists(newClient)) {
            this.createShellScript(newClient, installationPath.resolve("executor.jar").toString(), "download");
        }
        return true;
    }

}
