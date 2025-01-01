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
package com.ubiqube.etsi.mano.service.pkg.ns;

import java.util.Map;
import java.util.Set;

import com.ubiqube.etsi.mano.dao.mano.NsSap;
import com.ubiqube.etsi.mano.dao.mano.NsVnfIndicator;
import com.ubiqube.etsi.mano.dao.mano.dto.NsNsd;
import com.ubiqube.etsi.mano.dao.mano.dto.NsVnf;
import com.ubiqube.etsi.mano.dao.mano.nsd.VnffgDescriptor;
import com.ubiqube.etsi.mano.dao.mano.v2.nfvo.NsVirtualLink;
import com.ubiqube.etsi.mano.service.pkg.bean.NsInformations;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupAdapter;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.NsScaling;

public interface NsPackageProvider {

	NsInformations getNsInformations(Map<String, String> userData);

	Set<NsVirtualLink> getNsVirtualLink(Map<String, String> userData);

	Set<NsSap> getNsSap(Map<String, String> userData);

	Set<SecurityGroupAdapter> getSecurityGroups(Map<String, String> userData);

	Set<NsNsd> getNestedNsd(final Map<String, String> userData);

	Set<NsVnf> getVnfd(final Map<String, String> userData);

	Set<VnffgDescriptor> getVnffg(Map<String, String> userData);

	boolean isAutoHealEnabled();

	NsScaling getNsScaling(Map<String, String> userData);

	Set<NsVnfIndicator> getNsVnfIndicator(final Map<String, String> parameters);
}
