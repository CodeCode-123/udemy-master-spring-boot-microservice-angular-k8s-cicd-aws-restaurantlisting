package com.codecode.restaurantlisting.service;

import com.codecode.restaurantlisting.dto.RestaurantDTO;
import com.codecode.restaurantlisting.entity.Restaurant;
import com.codecode.restaurantlisting.mapper.RestaurantMapper;
import com.codecode.restaurantlisting.repository.RestaurantRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class RestaurantServiceTest {
    @Mock
    RestaurantRepo restaurantRepo;

    @InjectMocks
    RestaurantService restaurantService;

    @BeforeEach
    public void setUp() {
        //in order for Mock and InjectMocks annotations to take effect, you need to call MockitoAnnotations.openMocks(this);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllRestaurants() {
        //Create mock restaurants
        List<Restaurant> mockRestaurants = Arrays.asList(
                new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1"),
                new Restaurant(2, "Restaurant 2", "Address 2", "city 2", "Desc 2")
        );
        when(restaurantRepo.findAll()).thenReturn(mockRestaurants);
        //Call the service method
        List<RestaurantDTO> restaurantDTOList = restaurantService.findAllRestaurants();
        //Verify the result
        assertEquals(mockRestaurants.size(), restaurantDTOList.size());
        for (int i = 0; i < mockRestaurants.size(); i++) {
            RestaurantDTO tempDTO = RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(mockRestaurants.get(i));
            assertEquals(tempDTO, restaurantDTOList.get(i));
        }

        //Verify that the repository method was called
        verify(restaurantRepo, times(1)).findAll();
    }

    @Test
    public void testAddRestaurantInDB() {

        RestaurantDTO mockRestaurantDTOEnter = new RestaurantDTO("Restaurant 1", "Address 1", "city 1", "Desc 1");
        Restaurant mockRestaurantEnter = RestaurantMapper.INSTANCE.mapRestaurantDTOToRestaurant(mockRestaurantDTOEnter);
        when(restaurantRepo.save(mockRestaurantEnter)).thenReturn(mockRestaurantEnter);
        RestaurantDTO savedRestaurantDTOEnter = restaurantService.addRestaurantInDB(mockRestaurantDTOEnter);
        assertEquals(mockRestaurantDTOEnter, savedRestaurantDTOEnter);
        verify(restaurantRepo, times(1)).save(mockRestaurantEnter);

        //Create a mock restaurant to be saved
        RestaurantDTO mockRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
        Restaurant mockRestaurant = RestaurantMapper.INSTANCE.mapRestaurantDTOToRestaurant(mockRestaurantDTO);
        when(restaurantRepo.save(mockRestaurant)).thenReturn(mockRestaurant);
        RestaurantDTO savedRestaurantDTO = restaurantService.addRestaurantInDB(mockRestaurantDTO);
        assertEquals(mockRestaurantDTO, savedRestaurantDTO);
        verify(restaurantRepo, times(1)).save(mockRestaurant);

        Restaurant restaurant = new Restaurant("Restaurant 1", "Address 1", "city 1", "Desc 1");
        restaurant.setId(1);
        assertEquals(restaurant, mockRestaurant);
    }

    @Test
    public void testFetchRestaurantById_ExistingId() {
        //Create a mock restaurant ID
        Integer mockRestaurantId = 1;
        //Create a mock restaurant to be returned by the repository
        Restaurant mockRestaurant = new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
        //Mock the repository behavior
        when(restaurantRepo.findById(mockRestaurantId)).thenReturn(Optional.of(mockRestaurant));
        //Call the service
        ResponseEntity<RestaurantDTO> response = restaurantService.fetchRestaurantById(mockRestaurantId);
        //Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(mockRestaurant.getId(), response.getBody().getId());

        verify(restaurantRepo, times(1)).findById(mockRestaurantId);
    }

    @Test
    public void testFetchRestaurantById_NonExistingId() {
        //Create a mock restaurant ID
        Integer mockRestaurantId = 1;
        //Mock the repository behavior
        when(restaurantRepo.findById(mockRestaurantId)).thenReturn(Optional.empty());
        //Call the service method
        ResponseEntity<RestaurantDTO> response = restaurantService.fetchRestaurantById(mockRestaurantId);
        //Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(restaurantRepo, times(1)).findById(mockRestaurantId);
    }

}
