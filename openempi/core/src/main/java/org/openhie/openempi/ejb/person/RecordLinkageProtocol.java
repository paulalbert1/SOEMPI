/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openhie.openempi.ejb.person;

import java.util.List;

import javax.ejb.Remote;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.configuration.AdminConfiguration.ComponentType;
import org.openhie.openempi.matching.fellegisunter.BloomFilterParameterAdvice;
import org.openhie.openempi.model.MatchPairStatHalf;

@Remote
public interface RecordLinkageProtocol
{
	public void createMatchPairStatHalfTable(String sessionKey, String protocolTypeName, String statTableName,
			String datasetTableName, boolean withIndexesAndConstraints) throws ApplicationException;
	
	public void addMatchPairStatHalves(String sessionKey, String protocolTypeName, String statTableName,
			List<MatchPairStatHalf> matchPairStatHalves) throws ApplicationException;

	public void addIndexesAndConstraints(String sessionKey, String protocolTypeName, String statTableName, Long seqStart,
			String datasetTableName) throws ApplicationException;
	
	public int addPersonMatchRequest(String sessionKey, String protocolTypeName, String tableName, String address,
			byte[] myDhPublicKey, String matchPairStatHalfTableName) throws ApplicationException;

	public BloomFilterParameterAdvice acquireMatchRequests(String sessionKey, String protocolTypeName,
			int personMatchRequestId, ComponentType componentType) throws ApplicationException;
}
