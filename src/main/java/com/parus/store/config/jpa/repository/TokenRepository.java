package com.parus.store.config.jpa.repository;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parus.store.model.Token;
import com.parus.store.model.TokenName;
import com.parus.store.model.User;
import java.lang.String;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
	@Query("SELECT t FROM TOKEN t WHERE t.tokenpurpose=:tokenName and t.user=:user")
	Optional<Token> findByUserWithTokenPurpose(@Param("user")User user,@Param("tokenName")TokenName tokenName);

	@Query("SELECT t FROM TOKEN t WHERE t.value=:tokenValue and t.user=:user")
	Optional<Token> findByValueandEmail(@Param("user")User user,@Param("tokenValue")String tokenValue);
}
