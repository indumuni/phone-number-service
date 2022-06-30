package com.belong.phonenumber;

import com.belong.phonenumber.dto.PhoneNumberDTO;
import com.belong.phonenumber.dto.SearchFilterDTO;
import com.belong.phonenumber.exception.PhoneNumberNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Repository
public class FlatFilePhoneNumberRepository implements PhoneNumberRepository {

    Logger logger = LoggerFactory.getLogger(FlatFilePhoneNumberRepository.class);
    private final List<PhoneNumberDTO> phoneNumbers;
    private final Map<String, PhoneNumberDTO> phoneNumbersIndex;

    public FlatFilePhoneNumberRepository() {
        Class<FlatFilePhoneNumberRepository> clazz = FlatFilePhoneNumberRepository.class;
        InputStream inputStream = clazz.getResourceAsStream("/FlatFilePhoneNumberRepository.txt");
        try {
            this.phoneNumbers = FlatFilePhoneNumberRepository.transformToPhoneNumbers(inputStream);
            this.phoneNumbers.sort(Comparator.comparing(o -> o.id));
            phoneNumbersIndex = this.phoneNumbers.stream().collect(Collectors.toMap(PhoneNumberDTO::id, p -> p));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PhoneNumberDTO> findPhoneNumbers(SearchFilterDTO filter) {
        checkArgument(filter != null, "Invalid search filter");
        logger.info("findPhoneNumbers");

        List<PhoneNumberDTO> filtered = this.phoneNumbers;

        if (filter.customerId != null && filtered.size() > 0) {
            filtered = filtered.stream()
                    .filter(p -> p.customerId.equals(filter.customerId))
                    .collect(Collectors.toList());
        }
        int startIndex = validStartIndex(filter.skip, filtered.size());
        int endIndex = validEndIndex(startIndex, filter.limit, filtered.size());

        return filtered
                .subList(startIndex, endIndex)
                .stream()
                .map(p -> new PhoneNumberDTO(p.id, p.phoneNumber, p.active, p.customerId))
                .collect(Collectors.toList());
    }

    @Override
    public void changeStatus(String phoneNumberId, boolean status) {
        logger.info("Status:" + status);
        PhoneNumberDTO item = this.phoneNumbersIndex.get(phoneNumberId);
        if (item == null) {
            throw new PhoneNumberNotFound("Invalid phone number");
        }
        item.active = status;
    }

    private int validEndIndex(int startIndex, int limit, int filtered) {
        int endIndex = startIndex + limit;
        return Math.min(endIndex, filtered);
    }

    private int validStartIndex(int filter, int filtered) {
        return Math.min(filter, filtered);
    }

    List<PhoneNumberDTO> phoneNumbers() {
        return this.phoneNumbers;
    }

    public static List<PhoneNumberDTO> transformToPhoneNumbers(InputStream inputStream)
            throws IOException {
        List<PhoneNumberDTO> phoneNumbers = new ArrayList<>();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = line.split(",");
                phoneNumbers.add(new PhoneNumberDTO(record[0], record[1], Boolean.parseBoolean(record[3]), record[2]));
            }
        }
        return phoneNumbers;
    }

}
