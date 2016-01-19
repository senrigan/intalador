package com.gdc.nms.setup.util;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.gdc.nms.setup.util.os.GenericOS;
import com.gdc.nms.setup.util.os.OperatingSystem;
import com.gdc.nms.setup.util.os.UnixOS;
import com.gdc.nms.setup.util.os.windows.WindowsOS;

public class Environment {

	private static final OperatingSystem OS;

	static {
		if (isWindows()) {
			OS = new WindowsOS();
		} else if (isUnix()) {
		    OS = new UnixOS();
		}else {
			OS = new GenericOS();
		}
	}

	public static OperatingSystem getOS() {
        return OS;
    }

	 public static Path getTempPath() {
	        return Paths.get(System.getProperty(Constants.TEMP_DIR_PROPERTY));
	    }

	    public static Path getCurrentPath() {
	        Path path = getSelfJar();
	        if (Files.isRegularFile(path)) {
	            return path.getParent();
	        }
	        return path;
	    }

	    public static Path getJavaHome() {
	        return Paths.get(System.getProperty(Constants.JAVA_PATH_PROPERTY));
	    }

	    public static Path getJava() {
	        String name = "java";
	        if (isWindows()) {
	            name = "java.exe";
	        }
	        return getJavaHome().resolve("bin/" + name);
	    }

	    public static Path getSelfJar() {
	        try {
	            return Paths.get(Environment.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	        } catch (URISyntaxException e) {
	            return null;
	        }
	    }

	public static String getOsName() {
		return System.getProperty(Constants.OS_NAME_PROPERTY, Constants.UNKNOWN_PROPERTY_VALUE);
	}

	public static Path getUserHome() {
		return Paths.get(System.getProperty(Constants.USER_HOME_PROPERTY));
	}

	public static Path getNMSCachePath() {
		return getUserHome().resolve(Constants.NMS_CACHE_FOLDER_NAME);
	}

	public static boolean isWindows() {
		return getOsName().toLowerCase().contains("win");
	}

	public static boolean isUnix() {
		String osName = getOsName().toLowerCase();
		return osName.contains("nux") || osName.contains("nix");
	}

	private Environment() {
	}
}
