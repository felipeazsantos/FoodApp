package dev.felipeazsantos.FoodApp.role.services;

import dev.felipeazsantos.FoodApp.response.Response;
import dev.felipeazsantos.FoodApp.role.dtos.RoleDTO;

import java.util.List;

public interface RoleService {

    Response<RoleDTO> createRole(RoleDTO roleDTO);

    Response<RoleDTO> updateRole(RoleDTO roleDTO);

    Response<List<RoleDTO>> getAllRoles();

    Response<?> deleteRole(Long id);
}
