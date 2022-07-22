/**
 *     Copyright (C) 2019-2020 Ubiqube.
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
package com.ubiqube.etsi.mano.service.pkg.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NsInformations {

	private String nsdId;

	private String nsdName;

	private String nsdVersion;

	private String nsdDesigner;

	private String nsdInvariantId;

	private String instantiationLevel;

	private int minNumberOfInstance;

	private int maxNumberOfInstance;

	private String flavorId;

	private String virtualLinkReq;
	private String virtualLink1Req;
	private String virtualLink2Req;
	private String virtualLink3Req;
	private String virtualLink4Req;
	private String virtualLink5Req;
	private String virtualLink6Req;
	private String virtualLink7Req;
	private String virtualLink8Req;
	private String virtualLink9Req;
	private String virtualLink10Req;

}
