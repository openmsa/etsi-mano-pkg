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

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.ubiqube.etsi.mano.dao.mano.MonitoringParams;
import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.etsi.mano.dao.mano.pkg.VirtualCpu;
import com.ubiqube.etsi.mano.dao.mano.pkg.VirtualMemory;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VnfcMonitoringParameter;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.Compute;
import com.ubiqube.parser.tosca.scalar.Time;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VnfComputeMapping extends SoftwareImageMapping, VduProfileMapping {

	@Mapping(target = "affinityRule", ignore = true)
	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "cloudInit", source = "bootData.contentOrFileData.content")
	@Mapping(target = "destinationPath", source = "bootData.contentOrFileData.destinationPath")
	@Mapping(target = "diskSize", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "initialNumberOfInstance", ignore = true)
	@Mapping(target = "instantiationLevel", ignore = true)
	@Mapping(target = "networks", ignore = true)
	@Mapping(target = "placementGroup", ignore = true)
	@Mapping(target = "ports", ignore = true)
	@Mapping(target = "scalingAspectDeltas", ignore = true)
	@Mapping(target = "securityGroup", ignore = true)
	@Mapping(target = "softwareImage", source = "artifacts", qualifiedByName = "softwareImage")
	@Mapping(target = "sourcePath", source = "bootData.contentOrFileData.sourcePath")
	@Mapping(target = "state", ignore = true)
	@Mapping(target = "storages", source = "virtualStorageReq")
	@Mapping(target = "toscaId", ignore = true)
	@Mapping(target = "toscaName", source = "internalName")
	@Mapping(target = "virtualCpu", source = "virtualCompute.virtualCpu")
	@Mapping(target = "virtualMemory", source = "virtualCompute.virtualMemory")
	VnfCompute map(Compute c);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "virtualCpuPinningPolicy", source = "virtualCpuPinning.virtualCpuPinningPolicy")
	@Mapping(target = "virtualCpuPinningRule", source = "virtualCpuPinning.virtualCpuPinningRule")
	VirtualCpu mapToVirtualCpu(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualCpu o);

	@Mapping(target = "id", ignore = true)
	VirtualMemory mapToVirtualMemory(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VirtualMemory o);

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "objectType", ignore = true)
	@Mapping(target = "timestamp", ignore = true)
	@Mapping(target = "value", ignore = true)
	@Mapping(target = "vnfComputeName", ignore = true)
	MonitoringParams map(VnfcMonitoringParameter vmp);

	@Override
	default Long toLong(final Time time) {
		if (null == time) {
			return null;
		}
		return time.getValue().longValue() / 1000000000;
	}

	default Set<MonitoringParams> map(final Map<String, VnfcMonitoringParameter> mp) {
		if (null == mp) {
			return Set.of();
		}
		return mp.entrySet().stream()
				.map(x -> {
					final MonitoringParams res = map(x.getValue());
					res.setName(x.getKey());
					return res;
				})
				.collect(Collectors.toSet());
	}
}
