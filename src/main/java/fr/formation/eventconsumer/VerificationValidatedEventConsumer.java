package fr.formation.eventconsumer;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.formation.enumerator.VerificationEtat;
import fr.formation.model.Verification;
import fr.formation.repo.VerificationRepository;

@Component("onVerificationValidated")
public class VerificationValidatedEventConsumer implements Consumer<String> {
    @Autowired
    private VerificationRepository repository;

    @Override
    public void accept(String verificationId) {
        Optional<Verification> optVerification = this.repository.findById(verificationId);

        if (optVerification.isPresent()) {
            Verification verification = optVerification.get();

            verification.setEtat(VerificationEtat.OK);

            this.repository.save(verification);
        }
    }
}
