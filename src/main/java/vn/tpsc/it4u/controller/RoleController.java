package vn.tpsc.it4u.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import vn.tpsc.it4u.model.Role;
import vn.tpsc.it4u.repository.RoleRepository;
import vn.tpsc.it4u.util.ApiResponseUtils;

/**
 * RoleRepository
 */
@RestController
@RequestMapping("${app.api.version}/role")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

	@Autowired
	private RoleRepository repository;

	@Autowired
	private ApiResponseUtils apiResponse;

	@GetMapping()
	public ResponseEntity<?> get(Locale locale) {
		List<Role> roles = repository.findAll();

		return ResponseEntity.ok(apiResponse.success(roles, locale));
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody Role role, Locale locale) {
		repository.save(role);

		return ResponseEntity.ok(apiResponse.success("Role inserted"));
	}
}