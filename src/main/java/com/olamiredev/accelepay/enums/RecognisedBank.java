package com.olamiredev.accelepay.enums;

import java.util.Optional;

public enum RecognisedBank {

    GTB("GTB"), UBA("UBA"), ACCESS("Access"), ZENITH("Zenith"), FIRST_BANK("First Bank"), FCMB("FCMB"),
    POLARIS("Polaris"), STERLING("Sterling"), FIDELITY("Fidelity"), UNION("Union"),
    WEMA("Wema"), STANBIC("Stanbic"), KEYSTONE("KeyStone"), UNITY("Unity"), HERITAGE("Heritage"),
    JAIZ("Jaiz"), SUNTRUST("SunTrust"), PROVIDUS("Providus"), TITAN("Titan"),
    GLOBUS("Globus"), CORONATION("Coronation"),  ECOBANK("Ecobank"),
    STANDARD_CHARTERED("Standard Charted"), CITIBANK("Citi Bank");

    private final String bankName;

    RecognisedBank(String bankName) {
        this.bankName = bankName;
    }

    public static RecognisedBank getRandomBank() {
        return values()[(int) (Math.random() * values().length)];
    }

    public static Optional<RecognisedBank> getBank(String bankName) {
        for(RecognisedBank recognisedBank: values()) {
            if(recognisedBank.bankName.equalsIgnoreCase(bankName)) {
                return Optional.of(recognisedBank);
            }
        }
        return Optional.empty();
    }
}
