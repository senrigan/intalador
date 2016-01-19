package com.gdc.nms.setup.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Installation {

	private static Path INSTALLATIONPATH;

	private static final Set<String> clients = new HashSet<String>();

	private static final Set<String> files = new HashSet<String>();

	private static long libVersion;

	private static boolean installed;

	public static void setInstallationPath(Path installationPath) {
		INSTALLATIONPATH = installationPath;
	}

	public static boolean isClientInstalled(String client) {
	    return clients.contains(client);
	}

	public static Path getInstallaPath() {
		return INSTALLATIONPATH;
	}

	public static boolean addClient(String client) {
		if (clients.contains(client)) {
			return false;
		}
		return clients.add(client);
	}

	public static boolean removeClient(String client) {
		if (clients.contains(client)) {
			return clients.remove(client);
		}
		return false;
	}

	public static void setClients(Set<String> clients){
		Installation.clients.clear();
		Installation.clients.addAll(clients);
	}

	public static Set<String> getClients() {
		return new HashSet<String>(clients);
	}

	public static Set<String> getFiles() {
		return new HashSet<String>(files);
	}

	public static boolean addFile(String file) {
		if (files.contains(file)) {
			return false;
		}
		return files.add(file);
	}

	public static boolean removeFile(String file) {
		if (files.contains(file)) {
			return files.remove(files);
		}
		return false;
	}

	public static void setFiles(Set<String> files){
		Installation.files.clear();
		Installation.files.addAll(files);
	}

	protected static void parseInstallationPath(String installationPath) {
		if (installationPath == null || installationPath.isEmpty()) {
			throw new InstallationParseException("Installation Path was not set.");
		}
		Path path = Paths.get(installationPath);
		if (Files.exists(path) && Files.isDirectory(path)) {
			setInstallationPath(path);
		} else {
			throw new InstallationParseException("Installation Path does not exist.");
		}
	}

	protected static void parseFiles(String filesStr) {
		if (filesStr == null || filesStr.isEmpty()) {
			return;
		}
		String[] files = filesStr.split(Character.toString(Constants.SEPARATOR));
		for (String file : files) {
			if (file == null || file.isEmpty()) {
				continue;
			}
			addFile(file);
		}
	}

	protected static void parseClients(String clientsStr) {
		if (clientsStr == null || clientsStr.isEmpty()) {
			return;
		}
		String[] clients = clientsStr.split(Character.toString(Constants.SEPARATOR));
		for (String client : clients) {
			if (client == null || client.isEmpty()) {
				continue;
			} else if (Files.exists(INSTALLATIONPATH.resolve(client)) && Files.isDirectory(INSTALLATIONPATH.resolve(client))) {
				addClient(client);
			}
		}
	}

	protected static void parseLibVersion(String libVersionStr) {
	    if (libVersionStr == null || libVersionStr.isEmpty()) {
	        throw new InstallationParseException("Invalid lib version.");
	    }
	    try {
	        libVersion = Long.parseLong(libVersionStr);
	    } catch (NumberFormatException e) {
	        throw new InstallationParseException("Invalid lib version.");
	    }
	}

	protected static void setInstalled(boolean installed) {
		Installation.installed = installed;
	}

	public static void load() throws InstallationParseException {
		setInstalled(false);
		Path registry = getInstallationRegistry();
		if (Files.exists(registry)) {
			Properties properties = new Properties();
			try {
				properties.load(Files.newInputStream(registry));
				parseInstallationPath(properties.getProperty(Constants.NMS_INSTALLATION_PATH_PROPERTY));
				parseLibVersion(properties.getProperty(Constants.NMS_INSTALLATION_LIB_VERSION_PROPERTY));
				parseFiles(properties.getProperty(Constants.NMS_INSTALLED_FILES));
				parseClients(properties.getProperty(Constants.NMS_INSTALLED_CLIENTS));
				setInstalled(true);
			} catch (IOException e) {
				throw new InstallationParseException("Unexpected error while trying to read the installation file.", e);
			}
		}
	}

	public static boolean isInstalled() {
		return installed;
	}

	public static void setLibVersion(long libVersion) {
        Installation.libVersion = libVersion;
    }

	public static long getLibVersion() {
	    return libVersion;
	}

	private static String convertSetToString(Set<String> set) {
		StringBuilder builder = new StringBuilder();
		for (String value : set) {
			builder.append(value).append(Constants.SEPARATOR);
		}
		if (builder.length() > 0 && builder.charAt(builder.length() - 1) == Constants.SEPARATOR) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}

	protected static Path getInstallationRegistry() {
		return Environment.getNMSCachePath().resolve(Constants.INSTALLATION_REGISTRY_NAME);
	}
	
	public static void store() throws IOException {
		Properties properties = new Properties();
		properties.setProperty(Constants.NMS_INSTALLATION_PATH_PROPERTY, getInstallaPath().toString());
		properties.setProperty(Constants.NMS_INSTALLATION_LIB_VERSION_PROPERTY, Long.toString(getLibVersion()));
		properties.setProperty(Constants.NMS_INSTALLED_CLIENTS, convertSetToString(clients));
		properties.setProperty(Constants.NMS_INSTALLED_FILES, convertSetToString(files));

		if (Files.notExists(Environment.getNMSCachePath())) {
			Files.createDirectories(Environment.getNMSCachePath());
		}
		if (Files.notExists(getInstallationRegistry())) {
			Files.createFile(getInstallationRegistry());
		}
		OutputStream outputStream = Files.newOutputStream(getInstallationRegistry());
		properties.store(outputStream, "");
	}

	private Installation() {
	}
}
