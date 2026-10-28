package com.wallet.wallettracker.repository;
import com.wallet.wallettracker.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
