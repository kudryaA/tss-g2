package tss.g2.fyre.models.actions;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Person;

public class CheckRegistration implements Action {
    private DataStorage dataStorage;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String email;

    public CheckRegistration(DataStorage dataStorage, String login, String password, String name, String surname, String email) {
        this.dataStorage = dataStorage;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    @Override
    public Answer getAnswer() {
        boolean result = dataStorage.createNewPerson(login, password, name, surname, email);

        return new Answer<>(true, result);
    }
}
