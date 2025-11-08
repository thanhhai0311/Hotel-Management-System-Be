package com.javaweb.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.converter.ShiftingConverter;
import com.javaweb.model.dto.ShiftingDTO.*;
import com.javaweb.model.entity.*;
import com.javaweb.repository.*;
import com.javaweb.service.ShiftingService;

@Service
public class ShiftingServiceImpl implements ShiftingService {

	@Autowired
	private ShiftingRepository shiftingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShiftRepository shiftRepository;

	@Autowired
	private ShiftingConverter converter;

	@Override
	public ShiftingResponseDTO create(ShiftingCreateDTO dto) {
		ShiftingEntity entity = new ShiftingEntity();
		entity.setDetails(dto.getDetails());
		entity.setDay(dto.getDay());

		UserEntity employee = userRepository.findById(dto.getIdEmployee())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên"));
		ShiftEntity shift = shiftRepository.findById(dto.getIdShift())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ca làm"));

		entity.setEmployee(employee);
		entity.setShift(shift);

		ShiftingEntity saved = shiftingRepository.save(entity);
		return converter.toResponseDTO(saved);
	}

	@Override
	public ShiftingResponseDTO update(Integer id, ShiftingUpdateDTO dto) {
		ShiftingEntity entity = shiftingRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phân ca"));

		if (dto.getDetails() != null)
			entity.setDetails(dto.getDetails());
		if (dto.getDay() != null)
			entity.setDay(dto.getDay());

		if (dto.getIdEmployee() != null) {
			UserEntity emp = userRepository.findById(dto.getIdEmployee())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên"));
			entity.setEmployee(emp);
		}

		if (dto.getIdShift() != null) {
			ShiftEntity shift = shiftRepository.findById(dto.getIdShift())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ca làm"));
			entity.setShift(shift);
		}

		return converter.toResponseDTO(shiftingRepository.save(entity));
	}

	@Override
	public void delete(Integer id) {
		ShiftingEntity entity = shiftingRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phân ca"));
		shiftingRepository.delete(entity);
	}

	@Override
	public ShiftingResponseDTO getById(Integer id) {
		ShiftingEntity entity = shiftingRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phân ca"));
		return converter.toResponseDTO(entity);
	}

	@Override
	public Page<ShiftingResponseDTO> search(Integer idEmployee, Integer idShift, LocalDate day, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("day").descending());

		Specification<ShiftingEntity> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (idEmployee != null) {
				predicates.add(cb.equal(root.get("employee").get("id"), idEmployee));
			}

			if (idShift != null) {
				predicates.add(cb.equal(root.get("shift").get("id"), idShift));
			}

			if (day != null) {
				predicates.add(cb.equal(root.get("day"), day));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		Page<ShiftingEntity> result = shiftingRepository.findAll(spec, pageable);
		return result.map(converter::toResponseDTO);
	}

	@Override
	public List<ShiftingResponseDTO> searchAll(Integer idEmployee, Integer idShift, LocalDate day) {
		Specification<ShiftingEntity> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (idEmployee != null) {
				predicates.add(cb.equal(root.get("employee").get("id"), idEmployee));
			}

			if (idShift != null) {
				predicates.add(cb.equal(root.get("shift").get("id"), idShift));
			}

			if (day != null) {
				predicates.add(cb.equal(root.get("day"), day));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		return shiftingRepository.findAll(spec, Sort.by("day").descending()).stream().map(converter::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<ShiftingResponseDTO> getAll() {
		return shiftingRepository.findAll(Sort.by("day").descending()).stream().map(converter::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Override
	public Page<ShiftingResponseDTO> getAllPaged(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("day").descending());
		Page<ShiftingEntity> result = shiftingRepository.findAll(pageable);
		return result.map(converter::toResponseDTO);
	}

}
