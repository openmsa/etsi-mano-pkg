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

import java.time.ZonedDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.ubiqube.etsi.mano.dao.mano.VduProfile;
import com.ubiqube.etsi.mano.dao.mano.pkg.MaxNumberOfImpactedInstance;
import com.ubiqube.etsi.mano.dao.mano.pkg.MinNumberOfPreservedInstance;
import com.ubiqube.etsi.mano.dao.mano.pkg.NfviMaintenanceInfo;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MaxNumberOfImpactedInstances;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.MinNumberOfPreservedInstances;
import com.ubiqube.parser.tosca.scalar.Time;

import jakarta.annotation.Nullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VduProfileMapping {
	@Mapping(target = "affinityOrAntiAffinityGroupId", ignore = true)
	@Mapping(target = "localAffinityOrAntiAffinityRule", ignore = true)
	VduProfile mapToVduProfile(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VduProfile o);

	default ZonedDateTime toZonedDateTime(final @Nullable Time time) {
		return null;
	}

	@Mapping(target = "id", ignore = true)
	MaxNumberOfImpactedInstance mapToMaxNumberOfImpactedInstance(MaxNumberOfImpactedInstances o);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "impactMitigationRequested", source = "isImpactMitigationRequested")
	NfviMaintenanceInfo mapToNfviMaintenanceInfo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.NfviMaintenanceInfo o);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "minNumberOfImpactedInstances", source = "minNumberOfPreservedInstances")
	MinNumberOfPreservedInstance mapToMinNumberOfPreservedInstance(MinNumberOfPreservedInstances o);
}
