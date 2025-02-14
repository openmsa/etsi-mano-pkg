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
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.HelmChart;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ChecksumData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MaxNumberOfImpactedInstances;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VduProfile;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.Compute;
import com.ubiqube.parser.tosca.scalar.Frequency;
import com.ubiqube.parser.tosca.scalar.Size;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class VnfComputeMappingTest {
	private final VnfComputeMapping mapper = Mappers.getMapper(VnfComputeMapping.class);
	private final PodamFactoryImpl podam;

	public VnfComputeMappingTest() {
		podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
	}

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final Compute obj = podam.manufacturePojo(Compute.class);
		obj.setArtifacts(Map.of("img", new SwImage()));
		obj.getVirtualCompute().getVirtualCpu().setVirtualCpuClock(new Frequency("1ghz"));
		obj.getVduProfile().getNfviMaintenanceInfo().setSupportedMigrationType(List.of("NO_MIGRATION"));
		obj.getVirtualCompute().getVirtualMemory().setVirtualMemSize(new Size("1gib"));
		final VnfCompute r = mapper.map(obj);
		assertNotNull(r);
		assertNull(mapper.map((Compute) null));
	}

	@Test
	void testHelmChart() {
		final HelmChart obj = podam.manufacturePojo(HelmChart.class);

		SoftwareImage r = mapper.map(obj);
		assertNotNull(r);
		assertNull(mapper.map((HelmChart) null));
	}

	@Test
	void testMaxNumberOfImpactedInstances() {
		assertNull(mapper.mapToMaxNumberOfImpactedInstance((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MaxNumberOfImpactedInstances) null));
		MaxNumberOfImpactedInstances o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MaxNumberOfImpactedInstances.class);
		o.setMaxNumberOfImpactedInstances(null);
		assertNotNull(mapper.mapToMaxNumberOfImpactedInstance(o));
	}

	@Test
	void testmapToMinNumberOfPreservedInstance() {
		assertNull(mapper.mapToMinNumberOfPreservedInstance((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MinNumberOfPreservedInstances) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MinNumberOfPreservedInstances o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MinNumberOfPreservedInstances.class);
		o.setMinNumberOfPreservedInstances(null);
		assertNotNull(mapper.mapToMinNumberOfPreservedInstance(o));
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
	void testSwImage() {
		assertNull(mapper.map((com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage) null));
		com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage.class);
		o.setContainerFormat(null);
		o.setDiskFormat(null);
		o.setSize(new Size("1gib"));
		o.setMinDisk(new Size("1gib"));
		o.setMinRam(new Size("1gib"));
		assertNotNull(mapper.map(o));
	}

	@Test
	void testSwImage2() {
		assertNull(mapper.map((com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage) null));
		com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage.class);
		o.setContainerFormat("AMI");
		o.setDiskFormat("AMI");
		o.setSize(new Size("1gib"));
		o.setMinDisk(new Size("1gib"));
		o.setMinRam(new Size("1gib"));
		assertNotNull(mapper.map(o));
	}

	@Test
	void testmapToChecksum() {
		assertNull(mapper.mapToChecksum((ChecksumData) null));
		ChecksumData o = podam.manufacturePojo(ChecksumData.class);
		assertNotNull(mapper.mapToChecksum(o));
	}

	@Test
	void testVirtualCpu() {
		assertNull(mapper.mapToVirtualCpu((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualCpu) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualCpu o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualCpu.class);
		o.setVirtualCpuClock(null);
		assertNotNull(mapper.mapToVirtualCpu(o));
	}

	@Test
	void testmapToVirtualMemory() {
		assertNull(mapper.mapToVirtualMemory((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualMemory) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualMemory o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualMemory.class);
		o.setVirtualMemSize(null);
		assertNotNull(mapper.mapToVirtualMemory(o));
	}

	@Test
	void testVnfcMonitoringParameter() {
		assertNull(mapper.map((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VnfcMonitoringParameter) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VnfcMonitoringParameter o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VnfcMonitoringParameter.class);
		assertNotNull(mapper.map(o));
	}

}
