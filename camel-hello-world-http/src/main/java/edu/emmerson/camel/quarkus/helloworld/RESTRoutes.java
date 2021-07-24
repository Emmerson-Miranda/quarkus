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
package edu.emmerson.camel.quarkus.helloworld;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.opentracing.TagProcessor;

public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        /*
        from("netty-http:0.0.0.0:8099/greeting")
                .routeId("greeting")
                .to("direct:welcome-route");
        */
        from("direct:welcome-route")
                .routeId("processGreeting")
                .delay(simple("${random(1000, 2000)}"))
                .log("Welcome ${header.x-correlation-id}") //show up in opentracing logs
                .setBody(constant("Hello World - greetings!"));

        from("direct:bye-route")
                .routeId("processGoodby")
                .delay(simple("${random(1000, 2000)}"))
                .log("Bye ${header.x-correlation-id}") //show up in opentracing logs
                .setBody(constant("Hello World - goodbye!"));

        from("direct:something-route")
                .routeId("processDoSomething")
                .delay(simple("${random(1000, 2000)}"))
                .log("Doing something with ${header.x-correlation-id}") //show up in opentracing logs
                .setBody(constant("Hello World - doSomething"));

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/greeting")
                .get()
                .route()
                .routeId("processGreeting2GET")
                .process(new TagProcessor("myCorrelationId", simple("${header.x-correlation-id}")))
                .log("Calling welcome route ")
                .to("direct:welcome-route")
                .log("Calling bye route ")
                .to("direct:bye-route")
                .endRest()

                .post()
                .route()
                .routeId("processGreeting2POST")
                .process(new TagProcessor("myCorrelationId", simple("${header.x-correlation-id}")))
                .log("Calling welcome route ")
                .to("direct:welcome-route")
                .log("Calling something route ")
                .to("direct:something-route")
                .log("Calling bye route ")
                .to("direct:bye-route")
                .endRest();

        rest("/getgoogle")
                .get()
                .route()
                .routeId("getGoogle")
                .process(new TagProcessor("myCorrelationId", simple("${header.x-correlation-id}")))
                .log("Getting google home page")
                .removeHeader(Exchange.HTTP_PATH)
                //.setHeader(Exchange.HTTP_URI, constant("https://www.google.co.uk"))
                //.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
                .to("http://www.google.com?bridgeEndpoint=true")
                .endRest();

    }
}
