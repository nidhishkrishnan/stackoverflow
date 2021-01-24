package com.employee.service;

import com.employee.domain.UserDTO;
import com.employee.mapper.UserMapper;
import com.employee.model.User;
import com.employee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.employee.mapper.UserMapper.mapToUserDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> findAll() {
        return buildUserDto(userRepository.findAll());
    }

    private List<UserDTO> buildUserDto(List<User> users) {
        return users.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) throws RuntimeException {
        return mapToUserDto(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public void save(UserDTO user) {
        userRepository.save(buildUser(user));
    }

    private User buildUser(UserDTO userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        return user;
    }
}
