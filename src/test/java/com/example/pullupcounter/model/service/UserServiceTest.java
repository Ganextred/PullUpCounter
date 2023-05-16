//package com.example.pullupcounter.model.service;
//
//import com.example.pullupcounter.data.entity.Game;
//import com.example.pullupcounter.data.entity.InGameAccount;
//import com.example.pullupcounter.data.entity.User;
//import com.example.pullupcounter.data.repository.InGameAccountRepository;
//import com.example.pullupcounter.data.repository.UserRepository;
//import com.example.pullupcounter.model.ApiAccessException;
//import com.example.pullupcounter.model.enums.GameName;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//    @Mock
//    private UserRepository userRepo;
////    @Mock
////    private InGameAccountRepository lastMatchRepo;
////    @Mock
////    GameDriverFactory gameDriverFactory;
//    @InjectMocks
//    private UserService exampleService;
//
//
//    @Test
//    void testUpdateAllCounters() throws ApiAccessException {
//        // Mock data
//        User user1 = new User();
//        user1.setUsername("user1");
//
//        Game game1 = new Game();
//        game1.setName(GameName.LeagueOfLegends.name());
//
//        InGameAccount account1 = new InGameAccount();
//        account1.setGame(game1);
//        account1.setInGameAccountName("account1");
//
//        user1.setInGameAccountSet(Collections.singletonList(account1));
//
//        List<User> userList = Collections.singletonList(user1);
//
//        // Set up mock behavior
//        when(userRepo.findAll()).thenReturn(userList);
//        when(exampleService.updateGameHistory(user1, GameName.LeagueOfLegends)).thenReturn(5);
//
//        // Perform the method call
//        List<String> result = exampleService.updateAllCounters();
//
//        // Verify the interactions and assertions
//        verify(userRepo, times(1)).findAll();
//        verify(exampleService, times(1)).updateGameHistory(eq(user1), any(GameName.class));
//        assertEquals(1, result.size());
//        assertEquals("user1 5 смертей у грі game1 на акаунті account1", result.get(0));
//    }
//}