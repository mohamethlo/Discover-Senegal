package sn.discover.discoversenegal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.discover.discoversenegal.entities.Profile;
import sn.discover.discoversenegal.entities.User;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUser(User user);

    Optional<Profile> findByUser_Id(Long userId);
}