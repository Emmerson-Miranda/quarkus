/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emmerson.camel.quarkus.kafka.health.microprofile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

/**
 * A simple CamelContext uptime readiness check which implements the MicroProfile Health API.
 * 
 * This example is based on https://camel.apache.org/camel-quarkus/latest/user-guide/examples.html
 * 
 */
@Readiness
@ApplicationScoped
public class CamelUptimeHealthCheck implements HealthCheck {

    @Inject
    CamelContext camelContext;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.named("Uptime readiness check");

        if (camelContext.getUptimeMillis() > 0) {
            builder.up();
        } else {
            builder.down();
        }

        return builder.build();
    }
}