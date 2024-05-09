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

import com.ubiqube.etsi.mano.service.pkg.bean.NsInformations;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.NS;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NsInformationsMapping {
	@Mapping(target = "flavorId", source = "flavourId")
	@Mapping(target = "instantiationLevel", source = "nsProfile.nsInstantiationLevel")
	@Mapping(target = "maxNumberOfInstance", source = "nsProfile.maxNumberOfInstances")
	@Mapping(target = "minNumberOfInstance", source = "nsProfile.minNumberOfInstances")
	@Mapping(target = "nsdDesigner", source = "designer")
	@Mapping(target = "nsdId", source = "descriptorId")
	@Mapping(target = "nsdInvariantId", source = "invariantId")
	@Mapping(target = "nsdName", source = "name")
	@Mapping(target = "nsdVersion", source = "version")
	NsInformations mapToNsInformations(NS o);
}
