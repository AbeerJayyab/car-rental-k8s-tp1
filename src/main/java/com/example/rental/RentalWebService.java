package com.example.rental;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RentalWebService {

    private List<Car> cars = new ArrayList<>();

    // إدخال بيانات افتراضية للسيارات عند تشغيل السيرفر
    public RentalWebService() {
        cars.add(new Car("11AA22", "Ferrari", 100));
        cars.add(new Car("AA11BB", "BMW", 80));
    }

    // 1. Get a list of unrented cars (عرض السيارات غير المؤجرة فقط)
    @GetMapping("/cars")
    @ResponseStatus(HttpStatus.OK)
    public List<Car> listOfCars() {
        List<Car> unrentedCars = new ArrayList<>();
        for (Car car : cars) {
            if (!car.isRented()) { // تصفية السيارات المتاحة فقط
                unrentedCars.add(car);
            }
        }
        return unrentedCars;
    }

    // 2. Get the features of a car (عرض تفاصيل سيارة محددة بواسطة رقم اللوحة)
    @GetMapping("/cars/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Car aCar(@PathVariable("plateNumber") String plateNumber) throws Exception {
        for (Car car : cars) {
            if (car.getPlateNumber().equalsIgnoreCase(plateNumber)) {
                return car;
            }
        }
        throw new Exception("Car not found!");
    }

    // 3. Rent a car OR Get back a car (تأجير أو إرجاع سيارة)
    @PutMapping(value = "/cars/{plateNumber}")
    @ResponseStatus(HttpStatus.OK)
    public void rentOrGetBack(
            @PathVariable("plateNumber") String plateNumber,
            @RequestParam(value = "rent", required = true) boolean rent,
            @RequestBody(required = false) Dates dates) throws Exception {

        Car car = aCar(plateNumber); // البحث عن السيارة برقم اللوحة
        car.setRented(rent);         // تغيير حالة التأجير (true = مؤجرة، false = معادة)

        if (rent && dates != null) {
            System.out.println("Car " + plateNumber + " rented from " + dates.getBegin() + " to " + dates.getEnd());
        } else if (!rent) {
            System.out.println("Car " + plateNumber + " returned back.");
        }
    }
}
