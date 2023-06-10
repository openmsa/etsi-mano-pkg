/**
 *     Copyright (C) 2019-2023 Ubiqube.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;

import com.ubiqube.etsi.mano.repository.ByteArrayResource;
import com.ubiqube.etsi.mano.repository.ManoResource;
import com.ubiqube.etsi.mano.sol004.CsarModeEnum;
import com.ubiqube.etsi.mano.test.ZipUtil;
import com.ubiqube.etsi.mano.test.ZipUtil.Entry;

class Sol004PreOnboardingTest {

	@Test
	void testSingleZip() throws IOException {
		final FileInputStream fis = buildZip();
		final ManoResource res = new ByteArrayResource(fis.readAllBytes(), "ubi-tosca.csar");
		final Sol004PreOnboarding srv = new Sol004PreOnboarding(res);
		final CsarModeEnum mode = srv.getMode();
		assertEquals(CsarModeEnum.SINGLE_ZIP, mode);
	}

	private static FileInputStream buildZip() throws IOException, FileNotFoundException {
		ZipUtil.makeToscaZip("/tmp/ubi-tosca.csar", Entry.of("ubi-tosca/Definitions/tosca_ubi.yaml", "Definitions/tosca_ubi.yaml"),
				Entry.of("ubi-tosca/Definitions/etsi_nfv_sol001_vnfd_types.yaml", "Definitions/etsi_nfv_sol001_vnfd_types.yaml"),
				Entry.of("ubi-tosca/Definitions/etsi_nfv_sol001_common_types.yaml", "Definitions/etsi_nfv_sol001_common_types.yaml"),
				Entry.of("ubi-tosca/TOSCA-Metadata/TOSCA.meta", "TOSCA-Metadata/TOSCA.meta"));
		return new FileInputStream("/tmp/ubi-tosca.csar");
	}

	@Test
	void testMultiZip() throws IOException {
		buildZip();
		final FileOutputStream fos = new FileOutputStream("/tmp/double.zip");
		final ZipOutputStream zipOut = new ZipOutputStream(fos);
		final InputStream is = new FileInputStream("/tmp/ubi-tosca.csar");
		final ZipEntry zipEntry = new ZipEntry("ubi-tosca.csar");
		zipOut.putNextEntry(zipEntry);
		zipOut.putNextEntry(new ZipEntry("tmp/"));
		final byte[] bytes = new byte[1024];
		int length;
		while ((length = is.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		is.close();
		zipOut.close();
		fos.close();
		//
		final FileInputStream ff = new FileInputStream("/tmp/double.zip");
		final ManoResource res = new ByteArrayResource(ff.readAllBytes(), "ubi-tosca.csar");
		final Sol004PreOnboarding srv = new Sol004PreOnboarding(res);
		final CsarModeEnum mode = srv.getMode();
		assertEquals(CsarModeEnum.DOUBLE_ZIP, mode);
	}

	@Test
	void testSingleFile() throws IOException {
		ZipUtil.makeToscaZip("/tmp/ubi-tosca.csar", Entry.of("ubi-tosca/Definitions/tosca_ubi.yaml", "Definitions/tosca_ubi.yaml"));
		final FileInputStream ff = new FileInputStream("/tmp/ubi-tosca.csar");
		final ManoResource res = new ByteArrayResource(ff.readAllBytes(), "ubi-tosca.csar");
		final Sol004PreOnboarding srv = new Sol004PreOnboarding(res);
		final CsarModeEnum mode = srv.getMode();
		assertEquals(CsarModeEnum.SINGLE_ZIP, mode);
	}

	@Test
	void testSingleFile2() throws IOException {
		ZipUtil.makeToscaZip("/tmp/ubi-tosca.csar", Entry.of("ubi-tosca/Definitions/tosca_ubi.yaml", "Definitions/tosca_ubi.yml"));
		final FileInputStream ff = new FileInputStream("/tmp/ubi-tosca.csar");
		final ManoResource res = new ByteArrayResource(ff.readAllBytes(), "ubi-tosca.csar");
		final Sol004PreOnboarding srv = new Sol004PreOnboarding(res);
		final CsarModeEnum mode = srv.getMode();
		assertEquals(CsarModeEnum.SINGLE_ZIP, mode);
	}

	@Test
	void testUnknown() throws IOException {
		ZipUtil.makeToscaZip("/tmp/ubi-tosca.csar", Entry.of("ubi-tosca/Definitions/tosca_ubi.yaml", "Definitions/tosca_ubi.txt"));
		final FileInputStream ff = new FileInputStream("/tmp/ubi-tosca.csar");
		final ManoResource res = new ByteArrayResource(ff.readAllBytes(), "ubi-tosca.csar");
		final Sol004PreOnboarding srv = new Sol004PreOnboarding(res);
		final CsarModeEnum mode = srv.getMode();
		assertEquals(CsarModeEnum.UNKNOWN, mode);
	}
}
