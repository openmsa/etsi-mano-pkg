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
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubiqube.etsi.mano.dao.mano.AdditionalArtifact;
import com.ubiqube.etsi.mano.dao.mano.Attributes;
import com.ubiqube.etsi.mano.dao.mano.ScalingAspect;
import com.ubiqube.etsi.mano.dao.mano.TriggerDefinition;
import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.etsi.mano.dao.mano.VnfExtCp;
import com.ubiqube.etsi.mano.dao.mano.VnfIndicator;
import com.ubiqube.etsi.mano.dao.mano.VnfLinkPort;
import com.ubiqube.etsi.mano.dao.mano.VnfVl;
import com.ubiqube.etsi.mano.dao.mano.pkg.OsContainer;
import com.ubiqube.etsi.mano.dao.mano.pkg.OsContainerDeployableUnit;
import com.ubiqube.etsi.mano.dao.mano.pkg.VirtualCp;
import com.ubiqube.etsi.mano.dao.mano.repo.Repository;
import com.ubiqube.etsi.mano.dao.mano.repo.ToscaRepository;
import com.ubiqube.etsi.mano.dao.mano.vim.ContainerFormatType;
import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.etsi.mano.dao.mano.vim.VnfStorage;
import com.ubiqube.etsi.mano.dao.mano.vnfm.McIops;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.service.cond.ConditionService;
import com.ubiqube.etsi.mano.service.cond.Node;
import com.ubiqube.etsi.mano.service.pkg.bean.AffinityRuleAdapater;
import com.ubiqube.etsi.mano.service.pkg.bean.ProviderData;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupAdapter;
import com.ubiqube.etsi.mano.service.pkg.tosca.AbstractPackageReader;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ArtefactInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.PkgMapper;
import com.ubiqube.etsi.mano.service.pkg.vnf.VnfPackageReader;
import com.ubiqube.parser.tosca.Artifact;
import com.ubiqube.parser.tosca.ParseException;
import com.ubiqube.parser.tosca.RepositoryDefinition;
import com.ubiqube.parser.tosca.RepositoryDefinition.Credential;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.Mciop;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VNF;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VduCp;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfVirtualLink;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.Compute;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.VirtualBlockStorage;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.AffinityRule;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.AntiAffinityRule;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.InstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.ScalingAspects;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.SecurityGroupRule;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInitialDelta;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduScalingAspectDeltas;

import jakarta.annotation.Nonnull;

/**
 *
 * @author Olivier Vignaud {@literal <ovi@ubiqube.com>}
 *
 */
public class ToscaVnfPackageReader extends AbstractPackageReader implements VnfPackageReader {

	@Nonnull
	private final ConditionService conditionService;
	private final PkgMapper mapper;

	public ToscaVnfPackageReader(final InputStream data, final BinaryRepository repo, final UUID id, final ConditionService conditionService, final PkgMapper mapper, final ArtefactInformationsMapping artefactInformationsMapping) {
		super(data, repo, id, artefactInformationsMapping);
		this.conditionService = Objects.requireNonNull(conditionService);
		this.mapper = mapper;
	}

	@Override
	public @Nonnull ProviderData getProviderPadata() {
		final List<ProviderData> vnfs = getListOf(VNF.class, mapper::mapToProviderData, new HashMap<>());
		if (vnfs.isEmpty()) {
			throw new GenericException("Unable to find a VNFD block in this VNFD.");
		}
		final ProviderData vnf = vnfs.get(0);
		// Add default attributes
		final Attributes scaleStatus = Attributes.builder()
				.name("scale_status")
				.type("map")
				.entrySchema("tosca.datatypes.nfv.ScaleInfo")
				.build();
		final List<Attributes> attrs = Optional.ofNullable(vnf.getAttributes()).orElseGet(ArrayList::new);
		vnf.setAttributes(new ArrayList<>(attrs));
		vnf.getAttributes().add(scaleStatus);
		return vnf;
	}

	@Override
	public @Nonnull Set<AdditionalArtifact> getAdditionalArtefacts(final Map<String, String> parameters) {
		return getCsarFiles(AdditionalArtifact.class);
	}

	@Override
	public @Nonnull Set<VnfCompute> getVnfComputeNodes(final Map<String, String> parameters) {
		return this.getSetOf(Compute.class, mapper::mapToVnfCompute, parameters);
	}

	@Override
	public @Nonnull Set<VnfStorage> getVnfStorages(final Map<String, String> parameters) {
		return getSetOf(VirtualBlockStorage.class, mapper::mapToVnfStorage, parameters);
	}

	@Override
	public @Nonnull Set<VnfVl> getVnfVirtualLinks(final Map<String, String> parameters) {
		return this.getSetOf(VnfVirtualLink.class, mapper::mapToVnfVl, parameters);
	}

	@Override
	public @Nonnull Set<VnfLinkPort> getVnfVduCp(final Map<String, String> parameters) {
		return this.getSetOf(VduCp.class, mapper::mapToVnfLinkPort, parameters);
	}

	@Override
	public @Nonnull Set<VnfExtCp> getVnfExtCp(final Map<String, String> parameters) {
		return this.getSetOf(com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VnfExtCp.class, mapper::mapToVnfExtCp, parameters);
	}

	@Override
	public @Nonnull Set<ScalingAspect> getScalingAspects(final Map<String, String> parameters) {
		final List<ScalingAspects> list = getObjects(ScalingAspects.class, parameters);
		final Set<ScalingAspect> ret = new HashSet<>();
		for (final ScalingAspects scalingAspects : list) {
			final Map<String, com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScalingAspect> sa = scalingAspects.getAspects();
			final Set<ScalingAspect> tmp = sa.entrySet().stream().map(x -> {
				final ScalingAspect scaleRet = mapper.mapToScalingAspect(x.getValue());
				scaleRet.setName(x.getKey());
				return scaleRet;
			}).collect(Collectors.toSet());
			ret.addAll(tmp);
		}
		return ret;
	}

	@Override
	public @Nonnull List<InstantiationLevels> getInstatiationLevels(final Map<String, String> parameters) {
		return getListOf(InstantiationLevels.class, parameters);
	}

	@Override
	public @Nonnull List<VduInstantiationLevels> getVduInstantiationLevels(final Map<String, String> parameters) {
		return getListOf(VduInstantiationLevels.class, parameters);
	}

	@Override
	public @Nonnull List<VduInitialDelta> getVduInitialDelta(final Map<String, String> parameters) {
		return getListOf(VduInitialDelta.class, parameters);
	}

	@Override
	public @Nonnull List<VduScalingAspectDeltas> getVduScalingAspectDeltas(final Map<String, String> parameters) {
		return getListOf(VduScalingAspectDeltas.class, parameters);
	}

	@Override
	public @Nonnull Set<SecurityGroupAdapter> getSecurityGroups(final Map<String, String> userDefinedData) {
		final List<SecurityGroupRule> sgr = getObjects(SecurityGroupRule.class, userDefinedData);
		return sgr.stream().map(x -> new SecurityGroupAdapter(mapper.mapToSecurityGroup(x), x.getTargets())).collect(Collectors.toSet());
	}

	@Override
	public @Nonnull Set<AffinityRuleAdapater> getAffinityRules(final Map<String, String> userDefinedData) {
		final Set<AffinityRuleAdapater> af = getListOf(AffinityRule.class, userDefinedData).stream()
				.map(x -> {
					final com.ubiqube.etsi.mano.dao.mano.vim.AffinityRule afDao = mapper.mapToAffinityRule(x);
					return AffinityRuleAdapater.of(afDao, x.getTargets());
				})
				.collect(Collectors.toSet());
		final Set<AffinityRuleAdapater> anf = getListOf(AntiAffinityRule.class, userDefinedData).stream()
				.map(x -> {
					final com.ubiqube.etsi.mano.dao.mano.vim.AffinityRule afDao = mapper.mapToAffinityRule(x);
					afDao.setAnti(true);
					return AffinityRuleAdapater.of(afDao, x.getTargets());
				})
				.collect(Collectors.toSet());
		anf.forEach(x -> x.getAffinityRule().setAnti(true));
		af.addAll(anf);
		return af;
	}

	@Override
	public @Nonnull Set<OsContainer> getOsContainer(final Map<String, String> parameters) {
		return getSetOf(com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainer.class, mapper::mapToOsContainer, parameters);
	}

	@Override
	public @Nonnull Set<OsContainerDeployableUnit> getOsContainerDeployableUnit(final Map<String, String> parameters) {
		return getSetOf(com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainerDeployableUnit.class, mapper::mapToOsContainerDeployableUnit, parameters);
	}

	@Override
	public @Nonnull Set<VirtualCp> getVirtualCp(final Map<String, String> parameters) {
		return getSetOf(com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VirtualCp.class, mapper::mapToVirtualCp, parameters);
	}

	@Override
	public Set<VnfIndicator> getVnfIndicator(final Map<String, String> parameters) {
		final Set<VnfIndicator> vnfIndicators = getSetOf(com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VnfIndicator.class, mapper::mapToVnfIndicator, parameters);
		for (final VnfIndicator vnfIndicator : vnfIndicators) {
			final Set<String> mPs = new LinkedHashSet<>();
			final List<TriggerDefinition> triggerDefinitions = new ArrayList<>(vnfIndicator.getTriggers().values());
			for (final TriggerDefinition triggerDefinition : triggerDefinitions) {
				final ObjectMapper mapper = new ObjectMapper();
				final Node res = conditionService.parse(triggerDefinition.getCondition());
				final List<String> indicators = extractIndicators(res);
				mPs.addAll(indicators);
				try {
					triggerDefinition.setCondition(mapper.writeValueAsString(res));
				} catch (final JsonProcessingException e) {
					throw new ParseException("Unable to parse condition: " + triggerDefinition.getCondition(), e);
				}
			}
			vnfIndicator.setIndicators(mPs);
			vnfIndicator.setName(vnfIndicator.getToscaName());
		}
		return vnfIndicators;
	}

	private static List<String> extractIndicators(final Node root) {
		final NodeIndicatorVisitor niv = new NodeIndicatorVisitor();
		root.accept(niv, null);
		return niv.getResults();
	}

	@Override
	public @Nonnull Set<McIops> getMciops(final Map<String, String> userDefinedData) {
		return getSetOf(Mciop.class, this::map, userDefinedData);
	}

	private McIops map(final Mciop m) {
		final McIops ret = new McIops();
		ret.setAssociatedVdu(m.getAssociatedVduReq().stream().collect(Collectors.toSet()));
		ret.setToscaName(m.getInternalName());
		if (m.getArtifacts().size() != 1) {
			throw new GenericException("Size of artifact is incorrect, must be 1 but was " + m.getArtifacts().size());
		}
		final Entry<String, Artifact> arte = m.getArtifacts().entrySet().iterator().next();
		final Object obj = arte.getValue();
		if (!(obj instanceof final Artifact a)) {
			throw new GenericException("Only Artifact can be defined for " + m.getInternalName() + ", not " + obj.getClass().getSimpleName());
		}
		final SoftwareImage img = mapper.mapToSoftwareImage(a);
		img.setName(arte.getKey());
		img.setContainerFormat(ContainerFormatType.HELM);
		ret.setArtifacts(Map.of(arte.getKey(), img));
		return ret;
	}

	@Override
	public Set<Repository> getRepositories() {
		final Map<String, RepositoryDefinition> repos = Optional.ofNullable(getPkgRepositories()).orElseGet(Map::of);
		return repos.entrySet().stream().map(this::map).collect(Collectors.toSet());
	}

	private Repository map(final Entry<String, RepositoryDefinition> x) {
		final ToscaRepository rep = new ToscaRepository();
		rep.setName(x.getKey());
		final RepositoryDefinition val = x.getValue();
		rep.setDescription(val.getDescription());
		rep.setUrl(val.getUrl());
		final Credential cred = val.getCredential();
		if (null == cred) {
			return rep;
		}
		rep.setKeys(cred.getKeys());
		rep.setProtocol(cred.getProtocol());
		rep.setToken(cred.getToken());
		rep.setTokenType(cred.getTokenType());
		rep.setUsername(cred.getUser());
		return rep;
	}

	@Override
	public List<String> getVnfdFiles(final boolean includeSignatures) {
		return getVnfdFiles(getImports(), includeSignatures);
	}
}
