package com.bio.clinic.repositories;
import com.bio.clinic.entities.Medico; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}