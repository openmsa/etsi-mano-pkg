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

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.CpProtocolData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualNetworkInterfaceRequirements;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfExtCp;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class VnfVlMappingTest {
	private final VnfVlMapping vnfVlMapping = Mappers.getMapper(VnfVlMapping.class);
	private final PodamFactoryImpl podam;

	public VnfVlMappingTest() {
		podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
	}

	@Test
	void test() {
		assertNull(vnfVlMapping.mapToVlProtocolData((CpProtocolData) null));
		CpProtocolData o = podam.manufacturePojo(CpProtocolData.class);
		assertNotNull(vnfVlMapping.mapToVlProtocolData(o));
	}

	@Test
	void testVnfVirtualLink() {
		assertNull(vnfVlMapping.map((com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfVirtualLink) null));
		com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfVirtualLink o = podam
				.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfVirtualLink.class);
		assertNotNull(vnfVlMapping.map(o));
	}

	@Test
	void testVlProfile() {
		assertNull(vnfVlMapping.mapToVlProfileEntity((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VlProfile) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VlProfile o = podam
				.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VlProfile.class);
		assertNotNull(vnfVlMapping.mapToVlProfileEntity(o));
	}

	@Test
	void testVirtualLinkProtocolData() {
		assertNull(vnfVlMapping.mapToVlProtocolData((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualLinkProtocolData) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualLinkProtocolData o = podam
				.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualLinkProtocolData.class);
		assertNotNull(vnfVlMapping.mapToVlProtocolData(o));
	}

	@Test
	void testIpAllocationPool() {
		assertNull(vnfVlMapping.mapToIpPool((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.IpAllocationPool) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.IpAllocationPool o = podam
				.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.IpAllocationPool.class);
		assertNotNull(vnfVlMapping.mapToIpPool(o));
	}

	@Test
	void testQos() {
		assertNull(vnfVlMapping.mapToQos((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.Qos) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.Qos o = podam
				.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.Qos.class);
		assertNotNull(vnfVlMapping.mapToQos(o));
	}

	@Test
	void testL3ProtocolData() {
		assertNull(vnfVlMapping.map((com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.L3ProtocolData) null));
		com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.L3ProtocolData o = podam
				.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.L3ProtocolData.class);
		assertNotNull(vnfVlMapping.map(o));
	}

	@Test
	void testVduCp() {
		assertNull(vnfVlMapping.mapToVnfLinkPort((com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VduCp) null));
		com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VduCp o = podam
				.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VduCp.class);
		assertNotNull(vnfVlMapping.mapToVnfLinkPort(o));
	}

	@Test
	void testVnfExtCp() {
		assertNull(vnfVlMapping.mapToVnfExtCp((VnfExtCp) null));
		VnfExtCp o = podam.manufacturePojo(VnfExtCp.class);
		assertNotNull(vnfVlMapping.mapToVnfExtCp(o));
	}

	@Test
	void testVirtualNetworkInterfaceRequirements() {
		assertNull(vnfVlMapping.mapToVirtualNicReq((VirtualNetworkInterfaceRequirements) null));
		VirtualNetworkInterfaceRequirements o = podam.manufacturePojo(VirtualNetworkInterfaceRequirements.class);
		assertNotNull(vnfVlMapping.mapToVirtualNicReq(o));
	}

}
