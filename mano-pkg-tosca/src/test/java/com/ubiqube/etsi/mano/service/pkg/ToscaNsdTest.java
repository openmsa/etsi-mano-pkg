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
package com.ubiqube.etsi.mano.service.pkg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.NsSap;
import com.ubiqube.etsi.mano.dao.mano.NsVnfIndicator;
import com.ubiqube.etsi.mano.dao.mano.dto.NsNsd;
import com.ubiqube.etsi.mano.dao.mano.dto.NsVnf;
import com.ubiqube.etsi.mano.dao.mano.nsd.VnffgDescriptor;
import com.ubiqube.etsi.mano.dao.mano.v2.nfvo.NsVirtualLink;
import com.ubiqube.etsi.mano.service.pkg.bean.NsInformations;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupAdapter;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.NsScaling;
import com.ubiqube.etsi.mano.service.pkg.tosca.ns.ToscaNsPackageProvider;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ArtefactInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsSapMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsVirtualLinkMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsVnfIndicatorMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.PkgMapper;
import com.ubiqube.etsi.mano.test.ZipUtil;
import com.ubiqube.etsi.mano.test.ZipUtil.Entry;
import com.ubiqube.parser.test.ArtifactDownloader;

class ToscaNsdTest {
	private final ToscaNsPackageProvider tpp;

	public ToscaNsdTest() throws IOException {
		ArtifactDownloader.prepareArtifact("421");
		ZipUtil.makeToscaZip("/tmp/ubi-nsd-tosca.csar", Entry.of("ubi-nsd-tosca/Definitions/nsd_ubi.yaml", "Definitions/nsd_ubi.yaml"),
				Entry.of("ubi-nsd-tosca/Definitions/etsi_nfv_sol001_nsd_types.yaml", "Definitions/etsi_nfv_sol001_nsd_types.yaml"),
				Entry.of("ubi-nsd-tosca/Definitions/etsi_nfv_sol001_vnfd_types.yaml", "Definitions/etsi_nfv_sol001_vnfd_types.yaml"),
				Entry.of("ubi-nsd-tosca/Definitions/etsi_nfv_sol001_pnfd_types.yaml", "Definitions/etsi_nfv_sol001_pnfd_types.yaml"),
				Entry.of("ubi-nsd-tosca/Definitions/etsi_nfv_sol001_common_types.yaml", "Definitions/etsi_nfv_sol001_common_types.yaml"),
				Entry.of("ubi-nsd-tosca/TOSCA-Metadata/TOSCA.meta", "TOSCA-Metadata/TOSCA.meta"));
		final InputStream data = Files.newInputStream(Path.of("/tmp/ubi-nsd-tosca.csar"));
		final PkgMapper mapper = TestFactory.createPkgMapper();
		final NsInformationsMapping nsInformationsMapping = Mappers.getMapper(NsInformationsMapping.class);
		final NsVnfIndicatorMapping nsVnfIndicatorMapping = Mappers.getMapper(NsVnfIndicatorMapping.class);
		final NsSapMapping nsSapMapping = Mappers.getMapper(NsSapMapping.class);
		final NsVirtualLinkMapping nsVirtualLinkMapping = Mappers.getMapper(NsVirtualLinkMapping.class);
		final UUID id = UUID.randomUUID();
		final ArtefactInformationsMapping artefactInformationsMapping = Mappers.getMapper(ArtefactInformationsMapping.class);
		tpp = new ToscaNsPackageProvider(data, null, id, mapper, nsInformationsMapping, nsVnfIndicatorMapping, nsSapMapping, nsVirtualLinkMapping, artefactInformationsMapping);
	}

	@Test
	void testNsInformations() throws Exception {
		final NsInformations list = tpp.getNsInformations(new HashMap<>());
		assertEquals("flavor01", list.getFlavorId());
		assertEquals("demo", list.getInstantiationLevel());
		assertEquals(1, list.getMinNumberOfInstance());
		assertEquals(1, list.getMaxNumberOfInstance());
		assertEquals("ovi@ubiqube.com", list.getNsdDesigner());
		assertEquals("65f6fbed-654b-4d68-b730-5d8d72a8b865", list.getNsdInvariantId());
		assertEquals("ns01", list.getNsdName());
		assertEquals("0.0.1", list.getNsdVersion());
	}

	@Test
	void testNsVirtualLink() throws Exception {
		final Set<NsVirtualLink> list = tpp.getNsVirtualLink(new HashMap<>());
		assertEquals(1, list.size());
	}

	@Test
	void testNsSap() throws Exception {
		final Set<NsSap> list = tpp.getNsSap(new HashMap<>());
		assertEquals(1, list.size());
	}

	@Test
	void testSecurityGroupAdapter() throws Exception {
		final Set<SecurityGroupAdapter> list = tpp.getSecurityGroups(new HashMap<>());
		assertEquals(1, list.size());
	}

	@Test
	void testUbiqube01() throws Exception {
		final Set<NsVnf> list = tpp.getVnfd(new HashMap<>());
		assertEquals(1, list.size());
	}

	@Test
	void testUbiqube02() throws Exception {
		final Set<NsNsd> list = tpp.getNestedNsd(new HashMap<>());
		assertEquals(0, list.size());
	}

	@Test
	void testVnffg() {
		final Set<VnffgDescriptor> list = tpp.getVnffg(new HashMap<>());
		assertNotNull(list);
		assertEquals(1, list.size());
	}

	@Test
	void testAutoHealing() {
		final boolean list = tpp.isAutoHealEnabled();
		assertTrue(list);
	}

	@Test
	void testVnffgNsScaling() {
		final NsScaling list = tpp.getNsScaling(new HashMap<>());
		assertNotNull(list);
	}

	@Test
	void testNsIndictor() {
		final Set<NsVnfIndicator> list = tpp.getNsVnfIndicator(new HashMap<>());
		assertNotNull(list);
	}

}
