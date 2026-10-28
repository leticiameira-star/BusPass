package com.onbius.busspass.repository;

import com.onbius.busspass.entity.Linha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinhaRepository extends JpaRepository<Linha, Long> {
}
