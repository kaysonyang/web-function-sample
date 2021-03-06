/*
 * Copyright 2002-2016 the original author or authors.
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

package com.wsj.ms.fuction.repository;

import com.wsj.ms.fuction.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * @description 
 *
 * @author kayson Yang
 * @create 
 */
public interface PersonRepository {

	Mono<Person> getPerson(int id);

	Flux<Person> allPeople();

	Mono<Void> savePerson(Mono<Person> person);

}
