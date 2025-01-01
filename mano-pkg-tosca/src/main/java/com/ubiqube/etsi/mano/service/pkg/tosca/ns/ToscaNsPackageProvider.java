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
package com.ubiqube.etsi.mano.service.pkg.tosca.ns;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubiqube.etsi.mano.dao.mano.MonitoringParams;
import com.ubiqube.etsi.mano.dao.mano.NsSap;
import com.ubiqube.etsi.mano.dao.mano.NsVnfIndicator;
import com.ubiqube.etsi.mano.dao.mano.TriggerDefinition;
import com.ubiqube.etsi.mano.dao.mano.dto.NsNsd;
import com.ubiqube.etsi.mano.dao.mano.dto.NsVnf;
import com.ubiqube.etsi.mano.dao.mano.nsd.CpPair;
import com.ubiqube.etsi.mano.dao.mano.nsd.NfpDescriptor;
import com.ubiqube.etsi.mano.dao.mano.nsd.VnffgDescriptor;
import com.ubiqube.etsi.mano.dao.mano.nsd.VnffgInstance;
import com.ubiqube.etsi.mano.dao.mano.pm.PmType;
import com.ubiqube.etsi.mano.dao.mano.v2.nfvo.NsVirtualLink;
import com.ubiqube.etsi.mano.dao.mano.vim.vnffg.Classifier;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.service.pkg.ToscaException;
import com.ubiqube.etsi.mano.service.pkg.bean.NsInformations;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupAdapter;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.LevelMapping;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.NsScaling;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.RootLeaf;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.StepMapping;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.VlLevelMapping;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.VlStepMapping;
import com.ubiqube.etsi.mano.service.pkg.ns.NsPackageProvider;
import com.ubiqube.etsi.mano.service.pkg.tosca.AbstractPackageReader;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ArtefactInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsSapMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsVirtualLinkMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsVnfIndicatorMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.PkgMapper;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.LinkBitrateRequirements;
import com.ubiqube.parser.tosca.objects.tosca.groups.nfv.VNFFG;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.Forwarding;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.NFP;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.NS;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.NfpPosition;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.NfpPositionElement;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.Sap;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VNF;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.NfpRule;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.NsAutoScale;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.NsToLevelMapping;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.SecurityGroupRule;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VirtualLinkToLevelMapping;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VnfToInstantiationLevelMapping;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VnfToLevelMapping;

/**
 *
 * @author Olivier Vignaud {@literal <ovi@ubiqube.com>}
 *
 */
public class ToscaNsPackageProvider extends AbstractPackageReader implements NsPackageProvider {
	private static final Logger LOG = LoggerFactory.getLogger(ToscaNsPackageProvider.class);
	private final PkgMapper mapper;
	private final NsInformationsMapping nsInformationsMapping;
	private final NsVnfIndicatorMapping nsVnfIndicatorMapping;
	private final NsSapMapping nsSapMapping;
	private final NsVirtualLinkMapping nsVirtualLinkMapping;

	public ToscaNsPackageProvider(final InputStream data, final BinaryRepository repo, final UUID id,
			final PkgMapper mapper, final NsInformationsMapping nsInformationsMapping,
			final NsVnfIndicatorMapping nsVnfIndicatorMapping, final NsSapMapping nsSapMapping, final NsVirtualLinkMapping nsVirtualLinkMapping,
			final ArtefactInformationsMapping artefactInformationsMapping) {
		super(data, repo, id, artefactInformationsMapping);
		this.mapper = mapper;
		this.nsInformationsMapping = nsInformationsMapping;
		this.nsVnfIndicatorMapping = nsVnfIndicatorMapping;
		this.nsSapMapping = nsSapMapping;
		this.nsVirtualLinkMapping = nsVirtualLinkMapping;
	}

	@SuppressWarnings("null")
	@Override
	public NsInformations getNsInformations(final Map<String, String> userData) {
		final List<NsInformations> nss = getListOf(NS.class, nsInformationsMapping::mapToNsInformations, userData);
		if (nss.isEmpty()) {
			throw new GenericException("Unable to find a NSD block in this NSD.");
		}
		return nss.get(0);
	}

	@Override
	public Set<NsVirtualLink> getNsVirtualLink(final Map<String, String> userData) {
		return getSetOf(com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.NsVirtualLink.class, nsVirtualLinkMapping::mapToNsVirtualLink, userData);
	}

	@Override
	public Set<NsSap> getNsSap(final Map<String, String> userData) {
		return getSetOf(Sap.class, nsSapMapping::mapToNsSap, userData);
	}

	@Override
	public Set<SecurityGroupAdapter> getSecurityGroups(final Map<String, String> userData) {
		final List<SecurityGroupRule> sgr = getObjects(SecurityGroupRule.class, userData);
		return sgr.stream().map(x -> new SecurityGroupAdapter(mapper.mapToSecurityGroup(x), x.getTargets())).collect(Collectors.toSet());
	}

	@Override
	public Set<NsNsd> getNestedNsd(final Map<String, String> userData) {
		final List<NS> sgr = new ArrayList<>(getObjects(NS.class, userData));
		if (!sgr.isEmpty()) {
			sgr.remove(0);
		}
		return sgr.stream()
				.map(x -> new NsNsd(x.getInvariantId(), x.getInternalName(), x.getFlavourId(), x.getVirtualLinkReq()))
				.collect(Collectors.toSet());
	}

	@Override
	public Set<NsVnf> getVnfd(final Map<String, String> userData) {
		final List<VNF> sgr = getObjects(VNF.class, userData);
		return sgr.stream()
				.map(ToscaNsPackageProvider::map)
				.collect(Collectors.toSet());
	}

	private static NsVnf map(final VNF x) {
		final NsVnf o = new NsVnf();
		o.setVnfdId(x.getDescriptorId());
		o.setName(x.getInternalName());
		o.setFlavourId(x.getFlavourId());
		o.setVirtualLink(x.getVirtualLinkReq());
		o.setVirtualLink1(x.getVirtualLink1Req());
		o.setVirtualLink2(x.getVirtualLink2Req());
		o.setVirtualLink3(x.getVirtualLink3Req());
		o.setVirtualLink4(x.getVirtualLink4Req());
		o.setVirtualLink5(x.getVirtualLink5Req());
		o.setVirtualLink6(x.getVirtualLink6Req());
		o.setVirtualLink7(x.getVirtualLink7Req());
		o.setVirtualLink8(x.getVirtualLink8Req());
		o.setVirtualLink9(x.getVirtualLink9Req());
		o.setVirtualLink10(x.getVirtualLink10Req());
		return o;
	}

	/**
	 * VNFFGd -> 1..N, NfpPositionElement -> 0..N Nfpd.
	 */
	@Override
	public Set<VnffgDescriptor> getVnffg(final Map<String, String> userData) {
		// Port pair group ?
		final List<NfpPositionElement> elements = getObjects(NfpPositionElement.class, userData);
		// NfpPosition link to NfpPositionElement
		final List<NfpPosition> nfpPosition = getObjects(NfpPosition.class, userData);
		// nfp = Link to NfpPosition
		final List<NFP> nfp = getObjects(NFP.class, userData);
		final List<Forwarding> fwList = getObjects(Forwarding.class, userData);
		// vnffg link to NFP, VNF, PNF, NS, NsVirtualLink, NfpPositionElement
		final List<VNFFG> vnffg = getObjects(VNFFG.class, userData);
		return vnffg.stream().map(x -> {
			final VnffgDescriptor vnffgd = new VnffgDescriptor();
			vnffgd.setName(x.getInternalName());
			vnffgd.setNfpd(new ArrayList<>());
			final List<NFP> nfpd = findNfp(nfp, x.getMembers());
			nfpd.forEach(lNfp -> {
				final NfpDescriptor nnfpd = new NfpDescriptor();
				vnffgd.getNfpd().add(nnfpd);
				nnfpd.setToscaName(lNfp.getInternalName());
				nnfpd.setInstances(new ArrayList<>());
				final NfpRule nfpRule = findNfpRule(lNfp.getInternalName());
				vnffgd.setClassifier(toClassifier(nfpRule));
				lNfp.getNfpPositionReq().forEach(lPosition -> {
					final List<NfpPosition> pos = findNfpPosition(nfpPosition, lPosition);
					pos.forEach(l -> l.getElementReq().forEach(m -> {
						final List<NfpPositionElement> elem = findPositionElement(elements, m);
						elem.forEach(nfposElem -> {
							final VnffgInstance vnffgInstance = new VnffgInstance();
							nnfpd.getInstances().add(vnffgInstance);
							vnffgInstance.setPairs(new ArrayList<>());
							vnffgInstance.setToscaName(nfposElem.getInternalName());
							final CpPair cp = handleProfileElement(nfposElem, fwList);
							vnffgInstance.getPairs().add(cp);
						});
					}));
				});
			});
			return vnffgd;
		})
				.collect(Collectors.toSet());
	}

	private NfpRule findNfpRule(final String internalName) {
		final List<NfpRule> nfpRules = getObjects(NfpRule.class, Map.of());
		return nfpRules.stream()
				.filter(x -> x.getTargets().contains(internalName))
				.findFirst()
				.orElseThrow(() -> new ToscaException("Could not find nfpRule " + internalName));
	}

	private static CpPair handleProfileElement(final NfpPositionElement nfposElem, final List<Forwarding> fwList) {
		final List<String> nfposElement = nfposElem.getProfileElementReq();
		if (nfposElement.size() > 2) {
			throw new ToscaException("Size of list 'ProfileElement' must be equal or less than 2.");
		}
		final String igress = nfposElement.get(0);
		final CpPair cp = new CpPair();
		cp.setIngress(igress);
		final Forwarding fwin = findForwarding(fwList, igress);
		cp.setToscaName(nfposElem.getInternalName());
		cp.setIngressVl(fwin.getVirtualLinkReq());
		if (nfposElement.size() != 2) {
			return cp;
		}
		final String egress = nfposElement.get(1);
		cp.setEgress(egress);
		final Forwarding fweg = findForwarding(fwList, egress);
		cp.setEgressVl(fweg.getVirtualLinkReq());
		return cp;
	}

	private static Forwarding findForwarding(final List<Forwarding> fwList, final String igress) {
		return fwList.stream()
				.filter(x -> x.getInternalName().equals(igress))
				.findFirst()
				.orElseThrow(() -> new ToscaException("Unable to find Forwarding with name: " + igress));
	}

	private static List<NfpPositionElement> findPositionElement(final List<NfpPositionElement> elements, final String m) {
		return elements.stream().filter(x -> x.getInternalName().equals(m)).toList();
	}

	private static List<NfpPosition> findNfpPosition(final List<NfpPosition> nfpPosition, final String z) {
		return nfpPosition.stream().filter(x -> x.getInternalName().equals(z)).toList();
	}

	private static Classifier toClassifier(final NfpRule nr) {
		final Classifier ret = new Classifier();
		ret.setDestinationIpAddressPrefix(nr.getDestinationIpAddressPrefix());
		Optional.ofNullable(nr.getDestinationPortRange()).ifPresent(x -> {
			ret.setDestinationPortRangeMin(x.getMin());
			ret.setDestinationPortRangeMax(x.getMax());
		});
		ret.setClassifierName(nr.getInternalName());
		ret.setDscp(nr.getDscp());
		ret.setEtherDestinationAddress(nr.getEtherDestinationAddress());
		ret.setEtherSourceAddress(nr.getEtherSourceAddress());
		ret.setEtherType(nr.getEtherType());
		nr.getExtendedCriteria();
		ret.setProtocol(nr.getProtocol());
		ret.setSourceIpAddressPrefix(nr.getSourceIpAddressPrefix());
		nr.getSourcePortRange();
		Optional.ofNullable(nr.getSourcePortRange()).ifPresent(x -> {
			ret.setSourcePortRangeMin(x.getMin());
			ret.setSourcePortRangeMax(x.getMax());
		});
		ret.setVlanTag(Optional.ofNullable(nr.getVlanTag()).orElseGet(List::of).stream().collect(Collectors.toSet()));
		return ret;
	}

	private static List<NFP> findNfp(final List<NFP> nfp, final List<String> members) {
		return nfp.stream().filter(x -> members.contains(x.getInternalName())).toList();
	}

	@Override
	public boolean isAutoHealEnabled() {
		final List<NsAutoScale> autoScale = getObjects(NsAutoScale.class, Map.of());
		return !autoScale.isEmpty();
	}

	@Override
	public NsScaling getNsScaling(final Map<String, String> userData) {
		final NsScaling ret = new NsScaling();
		// Level
		final List<VnfToInstantiationLevelMapping> vnfLevel = getObjects(VnfToInstantiationLevelMapping.class, userData);
		ret.setVnfLevelMapping(vnfLevel.stream().map(x -> new LevelMapping(x.getInternalName(), x.getTargets(), x.getNumberOfInstances())).toList());
		final List<NsToLevelMapping> nsLevel = getObjects(NsToLevelMapping.class, userData);
		ret.setNsLevelMapping(nsLevel.stream().map(x -> new LevelMapping(x.getAspect(), x.getTargets(), x.getNumberOfInstances())).toList());
		final List<VirtualLinkToLevelMapping> vlLevel = getObjects(VirtualLinkToLevelMapping.class, userData);
		ret.setVlLevelMapping(vlLevel.stream().map(x -> new VlLevelMapping(x.getTargets(), map(x.getBitRateRequirements()))).toList());
		// Step
		final List<VnfToLevelMapping> vnfStep = getObjects(VnfToLevelMapping.class, userData);
		ret.setVnfStepMapping(vnfStep.stream().map(x -> new StepMapping(x.getAspect(), x.getTargets(), mapLevel(x.getNumberOfInstances()))).toList());
		final List<NsToLevelMapping> nsStep = getObjects(NsToLevelMapping.class, userData);
		ret.setNsStepMapping(nsStep.stream().map(x -> new StepMapping(x.getAspect(), x.getTargets(), mapLevel(x.getNumberOfInstances()))).toList());
		final List<VirtualLinkToLevelMapping> vlStep = getObjects(VirtualLinkToLevelMapping.class, userData);
		ret.setVlStepMapping(vlStep.stream().map(x -> new VlStepMapping(x.getAspect(), x.getTargets(), mapVlLevel(x.getBitRateRequirements()))).toList());
		return ret;
	}

	@Override
	public Set<NsVnfIndicator> getNsVnfIndicator(final Map<String, String> parameters) {
		final Set<NsVnfIndicator> nsVnfIndicators = getSetOf(com.ubiqube.parser.tosca.objects.tosca.policies.nfv.NsAutoScale.class, nsVnfIndicatorMapping::mapToNsVnfIndicator, parameters);
		for (final NsVnfIndicator nsVnfIndicator : nsVnfIndicators) {
			final Map<String, MonitoringParams> mPs = new HashMap<>();
			final List<TriggerDefinition> triggerDefinitions = new ArrayList<>(
					nsVnfIndicator.getTriggers().values());
			for (final TriggerDefinition triggerDefinition : triggerDefinitions) {
				final ObjectMapper mapper = new ObjectMapper();
				JsonNode actualObj;
				try {
					actualObj = mapper.readTree(triggerDefinition.getCondition());
					for (final JsonNode jsonNode : actualObj) {
						final Map<String, Object> conditions = mapper.convertValue(jsonNode,
								new TypeReference<Map<String, Object>>() {
									//
								});
						for (final String keyInd : conditions.keySet()) {
							final MonitoringParams monitoringParams = new MonitoringParams();
							monitoringParams.setCollectionPeriod(600L);
							monitoringParams.setName(keyInd);
							monitoringParams.setPerformanceMetric(keyInd);
							monitoringParams.setObjectType(PmType.NS);
							mPs.put(keyInd, monitoringParams);
						}
					}
				} catch (final JsonProcessingException e) {
					LOG.error(e.getMessage());
				}
			}
			final Set<MonitoringParams> m = new HashSet<>(mPs.values());
			nsVnfIndicator.setMonitoringParameters(m);
			nsVnfIndicator.setName(nsVnfIndicator.getToscaName());
		}
		return nsVnfIndicators;
	}

	private static Map<Integer, RootLeaf> mapVlLevel(final Map<String, LinkBitrateRequirements> bitRateRequirements) {
		return bitRateRequirements.entrySet().stream()
				.collect(Collectors.toMap(x -> Integer.valueOf(x.getKey()), x -> new RootLeaf(x.getValue().getRoot(), x.getValue().getLeaf())));
	}

	private static Map<Integer, Integer> mapLevel(final Map<String, Integer> map) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(x -> Integer.valueOf(x.getKey()), Entry::getValue));
	}

	private static Map<String, RootLeaf> map(final Map<String, LinkBitrateRequirements> bitRateRequirements) {
		return bitRateRequirements.entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, x -> new RootLeaf(x.getValue().getRoot(), x.getValue().getLeaf())));
	}

}
