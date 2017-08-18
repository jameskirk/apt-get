package emerge.api.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import emerge.api.PackageService;
import emerge.api.RepositoryService;
import emerge.conf.Configuration;
import emerge.misc.Logger;

public class AptServiceFacade {

	private PackageServiceImpl packageService;

	private RepositoryService repositoryService;

	public AptServiceFacade() {
		packageService = new PackageServiceImpl();
		repositoryService = new RepositoryServiceImpl();

		packageService.setRepositoryService(repositoryService);

		if (!new File(Configuration.emergeProgramDir).exists()) {
			Logger.info("creating .apt-get dir");
			new File(Configuration.emergeProgramDir).mkdir();
		}
		if (!new File(Configuration.ebuildRepositoryDir).exists()) {
			Logger.info("creating .apt-get/repository dir");
			new File(Configuration.ebuildRepositoryDir).mkdir();
		}
		if (!new File(Configuration.installedRepositoryDir).exists()) {
			Logger.info("creating .apt-get/installedRepositoryDir dir");
			new File(Configuration.installedRepositoryDir).mkdir();
		}
		if (!new File(Configuration.makeConfPath).exists()) {
			Logger.info("creating .apt-get/make.conf");
			InputStream ddlStream = this.getClass().getClassLoader().getResourceAsStream("make.conf");
			try (FileOutputStream fos = new FileOutputStream(Configuration.makeConfPath);) {
				byte[] buf = new byte[2048];
				int r;
				while (-1 != (r = ddlStream.read(buf))) {
					fos.write(buf, 0, r);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public PackageService getPackageService() {
		return packageService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

}
