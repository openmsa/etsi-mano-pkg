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
import com.ubiqube.etsi.mano.service.pkg.bean.ProviderData;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VnfMonitoringParameter;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VNF;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VnfMapping extends ScalarCommonMapping, ScaleCommonMapping {

	@Mapping(target = "attributes", ignore = true)
	@Mapping(target = "flavorId", source = "flavourId")
	@Mapping(target = "vnfProductName", source = "productName")
	@Mapping(target = "vnfProvider", source = "provider")
	@Mapping(target = "vnfSoftwareVersion", source = "softwareVersion")
	@Mapping(target = "vnfVersion", ignore = true)
	@Mapping(target = "vnfdVersion", source = "descriptorVersion")
	ProviderData map(VNF vnf);

	default Set<com.ubiqube.etsi.mano.service.pkg.bean.ScaleInfo> mapSi(final Map<String, com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScaleInfo> o) {
		if (null == o) {
			return Set.of();
		}
		return o.entrySet().stream().map(x -> {
			final com.ubiqube.etsi.mano.service.pkg.bean.ScaleInfo r = mapToScaleInfo(x.getValue());
			r.setName(x.getKey());
			return r;
		}).collect(Collectors.toSet());
	}

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "objectType", constant = "VNF")
	@Mapping(target = "timestamp", ignore = true)
	@Mapping(target = "value", ignore = true)
	@Mapping(target = "vnfComputeName", ignore = true)
	@Mapping(target = "name", source = "toscaName")
	MonitoringParams map(VnfMonitoringParameter mp, String toscaName);

	default Set<MonitoringParams> map(final Map<String, VnfMonitoringParameter> vmp) {
		if (null == vmp) {
			return Set.of();
		}
		return vmp.entrySet().stream()
				.map(x -> map(x.getValue(), x.getKey()))
				.collect(Collectors.toSet());
	}

}
