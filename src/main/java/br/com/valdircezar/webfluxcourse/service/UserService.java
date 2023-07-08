package br.com.valdircezar.webfluxcourse.service;

import br.com.valdircezar.webfluxcourse.entity.User;
import br.com.valdircezar.webfluxcourse.mapper.UserMapper;
import br.com.valdircezar.webfluxcourse.model.request.UserRequest;
import br.com.valdircezar.webfluxcourse.repository.UserReposirory;
import br.com.valdircezar.webfluxcourse.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReposirory reposirory;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request){
        return reposirory.save(mapper.toEntity(request));
    }

    public Mono<User> findById(final String id){
        return reposirory.findById(id)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException(
                        format("Object not found. Id: %s Type: %s", id, User.class.getSimpleName())
                )));
    }

    public Flux<User> finAll(){
        return reposirory.findAll();
    }
}
