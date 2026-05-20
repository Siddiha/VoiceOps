package journal;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA gives you save(), findAll(), findById() etc for FREE
// You don't need to write any SQL yourself for basic operations
public interface JournalRepository extends JpaRepository<JournalEntry, Long> {
    // That's it! Spring auto-generates all the DB methods
}