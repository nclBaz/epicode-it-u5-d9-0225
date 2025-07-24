package riccardogulin.u5d9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import riccardogulin.u5d9.entities.User;
import riccardogulin.u5d9.exceptions.ValidationException;
import riccardogulin.u5d9.payloads.NewUserDTO;
import riccardogulin.u5d9.payloads.NewUserRespDTO;
import riccardogulin.u5d9.services.UsersService;

import java.util.UUID;

/*

1. GET http://localhost:3001/users
2. POST http://localhost:3001/users (+req.body)
3. GET http://localhost:3001/users/{userId}
4. PUT http://localhost:3001/users/{userId} (+req.body)
5. DELETE http://localhost:3001/users/{userId}

*/

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UsersService usersService;

	@GetMapping
	public Page<User> findAll(@RequestParam(defaultValue = "0") int page,
	                          @RequestParam(defaultValue = "10") int size,
	                          @RequestParam(defaultValue = "id") String sortBy
	) {
		// Posso mettere dei valori di default per i query params, per far si che non ci siano errori se il client non li imposta
		return this.usersService.findAll(page, size, sortBy);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public NewUserRespDTO save(@RequestBody @Validated NewUserDTO payload, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			//validationResult.getFieldErrors().forEach(fieldError -> System.out.println(fieldError.getDefaultMessage()));
			throw new ValidationException(validationResult.getFieldErrors()
					.stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
		} else {
			User newUser = this.usersService.save(payload);
			return new NewUserRespDTO(newUser.getId());
		}

	}

	@GetMapping("/{userId}")
	public User getById(@PathVariable UUID userId) {
		return this.usersService.findById(userId);
	}

	@PutMapping("/{userId}")
	public User getByIdAndUpdate(@PathVariable UUID userId, @RequestBody NewUserDTO payload) {
		return this.usersService.findByIdAndUpdate(userId, payload);
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void getByIdAndDelete(@PathVariable UUID userId) {
		this.usersService.findByIdAndDelete(userId);
	}

	@PatchMapping("/{userId}/avatar")
	public String uploadImage(@RequestParam("avatar") MultipartFile file) {
		// "avatar" deve corrispondere ESATTAMENTE al campo del FormData nel quale il frontend inserirà l'immagine
		// Se non corrisponde non troverò il file
		System.out.println(file.getOriginalFilename());
		System.out.println(file.getSize());
		return this.usersService.uploadAvatar(file);

	}
}
