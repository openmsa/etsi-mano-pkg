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
package com.ubiqube.etsi.mano.service.pkg;

import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.AffinityRuleToscaMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.OsContainerMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.PkgMapper;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ScalingMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.SecurityGroupRuleMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.StorageMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.VirtualCpMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.VnfComputeMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.VnfIndicatorMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.VnfMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.VnfVlMapping;

public class TestFactory {

	private TestFactory() {
		//
	}

	public static PkgMapper createPkgMapper() {
		final VnfMapping vnfMapping = Mappers.getMapper(VnfMapping.class);
		final VnfComputeMapping vnfComputeMapping = Mappers.getMapper(VnfComputeMapping.class);
		final ScalingMapping scalingMapping = Mappers.getMapper(ScalingMapping.class);
		final SecurityGroupRuleMapping securityGroupRuleMapping = Mappers.getMapper(SecurityGroupRuleMapping.class);
		final AffinityRuleToscaMapping affinityRuleToscaMapping = Mappers.getMapper(AffinityRuleToscaMapping.class);
		final StorageMapping storageMapping = Mappers.getMapper(StorageMapping.class);
		final VnfVlMapping vnfVlMapping = Mappers.getMapper(VnfVlMapping.class);
		final OsContainerMapping osContainerMapping = Mappers.getMapper(OsContainerMapping.class);
		final VnfIndicatorMapping vnfIndicatorMapping = Mappers.getMapper(VnfIndicatorMapping.class);
		final VirtualCpMapping virtualCpMapping = Mappers.getMapper(VirtualCpMapping.class);
		return new PkgMapper(vnfMapping, vnfComputeMapping, scalingMapping, securityGroupRuleMapping, affinityRuleToscaMapping, storageMapping,
				vnfVlMapping, osContainerMapping, vnfIndicatorMapping, virtualCpMapping);
	}
}
