package com.darshan.demo.resource;

import java.util.Arrays;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.darshan.demo.model.CatalogItem;
import com.darshan.demo.model.Movie;
import com.darshan.demo.model.Rating;
import com.darshan.demo.model.UserRating;


@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	WebClient.Builder webClientBulder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId){

		UserRating ratings = restTemplate.getForObject("http://movie-rating-data/ratingsdata/users/" + userId, UserRating.class);

		return ratings.getUserRating().stream().map(rating ->{

			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

			
			return new CatalogItem(movie.getName(), "Test Desc", rating.getRating());
		})
				.collect(Collectors.toList());

	}
}

/*
 * Movie movie = webClientBulder.build() .get()
 * .uri("http://localhost:8082/movies/" + rating.getMovieId()) .retrieve()
 * .bodyToMono(Movie.class) .block();
 */

