package com.gdc.nms.setup.util.os.windows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import mslinks.ShellLink;

import com.gdc.nms.setup.Installer;
import com.gdc.nms.setup.util.Installation;
import com.gdc.nms.setup.util.os.OperatingSystem;

public class WindowsOS implements OperatingSystem {

    @Override
    public Path getDefaultInstallationPath() {
        return Paths.get(System.getenv("ProgramFiles"));
    }

    @Override
    public String getArchitecture() {
        return System.getProperty("os.arch");
    }

    private void createShortcut(String src, String workingDir, String cmdArgs, String iconLocation, String dest)
            throws IOException {
        ShellLink.createLink(src).setWorkingDir(workingDir).setCMDArgs(cmdArgs).setIconLocation(iconLocation)
            .saveTo(dest);
    }

    @Override
    public boolean createLinks(Path installationPath, String client) {
        String KEY = "HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders";

        // Getting Common Program
        try {
            String commonProgramsStr = WindowsUtil.readRegistry(KEY, "Common Programs", "REG_SZ");
            Path destDir = Paths.get(commonProgramsStr, "NMS");
            if (Files.notExists(destDir)) {
                Files.createDirectory(destDir);
                Installation.addFile(destDir.toString());
            }

            Path newClient = destDir.resolve("Descargar.lnk");
            if (Files.notExists(newClient)) {
                try {
                    this.createShortcut(installationPath.resolve("executor.exe").toString(),
                        installationPath.toString(), "download", Installation.getInstallaPath().resolve("images")
                            .resolve("gdc_logo.ico").toString(), newClient.toString());
                    Installation.addFile(newClient.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Installer.getInstance().addMessage(
                        "Ocurrio un error al crear el acceso directo para crear un nuevo cliente.");
                }
            }

            Path clientPath = destDir.resolve(client + ".lnk");
            if (Files.notExists(clientPath)) {
                try {
                    this.createShortcut(installationPath.resolve("executor.exe").toString(),
                        installationPath.toString(), "execute " + client,
                        Installation.getInstallaPath().resolve("images").resolve("gdc_logo.ico").toString(),
                        clientPath.toString());
                    Installation.addFile(clientPath.toString());
                } catch (Exception e) {
                    Installer.getInstance().addMessage("Ocurrio un error al crear el acceso directo del cliente.");
                }
            }

            Path uninstall = destDir.resolve("Uninstaller.lnk");
            if (Files.notExists(uninstall)) {
                try {
                    this.createShortcut(installationPath.resolve("uninstaller.exe").toString(),
                        installationPath.toString(), "",
                        Installation.getInstallaPath().resolve("images").resolve("setup.ico").toString(),
                        uninstall.toString());
                    Installation.addFile(uninstall.toString());
                } catch (Exception e) {
                    Installer.getInstance().addMessage("Ocurrio un error al crear el acceso directo al desinstalador.");
                }
            }

        } catch (Exception e) {
            Installer.getInstance().addMessage("Ocurrio un error al crear los iconos en el Menu de Inicio.");
        }
        return false;
    }

}
