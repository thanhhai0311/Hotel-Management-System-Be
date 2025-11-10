package com.javaweb.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.converter.RoleConverter;
import com.javaweb.model.dto.RoleDTO.RoleDTO;
import com.javaweb.model.dto.RoleDTO.RoleResponseDTO;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.repository.RoleRepository;
import com.javaweb.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RoleConverter roleConverter;
    
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public RoleResponseDTO createRole(RoleDTO dto) {
    	if (roleRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Tên Role '" + dto.getName() + "' đã tồn tại. Vui lòng chọn tên khác.");
        }
        RoleEntity entity = roleConverter.toEntity(dto);
        RoleEntity savedEntity = roleRepository.save(entity);
        return roleConverter.toResponseDTO(savedEntity);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleConverter::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDTO getRoleById(Integer id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Không tìm thấy Role với ID = " + id));
        return roleConverter.toResponseDTO(role);
    }

    @Override
    public RoleResponseDTO updateRole(Integer id, RoleDTO dto) {
        RoleEntity existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Không tìm thấy Role với ID = " + id));

        if (dto.getName() != null && !dto.getName().equals(existingRole.getName())) {
            if (roleRepository.existsByName(dto.getName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                        "Tên Role '" + dto.getName() + "' đã tồn tại cho một Role khác.");
            }
        }
        
        RoleEntity updatedEntity = roleConverter.toEntity(existingRole, dto);
        
        RoleEntity savedEntity = roleRepository.save(updatedEntity);
        return roleConverter.toResponseDTO(savedEntity);
    }

//    @Override
//    @Transactional
//    public void deleteRole(Integer id) {
//        RoleEntity role = roleRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
//                        "Không tìm thấy Role với ID = " + id));
//        
//        // TODO: Cần xử lý mối quan hệ OneToMany với AccountEntity trước khi xóa.
//        // Ví dụ: set role_id = null hoặc xóa AccountEntity liên quan.
//        // Tạm thời, giả định Cascade delete đã được cấu hình hoặc không có Account nào dùng role này.
//        
//        roleRepository.delete(role);
//    }
    
    @Override
    @Transactional
    public void deleteRole(Integer id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Không tìm thấy Role với ID = " + id));

        List<AccountEntity> accountsToUpdate = role.getAccountEntity();

        if (accountsToUpdate != null && !accountsToUpdate.isEmpty()) {
            for (AccountEntity account : accountsToUpdate) {
                account.setRole(null); 
            }
            accountRepository.saveAll(accountsToUpdate);
        }
        roleRepository.delete(role);
    }
}