package com.example.contbook.service.impl;

import com.example.contbook.model.Contact;
import com.example.contbook.repository.ContactRepository;
import com.example.contbook.service.ContactService;
import com.example.contbook.util.Validation;
import com.example.contbook.util.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact create(Contact contact) {
        if ((!Validator.checkLabel(contact.getEmailLabel(), Validation.label)) ||
                (!Validator.checkLabel(contact.getPhoneLabel(), Validation.label)) ||
                (!Validator.checkEmail(contact.getEmail(), Validation.regexEmail)) ||
                (!Validator.checkPhone(contact.getPhone(), Validation.regexPhone))){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"wrong Email");
        }
        return contactRepository.save(contact);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    @Transactional
    public Contact update(Contact contact, int id) {
        if ((!Validator.checkLabel(contact.getEmailLabel(), Validation.label)) ||
                (!Validator.checkLabel(contact.getPhoneLabel(), Validation.label)) ||
                (!Validator.checkEmail(contact.getEmail(), Validation.regexEmail)) ||
                (!Validator.checkPhone(contact.getPhone(), Validation.regexPhone))){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"wrong Email");
        }
        Contact fromDb = contactRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wrong ");
        });
        fromDb.setName(contact.getName());
        fromDb.setSurname(contact.getSurname());
        fromDb.setPhone(contact.getPhone());
        fromDb.setPhoneLabel(contact.getPhoneLabel());
        fromDb.setEmail(contact.getEmail());
        fromDb.setEmailLabel(contact.getEmailLabel());
        return fromDb;

    }

    @Override
    public void delete(int id) {
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> getByKey(String key) {
        return getAll()
                .stream()
                .filter(item -> item.stringify().toLowerCase(Locale.ROOT)
                        .contains(key.toLowerCase(Locale.ROOT)))
                .collect(Collectors
                        .toList());
    }
}
