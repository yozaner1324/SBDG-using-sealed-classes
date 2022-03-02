/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.geode.boot.autoconfigure.SslAutoConfiguration;
import org.springframework.geode.config.annotation.EnableClusterAware;
import org.springframework.stereotype.Repository;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;

@SpringBootApplication(exclude = {SslAutoConfiguration.class})
@EnableClusterAware
@EnableEntityDefinedRegions
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//	@Bean("Person")
//	public ClientRegionFactoryBean<String, Person> peopleRegion(GemFireCache gemfireCache) {
//
//		ClientRegionFactoryBean<String, Person> exampleRegion = new ClientRegionFactoryBean<>();
//		exampleRegion.setCache(gemfireCache);
//		exampleRegion.setClose(false);
//		exampleRegion.setRegionName("Person");
//		exampleRegion.setShortcut(ClientRegionShortcut.PROXY);
//		exampleRegion.setPersistent(false);
//
//		return exampleRegion;
//	}

	@Bean
	public ApplicationRunner runner(PersonRepository personRepository) {

		return args -> {
			long count = personRepository.count();
			System.out.println("Count: " + count);

			Person jonDoe = new Person("jonDoe");
			personRepository.save(jonDoe);

			count = personRepository.count();
			System.out.println("Count: " + count);

			Person jonDoeFoundById = personRepository.findById(jonDoe.getName()).orElse(null);
			System.out.println(jonDoeFoundById);
		};
	}


}
@Repository
interface PersonRepository extends GemfireRepository<Person, String> {

}

@Region("Person")
class Person{
	@Id
	private String name;
	public Person(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

class Customer extends Person {
	public Customer(String name) {
		super(name);
	}
}
