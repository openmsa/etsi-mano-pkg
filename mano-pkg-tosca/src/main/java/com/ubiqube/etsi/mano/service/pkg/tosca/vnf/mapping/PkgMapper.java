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

import com.ubiqube.etsi.mano.dao.mano.ScalingAspect;
import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.etsi.mano.dao.mano.VnfLinkPort;
import com.ubiqube.etsi.mano.dao.mano.VnfVl;
import com.ubiqube.etsi.mano.dao.mano.vim.AffinityRule;
import com.ubiqube.etsi.mano.dao.mano.vim.SecurityGroup;
import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.etsi.mano.dao.mano.vim.VnfStorage;
import com.ubiqube.etsi.mano.service.pkg.bean.ProviderData;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VNF;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VduCp;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VirtualCp;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfExtCp;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfVirtualLink;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.Compute;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainer;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainerDeployableUnit;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.VirtualBlockStorage;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.AntiAffinityRule;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.SecurityGroupRule;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VnfIndicator;

public class PkgMapper {
	private final VnfMapping vnfMapping;
	private final VnfComputeMapping vnfComputeMapping;
	private final ScalingMapping scalingMapping;
	private final SecurityGroupRuleMapping securityGroupRuleMapping;
	private final AffinityRuleToscaMapping affinityRuleToscaMapping;
	private final StorageMapping storageMapping;
	private final VnfVlMapping vnfVlMapping;
	private final OsContainerMapping osContainerMapping;
	private final VnfIndicatorMapping vnfIndicatorMapping;
	private final VirtualCpMapping virtualCpMapping;

	public PkgMapper(final VnfMapping vnfMapping, final VnfComputeMapping vnfComputeMapping, final ScalingMapping scalingMapping, final SecurityGroupRuleMapping securityGroupRuleMapping, final AffinityRuleToscaMapping affinityRuleToscaMapping, final StorageMapping storageMapping, final VnfVlMapping vnfVlMapping, final OsContainerMapping osContainerMapping, final VnfIndicatorMapping vnfIndicatorMapping, final VirtualCpMapping virtualCpMapping) {
		this.vnfMapping = vnfMapping;
		this.vnfComputeMapping = vnfComputeMapping;
		this.scalingMapping = scalingMapping;
		this.securityGroupRuleMapping = securityGroupRuleMapping;
		this.affinityRuleToscaMapping = affinityRuleToscaMapping;
		this.storageMapping = storageMapping;
		this.vnfVlMapping = vnfVlMapping;
		this.osContainerMapping = osContainerMapping;
		this.vnfIndicatorMapping = vnfIndicatorMapping;
		this.virtualCpMapping = virtualCpMapping;
	}

	public VnfCompute mapToVnfCompute(final Compute x) {
		return vnfComputeMapping.map(x);
	}

	public SoftwareImage mapToSoftwareImage(final Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public ScalingAspect mapToScalingAspect(final com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScalingAspect value) {
		return scalingMapping.map(value);
	}

	public SecurityGroup mapToSecurityGroup(final SecurityGroupRule x) {
		return securityGroupRuleMapping.map(x);
	}

	public AffinityRule mapToAffinityRule(final com.ubiqube.parser.tosca.objects.tosca.policies.nfv.AffinityRule x) {
		return affinityRuleToscaMapping.map(x);
	}

	public AffinityRule mapToAffinityRule(final AntiAffinityRule x) {
		return affinityRuleToscaMapping.map(x);
	}

	public ProviderData mapToProviderData(final VNF x) {
		return vnfMapping.map(x);
	}

	public VnfStorage mapToVnfStorage(final VirtualBlockStorage x) {
		return storageMapping.map(x);
	}

	public VnfVl mapToVnfVl(final VnfVirtualLink x) {
		return vnfVlMapping.map(x);
	}

	public VnfLinkPort mapToVnfLinkPort(final VduCp x) {
		return vnfVlMapping.mapToVnfLinkPort(x);
	}

	public com.ubiqube.etsi.mano.dao.mano.VnfExtCp mapToVnfExtCp(final VnfExtCp x) {
		return vnfVlMapping.mapToVnfExtCp(x);
	}

	public com.ubiqube.etsi.mano.dao.mano.pkg.VirtualCp mapToVirtualCp(final VirtualCp x) {
		return virtualCpMapping.mapToVirtualCp(x);
	}

	public com.ubiqube.etsi.mano.dao.mano.VnfIndicator mapToVnfIndicator(final VnfIndicator x) {
		return vnfIndicatorMapping.map(x);
	}

	public com.ubiqube.etsi.mano.dao.mano.pkg.OsContainerDeployableUnit mapToOsContainerDeployableUnit(final OsContainerDeployableUnit x) {
		return osContainerMapping.mapToOsContainerDeployableUnit(x);
	}

	public com.ubiqube.etsi.mano.dao.mano.pkg.OsContainer mapToOsContainer(final OsContainer x) {
		return osContainerMapping.mapToOsContainer(x);
	}

}
