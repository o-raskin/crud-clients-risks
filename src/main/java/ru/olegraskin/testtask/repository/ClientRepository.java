package ru.olegraskin.testtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.olegraskin.testtask.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
