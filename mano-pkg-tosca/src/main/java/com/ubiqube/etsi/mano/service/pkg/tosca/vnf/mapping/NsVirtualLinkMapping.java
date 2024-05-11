/**
 *     Copyright (C) 2019-2024 Ubiqube.
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
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.ubiqube.etsi.mano.dao.mano.NsVlConnectivityType;
import com.ubiqube.etsi.mano.dao.mano.NsVlProfile;
import com.ubiqube.etsi.mano.dao.mano.Qos;
import com.ubiqube.etsi.mano.dao.mano.v2.nfvo.NsVirtualLink;
import com.ubiqube.etsi.mano.dao.mano.vim.IpPool;
import com.ubiqube.etsi.mano.dao.mano.vim.L3Data;
import com.ubiqube.etsi.mano.dao.mano.vim.VlProtocolData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ConnectivityType;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NsIpAllocationPool;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NsL3ProtocolData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NsVirtualLinkProtocolData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NsVirtualLinkQos;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NsVirtualLinkMapping extends ScalarCommonMapping {

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "levelMapping", ignore = true)
	@Mapping(target = "nsVlProfile", source = "vlProfile")
	@Mapping(target = "nsdPackage", ignore = true)
	@Mapping(target = "state", ignore = true)
	@Mapping(target = "stepMapping", ignore = true)
	@Mapping(target = "toscaId", ignore = true)
	@Mapping(target = "toscaName", source = "internalName")
	@Mapping(target = "vlConnectivityType", source = "connectivityType")
	@Mapping(target = "vnffg", ignore = true)
	NsVirtualLink mapToNsVirtualLink(com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.NsVirtualLink o);

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "linkBitrateLeaf", source = "minBitrateRequirements.leaf")
	@Mapping(target = "linkBitrateRoot", source = "minBitrateRequirements.root")
	@Mapping(target = "maxBitrateRequirementsLeaf", source = "maxBitrateRequirements.leaf")
	@Mapping(target = "maxBitrateRequirementsRoot", source = "maxBitrateRequirements.root")
	@Mapping(target = "serviceAvailability", source = "serviceAvailabilityLevel")
	@Mapping(target = "vlProtocolData", source = "virtualLinkProtocolData")
	NsVlProfile mapToNsVlProfile(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NsVlProfile o);

	@Mapping(target = "id", ignore = true)
	Qos mapToQos(NsVirtualLinkQos o);

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ipAllocationPools", source = "l3ProtocolData.ipAllocationPools")
	VlProtocolData mapToVlProtocolData(NsVirtualLinkProtocolData o);

	@Mapping(target = "id", ignore = true)
	IpPool mapToIpPool(NsIpAllocationPool o);

	@Mapping(target = "dhcpEnabled", ignore = true)
	@Mapping(target = "gatewayIp", ignore = true)
	@Mapping(target = "ipv6AddressMode", ignore = true)
	@Mapping(target = "l3Name", source = "name")
	L3Data mapToL3Data(NsL3ProtocolData o);

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	NsVlConnectivityType mapToNsVlConnectivityType(ConnectivityType o);

}
