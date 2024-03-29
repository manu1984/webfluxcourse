package br.com.valdircezar.webfluxcourse.service;

import br.com.valdircezar.webfluxcourse.entity.User;
import br.com.valdircezar.webfluxcourse.mapper.UserMapper;
import br.com.valdircezar.webfluxcourse.model.request.UserRequest;
import br.com.valdircezar.webfluxcourse.repository.UserReposirory;
import br.com.valdircezar.webfluxcourse.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserReposirory reposirory;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;
    @Test
    void testSave() {
        UserRequest request = new UserRequest("valdir","valdir@mail.com","123");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(reposirory.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = service.save(request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(reposirory, times(1)) .save(any(User.class));

    }

    @Test
    void testFindById(){
        when(reposirory.findById(anyString())).thenReturn(Mono.just(User.builder()
                        .id("1234")
                .build()));

        Mono<User> result = service.findById("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class
                && user.getId() == "1234")
                .expectComplete()
                .verify();

        Mockito.verify(reposirory, times(1)) .findById(anyString());

    }

    @Test
    void testFindAll(){
        when(reposirory.findAll()).thenReturn(Flux.just(User.builder()
                .id("1234")
                .build()));

        Flux<User> result = service.finAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(reposirory, times(1)) .findAll();

    }

    @Test
    void testUpdate(){
        UserRequest request = new UserRequest("valdir","valdir@mail.com","123");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class), any(User.class))).thenReturn(entity);
        when(reposirory.findById(anyString())).thenReturn(Mono.just(entity));
        when(reposirory.save(any(User.class))).thenReturn(Mono.just(entity));

        Mono<User> result = service.update("123", request);

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(reposirory, times(1)) .save(any(User.class));
    }

    @Test
    void testDelete(){
        User entity = User.builder().build();
        when(reposirory.findAndRemove(anyString())).thenReturn(Mono.just(entity));

        Mono<User> result = service.delete("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getClass() == User.class)
                .expectComplete()
                .verify();

        Mockito.verify(reposirory, times(1)) .findAndRemove(anyString());

    }

    @Test
    void testHandleNotFound(){
        when(reposirory.findById(anyString())).thenReturn(Mono.empty());

        try{
            service.findById("123").block();
        }catch (Exception ex){
            Assertions.assertEquals(ObjectNotFoundException.class, ex.getClass());
            Assertions.assertEquals(format("Object not found. Id: %s Type: %s", "123", User.class.getSimpleName()), ex.getClass());
        }
    }
}