package kz.gov.example.esutd.soap.util.digital;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.security.PrivateKey;

@Getter
@RequiredArgsConstructor
public class KeyStoreWrapper {
    private final PrivateKey privateKey;
    private final CertificateWrapper certificate;
} 