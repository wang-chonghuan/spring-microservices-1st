package microservices.book.multiplication.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    // call by frontend  /users/1,2,3
    @GetMapping("/{idList}")
    public List<User> getUserByIdList(@PathVariable final List<Long> idList) {
        return userRepository.findAllByIdIn(idList);
    }
}
