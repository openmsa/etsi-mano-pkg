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
package com.ubiqube.etsi.mano.service.pkg.vnf;

import java.io.Closeable;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ubiqube.etsi.mano.service.pkg.bean.AffinityRuleAdapater;
import com.ubiqube.etsi.mano.service.pkg.bean.ProviderData;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupAdapter;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.InstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInitialDelta;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduScalingAspectDeltas;

import jakarta.annotation.Nullable;

/**
 *
 * @author Olivier Vignaud {@literal <ovi@ubiqube.com>}
 *
 */
public interface VnfPackageReader extends Closeable {

	ProviderData getProviderPadata();

	Set<AdditionalArtifact> getAdditionalArtefacts(Map<String, String> parameters);

	Set<VnfCompute> getVnfComputeNodes(Map<String, String> parameters);

	Set<VnfStorage> getVnfStorages(Map<String, String> parameters);

	Set<VnfVl> getVnfVirtualLinks(Map<String, String> parameters);

	Set<VnfLinkPort> getVnfVduCp(Map<String, String> parameters);

	Set<VnfExtCp> getVnfExtCp(Map<String, String> parameters);

	Set<ScalingAspect> getScalingAspects(Map<String, String> parameters);

	List<InstantiationLevels> getInstatiationLevels(Map<String, String> parameters);

	List<VduInstantiationLevels> getVduInstantiationLevels(Map<String, String> parameters);

	List<VduInitialDelta> getVduInitialDelta(Map<String, String> parameters);

	List<VduScalingAspectDeltas> getVduScalingAspectDeltas(Map<String, String> parameters);

	Set<AffinityRuleAdapater> getAffinityRules(Map<String, String> userDefinedData);

	Set<SecurityGroupAdapter> getSecurityGroups(Map<String, String> userData);

	List<String> getImports();

	@Nullable
	String getManifestContent();

	byte[] getFileContent(String fileName);

	Set<OsContainer> getOsContainer(Map<String, String> parameters);

	Set<OsContainerDeployableUnit> getOsContainerDeployableUnit(Map<String, String> parameters);

	Set<VirtualCp> getVirtualCp(Map<String, String> parameters);

	Set<VnfIndicator> getVnfIndicator(final Map<String, String> parameters);

	Set<McIops> getMciops(Map<String, String> userDefinedData);

	InputStream getFileInputStream(final String path);

	List<String> getVnfdFiles(boolean includeSignatures);

	Set<Repository> getRepositories();
}
