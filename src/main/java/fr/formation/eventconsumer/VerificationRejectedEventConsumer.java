package fr.formation.eventconsumer;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.formation.repository.VerificationRepository;

@Component("onVerificationRejected")
public class VerificationRejectedEventConsumer implements Consumer<String> {
    @Autowired
    private VerificationRepository repository;

    @Override
    public void accept(String verificationId) {
        this.repository.deleteById(verificationId);
    }
}
