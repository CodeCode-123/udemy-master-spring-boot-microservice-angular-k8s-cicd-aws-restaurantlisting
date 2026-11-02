package com.codecode.restaurantlisting.controller;

import com.codecode.restaurantlisting.dto.RestaurantDTO;
import com.codecode.restaurantlisting.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestaurantControllerTest {
    @Mock
    RestaurantService restaurantService;

    @InjectMocks
    RestaurantController restaurantController;

    @BeforeEach
    public void setup() {
        //in order for Mock and InjectMocks annotation
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchAllRestaurants() {
        //Mock the service behavior
        List<RestaurantDTO> mockRestaurants = Arrays.asList(
                new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1"),
                new RestaurantDTO(1, "Restaurant 2", "Address 2", "city 2", "Desc 2")
        );
        when(restaurantService.findAllRestaurants()).thenReturn(mockRestaurants);

        //Call the response
        ResponseEntity<List<RestaurantDTO>> response = restaurantController.fetchAllRestaurants();
        //Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRestaurants, response.getBody());
        //Verify that the service method was called
        verify(restaurantService, times(1)).findAllRestaurants();
    }

    @Test
    public void testSaveRestaurant() {
        //Create a mock restaurant to be saved
        RestaurantDTO mockRestaurant = new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");

        //Mock the service behavior
        when(restaurantService.addRestaurantInDB(mockRestaurant)).thenReturn(mockRestaurant);

        //Call the controller class
        ResponseEntity<RestaurantDTO> response = restaurantController.saveRestaurant(mockRestaurant);

        //Verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockRestaurant, response.getBody());

        //Verify that the service method was called
        //If multiple different objects were used, any() can be used to replace mockRestaurant
        verify(restaurantService, times(1)).addRestaurantInDB(mockRestaurant);
    }


    @Test
    public void testFetchRestaurantById() {
        //Create a mock restaurant ID
        Integer mockRestaurantId = 1;

        //Create a mock restaurant to be returned by the service
        RestaurantDTO mockRestaurant = new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");

        //Mock the service behavior
        when(restaurantService.fetchRestaurantById(mockRestaurantId)).thenReturn(new ResponseEntity<>(mockRestaurant, HttpStatus.OK));

        //Call the controller class
        ResponseEntity<RestaurantDTO> response = restaurantController.findRestaurantById(mockRestaurantId);

        //Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRestaurant, response.getBody());

        //Verify that the service method was called
        //If multiple different Integers were used, anyInt() can replace the mockRestaurantId
        verify(restaurantService, times(1)).fetchRestaurantById(mockRestaurantId);
    }
}
