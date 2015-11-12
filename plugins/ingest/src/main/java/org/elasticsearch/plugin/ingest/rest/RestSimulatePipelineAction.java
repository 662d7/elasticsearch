/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.plugin.ingest.rest;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugin.ingest.transport.simulate.SimulatePipelineAction;
import org.elasticsearch.plugin.ingest.transport.simulate.SimulatePipelineRequest;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.support.RestActions;
import org.elasticsearch.rest.action.support.RestStatusToXContentListener;

public class RestSimulatePipelineAction extends BaseRestHandler {

    @Inject
    public RestSimulatePipelineAction(Settings settings, RestController controller, Client client) {
        super(settings, controller, client);
        controller.registerHandler(RestRequest.Method.POST, "/_ingest/pipeline/{id}/_simulate", this);
        controller.registerHandler(RestRequest.Method.GET, "/_ingest/pipeline/{id}/_simulate", this);
        controller.registerHandler(RestRequest.Method.POST, "/_ingest/pipeline/_simulate", this);
        controller.registerHandler(RestRequest.Method.GET, "/_ingest/pipeline/_simulate", this);
    }

    @Override
    protected void handleRequest(RestRequest restRequest, RestChannel channel, Client client) throws Exception {
        SimulatePipelineRequest request = new SimulatePipelineRequest();
        request.id(restRequest.param("id"));
        request.verbose(restRequest.paramAsBoolean("verbose", false));

        if (RestActions.hasBodyContent(restRequest)) {
            request.source(RestActions.getRestContent(restRequest));
        }

        client.execute(SimulatePipelineAction.INSTANCE, request, new RestStatusToXContentListener<>(channel));
    }
}
