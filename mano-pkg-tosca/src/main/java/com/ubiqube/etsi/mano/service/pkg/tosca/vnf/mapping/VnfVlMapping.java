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

import com.ubiqube.etsi.mano.dao.mano.Qos;
import com.ubiqube.etsi.mano.dao.mano.VirtualNicReq;
import com.ubiqube.etsi.mano.dao.mano.VlProfileEntity;
import com.ubiqube.etsi.mano.dao.mano.VnfLinkPort;
import com.ubiqube.etsi.mano.dao.mano.VnfVl;
import com.ubiqube.etsi.mano.dao.mano.vim.IpPool;
import com.ubiqube.etsi.mano.dao.mano.vim.L3Data;
import com.ubiqube.etsi.mano.dao.mano.vim.VlProtocolData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.IpAllocationPool;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.L3ProtocolData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualLinkProtocolData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualNetworkInterfaceRequirements;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VlProfile;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VduCp;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfExtCp;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfVirtualLink;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VnfVlMapping extends ScalarCommonMapping, ConnectivityCommonMapper {
	@Mapping(target = "affinityRules", ignore = true)
	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "initialBrLeaf", ignore = true)
	@Mapping(target = "initialBrRoot", ignore = true)
	@Mapping(target = "placementGroup", ignore = true)
	@Mapping(target = "state", ignore = true)
	@Mapping(target = "toscaId", ignore = true)
	@Mapping(target = "toscaName", source = "internalName")
	@Mapping(target = "vlProfileEntity", source = "vlProfile")
	VnfVl map(VnfVirtualLink o);

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "linkBitrateLeaf", source = "minBitrateRequirements.leaf")
	@Mapping(target = "linkBitrateRoot", source = "minBitrateRequirements.root")
	@Mapping(target = "maxBitrateRequirementsLeaf", source = "maxBitrateRequirements.leaf")
	@Mapping(target = "maxBitrateRequirementsRoot", source = "maxBitrateRequirements.root")
	VlProfileEntity mapToVlProfileEntity(VlProfile o);

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ipAllocationPools", source = "l3ProtocolData.ipAllocationPools")
	VlProtocolData mapToVlProtocolData(VirtualLinkProtocolData o);

	@Mapping(target = "id", ignore = true)
	IpPool mapToIpPool(IpAllocationPool o);

	@Mapping(target = "id", ignore = true)
	Qos mapToQos(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.Qos o);

	@Mapping(target = "l3Name", source = "name")
	L3Data map(L3ProtocolData l3);

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "interfaceOrder", source = "order")
	@Mapping(target = "state", ignore = true)
	@Mapping(target = "toscaId", ignore = true)
	@Mapping(target = "toscaName", source = "internalName")
	@Mapping(target = "virtualBinding", source = "virtualBindingReq")
	@Mapping(target = "virtualLink", source = "virtualLinkReq")
	VnfLinkPort mapToVnfLinkPort(final VduCp x);

	@Mapping(target = "computeNode", ignore = true)
	@Mapping(target = "externalVirtualLink", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "internalVirtualLink", ignore = true)
	@Mapping(target = "securityGroup", ignore = true)
	@Mapping(target = "state", ignore = true)
	@Mapping(target = "toscaId", ignore = true)
	@Mapping(target = "toscaName", ignore = true)
	com.ubiqube.etsi.mano.dao.mano.VnfExtCp mapToVnfExtCp(final VnfExtCp x);

	@Mapping(target = "id", ignore = true)
	VirtualNicReq mapToVirtualNicReq(VirtualNetworkInterfaceRequirements o);
}
