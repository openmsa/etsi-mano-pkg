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

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ubiqube.etsi.mano.dao.mano.MonitoringParams;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VnfMonitoringParameter;

@Mapper
public interface VnfMonitoringCommonMapping extends ScalarCommonMapping {

	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "objectType", constant = "VNF")
	@Mapping(target = "timestamp", ignore = true)
	@Mapping(target = "value", ignore = true)
	@Mapping(target = "vnfComputeName", ignore = true)
	MonitoringParams mapToMonitoringParams(VnfMonitoringParameter mp, String toscaName);

}
