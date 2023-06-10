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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ubiqube.etsi.mano.repository.ByteArrayResource;
import com.ubiqube.etsi.mano.repository.ManoResource;
import com.ubiqube.etsi.mano.repository.VnfPackageRepository;
import com.ubiqube.etsi.mano.service.cond.ConditionService;
import com.ubiqube.etsi.mano.test.ZipUtil;
import com.ubiqube.etsi.mano.test.ZipUtil.Entry;
import com.ubiqube.parser.test.ArtifactDownloader;

import ma.glasnost.orika.OrikaSystemProperties;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;

/**
 *
 * @author Olivier Vignaud
 *
 */
@ExtendWith(MockitoExtension.class)
class ToscaVnfRegistryHandlerTest {
	@Mock
	private VnfPackageRepository repo;
	@Mock
	private ConditionService conditonService;

	public ToscaVnfRegistryHandlerTest() throws MalformedURLException {
		System.setProperty(OrikaSystemProperties.COMPILER_STRATEGY, EclipseJdtCompilerStrategy.class.getName());
		System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES, "true");
		ArtifactDownloader.prepareArtifact("421");
	}

	@Test
	void testIsProcesseble() {
		final ToscaVnfRegistryHandler srv = new ToscaVnfRegistryHandler(repo, conditonService);
		final ManoResource res = new ByteArrayResource(new byte[0], "filename");
		srv.isProcessable(res);
		assertTrue(true);
	}

	@Test
	void testGetProviderName() {
		final ToscaVnfRegistryHandler srv = new ToscaVnfRegistryHandler(repo, conditonService);
		srv.getProviderName();
		assertTrue(true);
	}

	@Test
	void testGetNewReaderInstance() throws IOException {
		ZipUtil.makeToscaZip("/tmp/ubi-tosca.csar", Entry.of("ubi-tosca/Definitions/tosca_ubi.yaml", "Definitions/tosca_ubi.yaml"),
				Entry.of("ubi-tosca/Definitions/etsi_nfv_sol001_vnfd_types.yaml", "Definitions/etsi_nfv_sol001_vnfd_types.yaml"),
				Entry.of("ubi-tosca/Definitions/etsi_nfv_sol001_common_types.yaml", "Definitions/etsi_nfv_sol001_common_types.yaml"),
				Entry.of("ubi-tosca/TOSCA-Metadata/TOSCA.meta", "TOSCA-Metadata/TOSCA.meta"));
		final ToscaVnfRegistryHandler srv = new ToscaVnfRegistryHandler(repo, conditonService);
		final InputStream is = new FileInputStream("/tmp/ubi-tosca.csar");
		srv.getNewReaderInstance(is, UUID.randomUUID());
		assertTrue(true);
	}

	@Test
	void testGetFileSystem() {
		final ToscaVnfRegistryHandler srv = new ToscaVnfRegistryHandler(repo, conditonService);
		final ManoResource res = new ByteArrayResource(new byte[0], "filename");
		srv.getFileSystem(res);
		assertTrue(true);
	}

}
