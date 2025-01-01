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
/**
 * This copy of Woodstox XML processor is licensed under the
 * Apache (Software) License, version 2.0 ("the License").
 * See the License for details about distribution rights, and the
 * specific rights regarding derivate works.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing Woodstox, in file "ASL2.0", under the same directory
 * as this file.
 */
package com.ubiqube.etsi.mano.pkg.k8s;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import com.ubiqube.etsi.mano.service.pkg.ToscaException;
import com.ubiqube.etsi.mano.service.pkg.bean.AffinityRuleAdapater;
import com.ubiqube.etsi.mano.service.pkg.bean.ProviderData;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupAdapter;
import com.ubiqube.etsi.mano.service.pkg.vnf.VnfPackageReader;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.InstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInitialDelta;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduScalingAspectDeltas;

import io.kubernetes.client.openapi.models.V1Pod;
import org.jspecify.annotations.NonNull;

/**
 *
 * @author Olivier Vignaud {@literal <ovi@ubiqube.com>}
 *
 */
public class K8sPodReader implements VnfPackageReader {

	private V1Pod obj;

	public K8sPodReader(final byte[] data) {
		final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			obj = mapper.readValue(data, V1Pod.class);
		} catch (final IOException e) {
			throw new ToscaException(e);
		}
	}

	@Override
	public @NonNull ProviderData getProviderPadata() {
		final ProviderData pd = new ProviderData();
		pd.setVnfProvider("K8S-POD");
		pd.setVnfProductName(obj.getKind());
		return pd;
	}

	@Override
	public @NonNull Set<AdditionalArtifact> getAdditionalArtefacts(final Map<String, String> parameters) {
		return new HashSet<>();
	}

	@Override
	public @NonNull Set<VnfCompute> getVnfComputeNodes(final Map<String, String> parameters) {
		final VnfCompute vc = new VnfCompute();
		vc.setToscaName(obj.getKind());
		return Set.of(vc);
	}

	@Override
	public @NonNull Set<VnfStorage> getVnfStorages(final Map<String, String> parameters) {
		return new HashSet<>();
	}

	@Override
	public @NonNull Set<VnfVl> getVnfVirtualLinks(final Map<String, String> parameters) {
		return new HashSet<>();
	}

	@Override
	public @NonNull Set<VnfLinkPort> getVnfVduCp(final Map<String, String> parameters) {
		return new HashSet<>();
	}

	@Override
	public @NonNull Set<VnfExtCp> getVnfExtCp(final Map<String, String> parameters) {
		return new HashSet<>();
	}

	@Override
	public @NonNull Set<ScalingAspect> getScalingAspects(final Map<String, String> parameters) {
		return new HashSet<>();
	}

	@Override
	public @NonNull List<InstantiationLevels> getInstatiationLevels(final Map<String, String> parameters) {
		return new ArrayList<>();
	}

	@Override
	public @NonNull List<VduInstantiationLevels> getVduInstantiationLevels(final Map<String, String> parameters) {
		return new ArrayList<>();
	}

	@Override
	public @NonNull List<VduInitialDelta> getVduInitialDelta(final Map<String, String> parameters) {
		return new ArrayList<>();
	}

	@Override
	public @NonNull List<VduScalingAspectDeltas> getVduScalingAspectDeltas(final Map<String, String> parameters) {
		return new ArrayList<>();
	}

	@Override
	public void close() throws IOException {
		//
	}

	@Override
	public @NonNull Set<AffinityRuleAdapater> getAffinityRules(final Map<String, String> userDefinedData) {
		return Set.of();
	}

	@Override
	public @NonNull Set<SecurityGroupAdapter> getSecurityGroups(final Map<String, String> userData) {
		return Set.of();
	}

	@Override
	public @NonNull List<String> getImports() {
		return List.of();
	}

	@Override
	public String getManifestContent() {
		return null;
	}

	@Override
	public byte[] getFileContent(final String fileName) {
		return new byte[0];
	}

	@Override
	public @NonNull Set<OsContainer> getOsContainer(final Map<String, String> parameters) {
		return Set.of();
	}

	@Override
	public @NonNull Set<OsContainerDeployableUnit> getOsContainerDeployableUnit(final Map<String, String> parameters) {
		return Set.of();
	}

	@Override
	public @NonNull Set<VirtualCp> getVirtualCp(final Map<String, String> parameters) {
		return Set.of();
	}

	@Override
	public @NonNull Set<McIops> getMciops(final Map<String, String> userDefinedData) {
		return Set.of();
	}

	@Override
	public InputStream getFileInputStream(final String path) {
		return null;
	}

	@Override
	public List<String> getVnfdFiles(final boolean includeSignatures) {
		return List.of();
	}

	@Override
	public Set<VnfIndicator> getVnfIndicator(final Map<String, String> parameters) {
		return Set.of();
	}

	@Override
	public Set<Repository> getRepositories() {
		return Set.of();
	}

}
