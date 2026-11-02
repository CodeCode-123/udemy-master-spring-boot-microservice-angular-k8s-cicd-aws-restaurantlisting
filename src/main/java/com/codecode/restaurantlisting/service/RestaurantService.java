package com.codecode.restaurantlisting.service;

import com.codecode.restaurantlisting.dto.RestaurantDTO;
import com.codecode.restaurantlisting.entity.Restaurant;
import com.codecode.restaurantlisting.mapper.RestaurantMapper;
import com.codecode.restaurantlisting.repository.RestaurantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RestaurantService {
    private final RestaurantRepo restaurantRepo;

    @Autowired
    public RestaurantService(RestaurantRepo restaurantRepo) {
        this.restaurantRepo = restaurantRepo;
    }

    public List<RestaurantDTO> findAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepo.findAll();
        List<RestaurantDTO> restaurantDTOList = restaurants.stream().map(restaurant ->
                        RestaurantMapper.INSTANCE
                                .mapRestaurantToRestaurantDTO(restaurant)).toList();
        return restaurantDTOList;
    }

    public RestaurantDTO addRestaurantInDB(RestaurantDTO restaurantDTO) {
        Restaurant savedRestaurant = restaurantRepo.save(RestaurantMapper.INSTANCE
                .mapRestaurantDTOToRestaurant(restaurantDTO));
        return RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(savedRestaurant);
    }

    public ResponseEntity<RestaurantDTO> fetchRestaurantById(Integer id) {
        Optional<Restaurant> restaurant = restaurantRepo.findById(id);
        if (restaurant.isPresent()) {
            return new ResponseEntity<>(RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(restaurant.get()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
