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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.AdditionalArtifact;
import com.ubiqube.etsi.mano.dao.mano.MonitoringParams;
import com.ubiqube.etsi.mano.dao.mano.ScalingAspect;
import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.etsi.mano.dao.mano.VnfExtCp;
import com.ubiqube.etsi.mano.dao.mano.VnfIndicator;
import com.ubiqube.etsi.mano.dao.mano.VnfLinkPort;
import com.ubiqube.etsi.mano.dao.mano.VnfVl;
import com.ubiqube.etsi.mano.dao.mano.pkg.OsContainer;
import com.ubiqube.etsi.mano.dao.mano.pkg.OsContainerDeployableUnit;
import com.ubiqube.etsi.mano.dao.mano.pkg.VirtualCp;
import com.ubiqube.etsi.mano.dao.mano.repo.Repository;
import com.ubiqube.etsi.mano.dao.mano.vim.Checksum;
import com.ubiqube.etsi.mano.dao.mano.vim.ContainerFormatType;
import com.ubiqube.etsi.mano.dao.mano.vim.DiskFormatType;
import com.ubiqube.etsi.mano.dao.mano.vim.IpPool;
import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.etsi.mano.dao.mano.vim.VnfStorage;
import com.ubiqube.etsi.mano.dao.mano.vnfm.McIops;
import com.ubiqube.etsi.mano.service.cond.ConditionService;
import com.ubiqube.etsi.mano.service.pkg.bean.AffinityRuleAdapater;
import com.ubiqube.etsi.mano.service.pkg.bean.ProviderData;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupAdapter;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.ToscaVnfPackageReader;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ArtefactInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.PkgMapper;
import com.ubiqube.etsi.mano.test.ZipUtil;
import com.ubiqube.etsi.mano.test.ZipUtil.Entry;
import com.ubiqube.parser.test.ArtifactDownloader;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.InstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInitialDelta;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduScalingAspectDeltas;

class ToscaPackageProviderTest {
	private final ConditionService cs;
	ToscaVnfPackageReader tpp;

	public ToscaPackageProviderTest() throws IOException {
		ArtifactDownloader.prepareArtifact("421");
		ZipUtil.makeToscaZip("/tmp/ubi-tosca.csar", Entry.of("ubi-tosca/Definitions/tosca_ubi.yaml", "Definitions/tosca_ubi.yaml"),
				Entry.of("ubi-tosca/Definitions/etsi_nfv_sol001_vnfd_types.yaml", "Definitions/etsi_nfv_sol001_vnfd_types.yaml"),
				Entry.of("ubi-tosca/Definitions/etsi_nfv_sol001_common_types.yaml", "Definitions/etsi_nfv_sol001_common_types.yaml"),
				Entry.of("ubi-tosca/TOSCA-Metadata/TOSCA.meta", "TOSCA-Metadata/TOSCA.meta"));
		this.cs = new ConditionService();
		final ArtefactInformationsMapping artefactInformationsMapping = Mappers.getMapper(ArtefactInformationsMapping.class);
		final PkgMapper pm = TestFactory.createPkgMapper();
		try (final InputStream data = Files.newInputStream(Path.of("/tmp/ubi-tosca.csar"))) {
			tpp = new ToscaVnfPackageReader(data, null, UUID.randomUUID(), cs, pm, artefactInformationsMapping);
		}
	}

	@Test
	void testSoftwareImage01() throws Exception {
		final Set<AdditionalArtifact> aa = tpp.getAdditionalArtefacts(new HashMap<>());
		System.out.println("" + aa);
		assertNotNull(aa);
	}

	@Test
	void testComputeNode01() throws Exception {
		final Set<VnfCompute> vnfCn = tpp.getVnfComputeNodes(new HashMap<>());
		System.out.println("" + vnfCn);
		assertEquals(2, vnfCn.size());
		VnfCompute cn = vnfCn.iterator().next();
		checkmonitoringConfig(cn.getMonitoringParameters());
		cn = vnfCn.iterator().next();
		checkmonitoringConfig(cn.getMonitoringParameters());
		assertNotNull(cn);
	}

	private static void checkmonitoringConfig(final Set<MonitoringParams> set) {
		if ((set == null) || set.isEmpty()) {
			return;
		}

		final MonitoringParams mp = set.iterator().next();
		if (mp == null) {
			return;
		}
		assertEquals(60L, mp.getCollectionPeriod());
		assertEquals("metric name", mp.getName());
	}

	@Test
	void testStorage01() throws Exception {
		final Set<VnfStorage> storages = tpp.getVnfStorages(new HashMap<>());
		System.out.println("" + storages);
		assertEquals(1, storages.size());
		for (final VnfStorage vnfStorage : storages) {
			assertEquals(6000000000L, vnfStorage.getSize());
			assertNotNull(vnfStorage.getToscaName());
			if ("OBJECT".equals(vnfStorage.getType())) {
				//
			} else if ("BLOCK".equals(vnfStorage.getType())) {
				final SoftwareImage swImage = vnfStorage.getSoftwareImage();
				final Checksum checksum = swImage.getChecksum();
				assertNotNull(checksum);
				assertEquals("SHA-256", checksum.getAlgorithm());
				assertEquals("01ba4719c80b6fe911b091a7c05124b64eeece964e09c058ef8f9805daca546b", checksum.getHash());

				assertEquals("BARE", Optional.ofNullable(swImage.getContainerFormat()).map(ContainerFormatType::toString).orElseThrow());
				assertEquals("QCOW2", Optional.ofNullable(swImage.getDiskFormat()).map(DiskFormatType::toString).orElseThrow());
				assertEquals(5000000000L, swImage.getMinDisk());
				assertEquals(512000000L, swImage.getMinRam());
				assertEquals("cirros", swImage.getName());
			}
		}
	}

	@Test
	void testVirtualLink01() throws Exception {
		final Set<VnfVl> vnfVl = tpp.getVnfVirtualLinks(new HashMap<>());
		assertEquals(3, vnfVl.size());
		final VnfVl vl = vnfVl.iterator().next();
		// assertEquals("middleVl01", vl.getToscaName());
		final Set<IpPool> ipPool = vl.getVlProfileEntity().getVirtualLinkProtocolData().iterator().next().getIpAllocationPools();
		assertEquals(1, ipPool.size());
	}

	@Test
	void testVirtualCp01() throws Exception {
		final Set<VnfLinkPort> vnfCp = tpp.getVnfVduCp(new HashMap<>());
		assertEquals(4, vnfCp.size());
		final VnfLinkPort cp = vnfCp.iterator().next();
		// assertEquals("cpLc02", cp.getToscaName());
		assertNotNull(cp);
	}

	@Test
	void testVnfExtCp() throws Exception {
		final Set<VnfExtCp> extCp = tpp.getVnfExtCp(new HashMap<>());
		assertEquals(1, extCp.size());
	}

	@Test
	void testScalingAspect() throws Exception {
		final Set<ScalingAspect> list = tpp.getScalingAspects(new HashMap<>());
		System.out.println("" + list);
		assertNotNull(list);
	}

	@Test
	void testVduScalingAspectDeltas() throws Exception {
		final List<VduScalingAspectDeltas> list = tpp.getVduScalingAspectDeltas(new HashMap<>());
		System.out.println("" + list);
		assertNotNull(list);
	}

	@Test
	void testGetProviderPadata() throws Exception {
		final ProviderData res = tpp.getProviderPadata();
		assertNotNull(res);
		final Set<MonitoringParams> monParams = res.getMonitoringParameters();
		assertNotNull(monParams);
		assertEquals(1, monParams.size());
		final MonitoringParams data = monParams.iterator().next();
		assertEquals("mon01", data.getName());
	}

	@Test
	void testAffinityRule() throws Exception {
		final Set<AffinityRuleAdapater> res = tpp.getAffinityRules(Map.of());
		assertNotNull(res);
		assertEquals(2, res.size());
	}

	@Test
	void testSecurityGroup() throws Exception {
		final Set<SecurityGroupAdapter> res = tpp.getSecurityGroups(Map.of());
		assertNotNull(res);
		assertEquals(1, res.size());
	}

	@Test
	void testVnfd() {
		final List<String> files = tpp.getVnfdFiles(true);
		assertEquals(4, files.size());
	}

	@Test
	void testVnfIndicator() {
		final Set<VnfIndicator> res = tpp.getVnfIndicator(Map.of());
		assertNotNull(res);
		assertEquals(2, res.size());
	}

	@Test
	void testgetInstatiationLevels() {
		final List<InstantiationLevels> files = tpp.getInstatiationLevels(Map.of());
		assertEquals(1, files.size());
	}

	@Test
	void testVduInstantiationLevels() {
		final List<VduInstantiationLevels> files = tpp.getVduInstantiationLevels(Map.of());
		assertEquals(1, files.size());
	}

	@Test
	void testgetVduInitialDelta() {
		final List<VduInitialDelta> files = tpp.getVduInitialDelta(Map.of());
		assertEquals(2, files.size());
	}

	@Test
	void testgetOsContainer() {
		final Set<OsContainer> files = tpp.getOsContainer(Map.of());
		assertEquals(1, files.size());
	}

	@Test
	void testgetOsContainerDeployableUnit() {
		final Set<OsContainerDeployableUnit> files = tpp.getOsContainerDeployableUnit(Map.of());
		assertEquals(1, files.size());
	}

	@Test
	void testgetVirtualCp() {
		final Set<VirtualCp> files = tpp.getVirtualCp(Map.of());
		assertEquals(1, files.size());
	}

	@Test
	void testgetMciops() {
		final Set<McIops> files = tpp.getMciops(Map.of());
		assertEquals(1, files.size());
	}

	@Test
	void testgetRepositories() {
		final Set<Repository> files = tpp.getRepositories();
		assertEquals(2, files.size());
	}

	@Test
	void testManifestContent() {
		final String files = tpp.getManifestContent();
		assertNull(files);
	}

	@Test
	void testFileContent() {
		final byte[] files = tpp.getFileContent("TOSCA-Metadata/TOSCA.meta");
		assertNotNull(files);
	}

	@Test
	void testgetFileInputStream() {
		final InputStream files = tpp.getFileInputStream("TOSCA-Metadata/TOSCA.meta");
		assertNotNull(files);
	}
}
