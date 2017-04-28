/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wsj.ms.fuction;

import com.fasterxml.jackson.core.io.UTF8Writer;
import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser;
import com.wsj.ms.fuction.repository.DummyPersonRepository;
import com.wsj.ms.fuction.repository.DummyProductRepository;
import com.wsj.ms.fuction.repository.PersonRepository;
import com.wsj.ms.fuction.repository.ProductRepository;
import com.wsj.ms.fuction.service.PersonHandler;
import com.wsj.ms.fuction.service.ProductHandler;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import reactor.ipc.netty.http.server.HttpServer;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;
/**
 * @description 
 *
 * @author kayson Yang
 * @create 
 */
public class Server {

	public static final String HOST = "localhost";

	public static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		//server.startReactorServer();
		server.startTomcatServer();

		System.out.println("Press ENTER to exit.");
		System.in.read();
	}

	public RouterFunction<ServerResponse> routingFunction() {
		PersonRepository repository = new DummyPersonRepository();
		ProductRepository repository2 = new DummyProductRepository();
		PersonHandler handler = new PersonHandler(repository);
		ProductHandler handler2 = new ProductHandler(repository2);

		return nest(path("/person"),
				   nest(   accept(APPLICATION_JSON),
						   route(GET("/{id}"), handler::getPerson)
						   .andRoute(method(HttpMethod.GET), handler::listPeople)
				        )
					.andRoute( POST("/")
							   .and(contentType(APPLICATION_JSON)),
							handler::createPerson))

				.andNest(path("/product"),nest(
						accept(APPLICATION_JSON),
						route(GET("/{id}"), handler2::getProduct)
								.andRoute(method(HttpMethod.GET), handler2::listProduct)
				).andRoute( POST("/p")
								.and(contentType(APPLICATION_JSON)),
						handler2::createProduct)
				);
	}



	public void startReactorServer() throws InterruptedException {
		RouterFunction<ServerResponse> route = routingFunction();
		HttpHandler httpHandler = toHttpHandler(route);

		ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
		HttpServer server = HttpServer.create(HOST, PORT);
		server.newHandler(adapter).block();
	}

	public void startTomcatServer() throws LifecycleException {
		RouterFunction<?> route = routingFunction();
		HttpHandler httpHandler = toHttpHandler(route);

		Tomcat tomcatServer = new Tomcat();
		tomcatServer.setHostname(HOST);
		tomcatServer.setPort(PORT);
		Context rootContext = tomcatServer.addContext("", System.getProperty("java.io.tmpdir"));
		ServletHttpHandlerAdapter servlet = new ServletHttpHandlerAdapter(httpHandler);
		Tomcat.addServlet(rootContext, "httpHandlerServlet", servlet);
		rootContext.addServletMapping("/", "httpHandlerServlet");
		tomcatServer.start();
	}

}
