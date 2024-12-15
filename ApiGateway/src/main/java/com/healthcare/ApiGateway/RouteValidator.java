package com.healthcare.ApiGateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

	// List of unsecured API endpoints that don't require JWT validation
	public static final List<String> openApiEndpoints = List.of(
			"/api/v1/auth/register/doctor",
			"/api/v1/auth/login"
	);

	public Predicate<ServerHttpRequest> isSecured =
			request -> {
				String path = request.getURI().getPath();
				boolean isSecured = openApiEndpoints.stream().noneMatch(uri->path.contains(uri));
				if(!isSecured) {
					System.out.println("Unsecured access:{}"+path);
				}
				System.out.println("&6*^*&^*&UHUD");
				return isSecured;
			};
}
