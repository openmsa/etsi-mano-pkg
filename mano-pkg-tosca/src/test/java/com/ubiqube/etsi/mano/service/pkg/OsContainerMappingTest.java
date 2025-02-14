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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.vim.Checksum;
import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.OsContainerMapping;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.HelmChart;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ChecksumData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ExtendedResourceData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MaxNumberOfImpactedInstances;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VduProfile;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainer;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainerDeployableUnit;
import com.ubiqube.parser.tosca.scalar.Size;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class OsContainerMappingTest {

	private final PodamFactoryImpl podam;
	private final OsContainerMapping mapper = Mappers.getMapper(OsContainerMapping.class);

	public OsContainerMappingTest() {
		podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
	}

	@Test
	void testName() {
		final OsContainer obj = podam.manufacturePojo(OsContainer.class);
		final Size s = new Size(5000L);
		obj.setMemoryResourceLimit(s);
		obj.setRequestedMemoryResources(s);
		obj.setEphemeralStorageResourceLimit(s);
		obj.setRequestedEphemeralStorageResources(s);
		obj.setArtifacts(Map.of());
		obj.getHugePagesResources().forEach(x -> {
			x.setHugepageSize(new Size(5000L));
			x.setRequestedSize(new Size(5000L));
		});
		final com.ubiqube.etsi.mano.dao.mano.pkg.OsContainer r = mapper.mapToOsContainer(obj);
		assertNotNull(r);
	}

	@Test
	void testOsContainerDeployableUnit() {
		final OsContainerDeployableUnit obj = podam.manufacturePojo(OsContainerDeployableUnit.class);
		obj.setNfviConstraints(Map.of("NODE_POOL", "NODE_POOL"));
		obj.setMcioConstraintParams(List.of("NODE_POOL"));
		obj.getVduProfile().getNfviMaintenanceInfo().setSupportedMigrationType(List.of());
		final com.ubiqube.etsi.mano.dao.mano.pkg.OsContainerDeployableUnit r = mapper.mapToOsContainerDeployableUnit(obj);
		assertNotNull(r);
	}

	@Test
	void testSwImage() {
		assertNull(mapper.map((SwImage) null));
		final SwImage obj = podam.manufacturePojo(SwImage.class);
		obj.setDiskFormat("AMI");
		obj.setSize(new Size(5000L));
		obj.setMinDisk(new Size(5000L));
		obj.setMinRam(new Size(5000L));
		SoftwareImage r = mapper.map(obj);
		assertNotNull(r);
		obj.setDiskFormat(null);
		mapper.map(obj);
	}

	@Test
	void testChecksumData() {
		assertNull(mapper.mapToChecksum(null));
		ChecksumData o = podam.manufacturePojo(ChecksumData.class);
		Checksum r = mapper.mapToChecksum(o);
		assertNotNull(r);
	}

	@Test
	void testHelmChart() {
		assertNull(mapper.map((HelmChart) null));
		final HelmChart obj = podam.manufacturePojo(HelmChart.class);
		SoftwareImage r = mapper.map(obj);
		assertNotNull(r);
	}

	@Test
	void testmapToVduProfile() {
		assertNull(mapper.mapToVduProfile((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VduProfile) null));
		VduProfile o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VduProfile.class);
		o.setMaxNumberOfInstances(null);
		o.setMinNumberOfInstances(null);
		o.getNfviMaintenanceInfo().setSupportedMigrationType(null);
		assertNotNull(mapper.mapToVduProfile(o));
	}

	@Test
	void testMaxNumberOfImpactedInstances() {
		assertNull(mapper.mapToMaxNumberOfImpactedInstance((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MaxNumberOfImpactedInstances) null));
		MaxNumberOfImpactedInstances o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MaxNumberOfImpactedInstances.class);
		o.setMaxNumberOfImpactedInstances(null);
		assertNotNull(mapper.mapToMaxNumberOfImpactedInstance(o));
	}

	@Test
	void testNfviMaintenanceInfo() {
		assertNull(mapper.mapToNfviMaintenanceInfo((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NfviMaintenanceInfo) null));
		final com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NfviMaintenanceInfo obj = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NfviMaintenanceInfo.class);
		obj.setIsImpactMitigationRequested(null);
		obj.setSupportedMigrationType(List.of("OFFLINE_MIGRATION"));
		assertNotNull(mapper.mapToNfviMaintenanceInfo(obj));
	}

	@Test
	void testmapToMinNumberOfPreservedInstance() {
		assertNull(mapper.mapToMinNumberOfPreservedInstance((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MinNumberOfPreservedInstances) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MinNumberOfPreservedInstances o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MinNumberOfPreservedInstances.class);
		o.setMinNumberOfPreservedInstances(null);
		assertNotNull(mapper.mapToMinNumberOfPreservedInstance(o));
	}

	@Test
	void testOsContainer() {
		assertNull(mapper.mapToOsContainer((OsContainer) null));
		OsContainer o = podam.manufacturePojo(OsContainer.class);
		o.setCpuResourceLimit(null);
		o.setRequestedCpuResources(null);
		o.setArtifacts(Map.of());
		o.setMemoryResourceLimit(new Size(5000L));
		o.setRequestedMemoryResources(new Size(5000L));
		o.setEphemeralStorageResourceLimit(new Size(5000L));
		o.setRequestedEphemeralStorageResources(new Size(5000L));
		o.getHugePagesResources().forEach(x -> {
			x.setHugepageSize(new Size(5000L));
			x.setRequestedSize(new Size(5000L));
		});
		assertNotNull(mapper.mapToOsContainer(o));
	}

	@Test
	void testExtendedResourceData() {
		assertNull(mapper.mapToExtendedResourceData(null));
		ExtendedResourceData o = podam.manufacturePojo(ExtendedResourceData.class);
		o.setAmount(null);
		assertNotNull(mapper.mapToExtendedResourceData(o));
	}

	@Test
	void testHugepages() {
		assertNull(mapper.mapToHugepages(null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.Hugepages o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.Hugepages.class);
		o.setHugepageSize(new Size(5000L));
		o.setRequestedSize(new Size(5000L));
		assertNotNull(mapper.mapToHugepages(o));
	}

	@Test
	void testOsContainerDeployableUnit2() {
		assertNull(mapper.mapToOsContainerDeployableUnit(null));
		OsContainerDeployableUnit o = podam.manufacturePojo(OsContainerDeployableUnit.class);
		o.setContainerReq(null);
		o.setVirtualStorageReq(null);
		o.setNfviConstraints(Map.of("NODE_POOL", "NODE_POOL"));
		o.setMcioConstraintParams(List.of("NODE_POOL"));
		o.getVduProfile().getNfviMaintenanceInfo().setSupportedMigrationType(List.of());
		assertNotNull(mapper.mapToOsContainerDeployableUnit(o));
	}

	@Test
	void testRequestedAdditionalCapability() {
		assertNull(mapper.mapToRequestedAdditionalCapability(null));
		final com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.RequestedAdditionalCapability o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.RequestedAdditionalCapability.class);
		o.setSupportMandatory(null);
		assertNotNull(mapper.mapToRequestedAdditionalCapability(o));
	}

	@Test
	void testHelmChart2() {
		assertNull(mapper.map((HelmChart) null));
	}
}
