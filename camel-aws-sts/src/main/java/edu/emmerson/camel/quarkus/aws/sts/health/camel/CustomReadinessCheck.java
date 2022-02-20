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
package edu.emmerson.camel.quarkus.aws.sts.health.camel;

import java.util.Map;

import org.apache.camel.health.HealthCheckResultBuilder;
import org.apache.camel.impl.health.AbstractHealthCheck;

/**
 * A simple custom liveness check which utilizes the Camel Health API.
 * 
 * This example is based on https://camel.apache.org/camel-quarkus/latest/user-guide/examples.html
 * 
 */
public class CustomReadinessCheck extends AbstractHealthCheck {

    public CustomReadinessCheck() {
        super("custom-readiness-check");
        getConfiguration().setEnabled(true);
    }

    @Override
    protected void doCall(HealthCheckResultBuilder builder, Map<String, Object> options) {
        builder.up();
    }

    @Override
    public boolean isLiveness() {
        return false;
    }
}