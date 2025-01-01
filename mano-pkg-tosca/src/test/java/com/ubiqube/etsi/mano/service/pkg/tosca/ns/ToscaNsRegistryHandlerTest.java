/**
 * Copyright (C) 2019-2025 Ubiqube.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.ns;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ubiqube.etsi.mano.repository.ByteArrayResource;
import com.ubiqube.etsi.mano.repository.ManoResource;
import com.ubiqube.etsi.mano.repository.NsdRepository;
import com.ubiqube.etsi.mano.service.pkg.TestFactory;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ArtefactInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsSapMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsVirtualLinkMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsVnfIndicatorMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.PkgMapper;
import com.ubiqube.etsi.mano.test.ZipUtil;
import com.ubiqube.etsi.mano.test.ZipUtil.Entry;
import com.ubiqube.parser.test.ArtifactDownloader;

/**
 *
 * @author Olivier Vignaud
 *
 */
@ExtendWith(MockitoExtension.class)
class ToscaNsRegistryHandlerTest {
	@Mock
	private NsdRepository repo;

	public ToscaNsRegistryHandlerTest() throws MalformedURLException {
		ArtifactDownloader.prepareArtifact("421");
	}

	@Test
	void testgetFileSystem() {
		final ToscaNsRegistryHandler srv = createRegistryHandler();
		final ManoResource res = new ByteArrayResource(new byte[0], "filename");
		srv.getFileSystem(res);
		assertTrue(true);
	}

	private ToscaNsRegistryHandler createRegistryHandler() {
		final PkgMapper mapper = TestFactory.createPkgMapper();
		final NsInformationsMapping nsInformationsMapping = Mappers.getMapper(NsInformationsMapping.class);
		final NsVnfIndicatorMapping nsVnfIndicatorMapping = Mappers.getMapper(NsVnfIndicatorMapping.class);
		final NsSapMapping nsSapMapping = Mappers.getMapper(NsSapMapping.class);
		final NsVirtualLinkMapping nsVirtualLinkMapping = Mappers.getMapper(NsVirtualLinkMapping.class);
		final ArtefactInformationsMapping artefactInformationsMapping = Mappers.getMapper(ArtefactInformationsMapping.class);
		return new ToscaNsRegistryHandler(repo, nsVirtualLinkMapping, mapper, nsVnfIndicatorMapping, nsSapMapping, nsInformationsMapping, artefactInformationsMapping);
	}

	@Test
	void testIsProcessable() {
		final ToscaNsRegistryHandler srv = createRegistryHandler();
		final ManoResource res = new ByteArrayResource(new byte[0], "filename");
		srv.isProcessable(res);
		assertTrue(true);
	}

	@Test
	void testGetProviderName() {
		final ToscaNsRegistryHandler srv = createRegistryHandler();
		srv.getProviderName();
		assertTrue(true);
	}

	@Test
	void testGetNewReaderInstance() throws IOException {
		ZipUtil.makeToscaZip("/tmp/ubi-tosca.csar", Entry.of("ubi-tosca/Definitions/tosca_ubi.yaml", "Definitions/tosca_ubi.yaml"),
				Entry.of("ubi-tosca/Definitions/etsi_nfv_sol001_vnfd_types.yaml", "Definitions/etsi_nfv_sol001_vnfd_types.yaml"),
				Entry.of("ubi-tosca/Definitions/etsi_nfv_sol001_common_types.yaml", "Definitions/etsi_nfv_sol001_common_types.yaml"),
				Entry.of("ubi-tosca/TOSCA-Metadata/TOSCA.meta", "TOSCA-Metadata/TOSCA.meta"));
		final ToscaNsRegistryHandler srv = createRegistryHandler();
		final InputStream is = new FileInputStream("/tmp/ubi-tosca.csar");
		srv.getNewReaderInstance(is, UUID.randomUUID());
		assertTrue(true);
	}

}
