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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.ubiqube.etsi.mano.dao.mano.AdditionalArtifact;
import com.ubiqube.etsi.mano.dao.mano.ScalingAspect;
import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.etsi.mano.dao.mano.VnfExtCp;
import com.ubiqube.etsi.mano.dao.mano.VnfIndicator;
import com.ubiqube.etsi.mano.dao.mano.VnfLinkPort;
import com.ubiqube.etsi.mano.dao.mano.VnfVl;
import com.ubiqube.etsi.mano.dao.mano.pkg.OsContainer;
import com.ubiqube.etsi.mano.dao.mano.pkg.OsContainerDeployableUnit;
import com.ubiqube.etsi.mano.dao.mano.pkg.VirtualCp;
import com.ubiqube.etsi.mano.dao.mano.repo.Repository;
import com.ubiqube.etsi.mano.dao.mano.vim.VnfStorage;
import com.ubiqube.etsi.mano.dao.mano.vnfm.McIops;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.service.pkg.bean.AffinityRuleAdapater;
import com.ubiqube.etsi.mano.service.pkg.bean.ProviderData;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupAdapter;
import com.ubiqube.etsi.mano.service.pkg.tosca.AbstractPackageReader;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.VnfMapping;
import com.ubiqube.etsi.mano.service.pkg.vnf.VnfPackageReader;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VNF;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.InstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInitialDelta;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduScalingAspectDeltas;

import ma.glasnost.orika.MapperFactory;

public class ToscaVnfPackageReaderMapstruct extends AbstractPackageReader implements VnfPackageReader {
	VnfMapping vnfMapping;

	protected ToscaVnfPackageReaderMapstruct(final InputStream data, final BinaryRepository repo, final UUID id) {
		super(data, repo, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ProviderData getProviderPadata() {
		final List<ProviderData> vnfs = getListOf(VNF.class, x -> vnfMapping.map(x), new HashMap<>());
		if (vnfs.isEmpty()) {
			return null;
		}
		return vnfs.get(0);
	}

	@Override
	public Set<AdditionalArtifact> getAdditionalArtefacts(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VnfCompute> getVnfComputeNodes(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VnfStorage> getVnfStorages(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VnfVl> getVnfVirtualLinks(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VnfLinkPort> getVnfVduCp(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VnfExtCp> getVnfExtCp(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ScalingAspect> getScalingAspects(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InstantiationLevels> getInstatiationLevels(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VduInstantiationLevels> getVduInstantiationLevels(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VduInitialDelta> getVduInitialDelta(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VduScalingAspectDeltas> getVduScalingAspectDeltas(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<AffinityRuleAdapater> getAffinityRules(final Map<String, String> userDefinedData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SecurityGroupAdapter> getSecurityGroups(final Map<String, String> userData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OsContainer> getOsContainer(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OsContainerDeployableUnit> getOsContainerDeployableUnit(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VirtualCp> getVirtualCp(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VnfIndicator> getVnfIndicator(final Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<McIops> getMciops(final Map<String, String> userDefinedData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Repository> getRepositories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void additionalMapping(final MapperFactory mapperFactory) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getVnfdFiles(final boolean includeSignatures) {
		// TODO Auto-generated method stub
		return null;
	}

}
