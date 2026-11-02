package com.codecode.restaurantlisting.mapper;

import com.codecode.restaurantlisting.dto.RestaurantDTO;
import com.codecode.restaurantlisting.entity.Restaurant;
import org.springframework.beans.BeanUtils;

public class RestaurantMapperImpl implements RestaurantMapper {
    @Override
    public Restaurant mapRestaurantDTOToRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = new Restaurant();
        BeanUtils.copyProperties(restaurantDTO, restaurant);
        return restaurant;
    }

    @Override
    public RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        BeanUtils.copyProperties(restaurant, restaurantDTO);
        return restaurantDTO;
    }
}
