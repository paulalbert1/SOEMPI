/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openempi.webapp.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.ReferenceDataService;
import org.openhie.openempi.blocking.BlockingServiceSelector;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.MatchingServiceSelector;
import org.openhie.openempi.model.FieldType;
import org.openhie.openempi.recordlinkage.RecordLinkageProtocolSelector;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.transformation.TransformationFunctionType;
import org.openhie.openempi.transformation.TransformationService;
import org.openhie.openempi.transformation.function.corruption.LastnameCorruptor;
import org.openhie.openempi.transformation.function.corruption.NicknameCorruptor;
import org.openhie.openempi.transformation.function.corruption.StringCorruptor;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReferenceDataServiceImpl extends RemoteServiceServlet implements ReferenceDataService
{
	private static final long serialVersionUID = -643119084204938566L;

	private Logger log = Logger.getLogger(getClass());
	private ApplicationContext context;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
	}
	
	public List<String> getAllTransformationFunctionNames() {
		log.debug("Received request to retrieve the list of all transformation function names.");
		try {
			TransformationService transformationService = Context.getTransformationService();
			List<String> transformationFunctionNames = transformationService.getAllTransformationFunctionNames();
			return transformationFunctionNames;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<String> getStringTransformationFunctionNames() {
		log.debug("Received request to retrieve the list of string transformation function names.");
		try {
			TransformationService transformationService = Context.getTransformationService();
			List<String> transformationFunctionNames =
				transformationService.getTransformationFunctionNames(FieldType.FieldTypeEnum.String);
			return transformationFunctionNames;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<String> getStreamTransformationFunctionNames() {
		log.debug("Received request to retrieve the list of stream transformation function names.");
		try {
			TransformationService transformationService = Context.getTransformationService();
			List<String> transformationFunctionNames =
				transformationService.getTransformationFunctionNames(FieldType.FieldTypeEnum.Blob);
			return transformationFunctionNames;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<String> getComparatorFunctionNames() {
		log.debug("Received request to retrieve the list of comparator function names.");
		try {
			StringComparisonService stringComparisonService = Context.getStringComparisonService();
			List<String> comparisonFunctionNames = stringComparisonService.getComparisonFunctionNames();
			return comparisonFunctionNames;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<String> getBlockingServiceNames() {
		log.debug("Received request to retrieve the list of blocking service names.");
		try {
			BlockingServiceSelector blockingServiceSelector = Context.getBlockingServiceSelector();
			List<String> blockingServiceNames = blockingServiceSelector.getBlockingServiceNames();
			return blockingServiceNames;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<String> getMatchingServiceNames() {
		log.debug("Received request to retrieve the list of matching service names.");
		try {
			MatchingServiceSelector matchingServiceSelector = Context.getMatchingServiceSelector();
			List<String> matchingFunctionNames = matchingServiceSelector.getMatchingServiceNames();
			return matchingFunctionNames;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<String> getRecordLinkageProtocolNames() {
		log.debug("Received request to retrieve the list of record linkage protocol names.");
		try {
			RecordLinkageProtocolSelector recordLinkageProtocolSelector = Context.getRecordLinkageProtocolSelector();
			List<String> recordLinkageProtocolNames = recordLinkageProtocolSelector.getRecordLinkageProtocolNames();
			return recordLinkageProtocolNames;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public void initializeNicknameCache() {
		log.debug("Received request to initialize 'Nickname -> name' and 'Name -> nickname' cache structures.");
		try {
			TransformationService transformationService = Context.getTransformationService();
			TransformationFunctionType transformationFunctionType =
					transformationService.getTransformationFunctionType(NicknameCorruptor.NICKNAME_CORRUPTOR_NAME);
			// A dummy trafo will cause the cache to populate
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(NicknameCorruptor.REPLACE_PROBABILITY_TAG, 1.0d);	// Name 2 Nick and Nick 2 Name creation
			params.put(StringCorruptor.OCR_ERROR_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.PHONETIC_ERROR_PROBABILITY_TAG, 1.0d);	// key ArrayList population
			params.put(StringCorruptor.DELETION_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.INSERTION_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.SUBSTITUTION_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.TRANSPOSITION_PROBABILITY_TAG, 0.0d);
			transformationFunctionType.getTransformationFunction().transform("DUMMY", params);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public void initializeLastnameCache() {
		log.debug("Received request to initialize Lastname cache structures.");
		try {
			TransformationService transformationService = Context.getTransformationService();
			TransformationFunctionType transformationFunctionType =
					transformationService.getTransformationFunctionType(LastnameCorruptor.LASTNAME_CORRUPTOR_NAME);
			// A dummy trafo will cause the cache to populate
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(LastnameCorruptor.HYPHENATE_PROBABILITY_TAG, 0.0d);
			params.put(LastnameCorruptor.MALE_REPLACE_PROBABILITY_TAG, 1.0d);
			params.put(LastnameCorruptor.GENDER_TAG, "M");
			params.put(StringCorruptor.OCR_ERROR_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.PHONETIC_ERROR_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.DELETION_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.INSERTION_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.SUBSTITUTION_PROBABILITY_TAG, 0.0d);
			params.put(StringCorruptor.TRANSPOSITION_PROBABILITY_TAG, 0.0d);
			transformationFunctionType.getTransformationFunction().transform("DUMMY", params);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	protected ApplicationContext getContext() {
		return context;
	}
}
