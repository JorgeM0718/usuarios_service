package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.request.ClienteRequest;
import com.pragma.usuarios.application.dto.request.EmpleadoRequest;
import com.pragma.usuarios.application.dto.request.PropietarioRequest;
import com.pragma.usuarios.application.dto.response.UsuarioResponse;
import com.pragma.usuarios.application.dto.response.UsuarioRolResponse;
import com.pragma.usuarios.application.handler.UsuarioHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios")
public class UsuarioRestController {

    private final UsuarioHandler usuarioHandler;

    @PostMapping("/propietarios")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear propietario", description = "Solo administrador. Debe ser mayor de edad.")
    public ResponseEntity<UsuarioResponse> crearPropietario(@Valid @RequestBody PropietarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioHandler.crearPropietario(request));
    }

    @PostMapping("/empleados")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear empleado", description = "Solo propietario.")
    public ResponseEntity<UsuarioResponse> crearEmpleado(@Valid @RequestBody EmpleadoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioHandler.crearEmpleado(request));
    }

    @PostMapping("/clientes")
    @Operation(summary = "Crear cliente", description = "Público.")
    public ResponseEntity<UsuarioResponse> crearCliente(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioHandler.crearCliente(request));
    }

    @GetMapping("/{idUsuario}/rol")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Consultar rol", description = "Uso interno entre microservicios.")
    public ResponseEntity<UsuarioRolResponse> obtenerRol(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(usuarioHandler.obtenerRol(idUsuario));
    }
}
