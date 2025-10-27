package org.travy.Springstarter.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.travy.Springstarter.models.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
}
