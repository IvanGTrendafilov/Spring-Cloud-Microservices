package com.example.reservationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Collection;


@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @RestResource(path = "by-name")
    Collection<Reservation> findByReservationName(@Param(value = "rn") String rn);

}

@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(final ReservationRepository reservationRepository) {
        return args -> {
            Arrays.asList("Ivan Trendafilov, Sidney El Agib, Sunny Verma, All of shared services".split(","))
                    .forEach(x -> reservationRepository.save(new Reservation(x)));
            reservationRepository.findAll().forEach(System.out::println);
        };
    }
}

@RefreshScope
@RestController
class DescriptionRestController {

    @Value("${info.description}")
    private String description;

    @RequestMapping(path = "/description")
    String getDescription() {
        return this.description;
    }
}

@Entity
class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private String reservationName;

    Reservation() {

    }

    public Reservation(final String reservationName) {
        this.reservationName = reservationName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getReservationName() {
        return this.reservationName;
    }

    public void setReservationName(final String reservationName) {
        this.reservationName = reservationName;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + this.id +
                ", reservationName='" + this.reservationName + '\'' +
                '}';
    }
}

