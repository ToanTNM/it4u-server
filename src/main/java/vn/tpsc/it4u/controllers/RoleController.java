package vn.tpsc.it4u.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import vn.tpsc.it4u.models.Role;
import vn.tpsc.it4u.repository.RoleRepository;
import vn.tpsc.it4u.utils.ApiResponseUtils;

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
	public String get(Locale locale) {
		// List<Role> roles = repository.findAll();
		JSONObject getRoles = new JSONObject(ResponseEntity.ok(repository.findAll()));
		JSONArray getBody = getRoles.getJSONArray("body");
		return getBody.toString();
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody Role role, Locale locale) {
		repository.save(role);

		return ResponseEntity.ok(apiResponse.success("Role inserted"));
	}
}