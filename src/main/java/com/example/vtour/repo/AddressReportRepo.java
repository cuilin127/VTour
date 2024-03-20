package com.example.vtour.repo;

import com.example.vtour.model.AddressReport;
import com.example.vtour.model.House;
import okhttp3.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressReportRepo extends CrudRepository<AddressReport, Integer> {
}
