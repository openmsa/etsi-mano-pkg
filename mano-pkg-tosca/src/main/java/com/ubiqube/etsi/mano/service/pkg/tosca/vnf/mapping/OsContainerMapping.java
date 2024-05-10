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

import com.ubiqube.etsi.mano.dao.mano.EntryStringString;
import com.ubiqube.etsi.mano.dao.mano.pkg.ExtendedResourceData;
import com.ubiqube.etsi.mano.dao.mano.pkg.Hugepages;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.RequestedAdditionalCapability;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainer;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainerDeployableUnit;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OsContainerMapping extends VduProfileMapping, ScalarCommonMapping {
	@Mapping(target = "id", ignore = true)
	com.ubiqube.etsi.mano.dao.mano.pkg.OsContainer mapToOsContainer(final OsContainer x);

	@Mapping(target = "id", ignore = true)
	ExtendedResourceData mapToExtendedResourceData(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ExtendedResourceData o);

	@Mapping(target = "id", ignore = true)
	Hugepages mapToHugepages(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.Hugepages o);

	default Set<EntryStringString> mapToEntryStringString(final Map<String, String> value) {
		if (null == value) {
			return Set.of();
		}
		return value.entrySet().stream()
				.map(x -> {
					final EntryStringString e = new EntryStringString();
					e.setKey(x.getKey());
					e.setWalue(x.getValue());
					return e;
				}).collect(Collectors.toSet());
	}

	@Mapping(target = "id", ignore = true)
	com.ubiqube.etsi.mano.dao.mano.pkg.OsContainerDeployableUnit mapToOsContainerDeployableUnit(final OsContainerDeployableUnit x);

	default Set<String> mapToString(final Map<String, String> value) {
		if (null == value) {
			return Set.of();
		}
		return value.values().stream().collect(Collectors.toSet());
	}

	default Set<com.ubiqube.etsi.mano.dao.mano.pkg.RequestedAdditionalCapability> map(final Map<String, RequestedAdditionalCapability> value) {
		if (null == value) {
			return Set.of();
		}
		return value.entrySet().stream()
				.map(x -> mapToRequestedAdditionalCapability(x.getValue())).collect(Collectors.toSet());
	}

	@Mapping(target = "id", ignore = true)
	com.ubiqube.etsi.mano.dao.mano.pkg.RequestedAdditionalCapability mapToRequestedAdditionalCapability(RequestedAdditionalCapability value);
}
