package net.wojtekk.aoc2018.days.day13

import java.io.File

fun main() {
    val path = "day13/input.txt"
    val initialState = File(ClassLoader.getSystemResource(path).file)
            .readLines()
            .filter(String::isNotEmpty)

    val maxLength = initialState.map { it.length }.max()!!
    val roadsWithCars = initialState.map { it.padEnd(maxLength, ' ') }

    var cars: MutableList<Car> = findCars(roadsWithCars)

    val roads = removeCars(roadsWithCars)

    do {
        cars.toList().forEach { car ->
            if (car in cars) {
                car.drive()
                car.navigate(roads)
                val collision = detectCollitions(cars)
                if (collision.isPresent) {
                    val col = collision.get()
                    cars.removeAll { it.x == col.first && it.y == col.second }
                }
            }
        }
    } while (cars.size > 1)

    System.out.println("Answer: $cars")
}
