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

import com.ubiqube.etsi.mano.dao.mano.ScalingAspect;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.InstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInitialDelta;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduScalingAspectDeltas;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScalingMapping extends ScaleCommonMapping {

	@Mapping(target = "id", ignore = true)
	ScalingAspect map(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScalingAspect o);

	com.ubiqube.etsi.mano.service.pkg.bean.VduInitialDelta mapToVduInitialDelta(final VduInitialDelta x);

	com.ubiqube.etsi.mano.service.pkg.bean.VduScalingAspectDeltas mapToVduScalingAspectDeltas(final VduScalingAspectDeltas x);

	com.ubiqube.etsi.mano.service.pkg.bean.VduInstantiationLevels mapToVduInstantiationLevels(final VduInstantiationLevels x);

	com.ubiqube.etsi.mano.service.pkg.bean.InstantiationLevels mapToInstantiationLevels(final InstantiationLevels x);

}
