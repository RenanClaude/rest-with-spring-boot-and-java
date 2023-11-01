package com.webdevelopment.services;

import org.springframework.stereotype.Service;

import com.webdevelopment.exceptions.ResourceNotFoundException;
import com.webdevelopment.util.Util;

@Service
public class MathService {

	public Double sumService(String firstNumber, String secondNumber) {
		if (!Util.isNumeric(firstNumber) || !Util.isNumeric(secondNumber)) {
			throw new ResourceNotFoundException("Please, set a numeric value");
		}
		return Util.convertToDouble(firstNumber) + Util.convertToDouble(secondNumber);
	}

	public Double subtractionService(String firstNumber, String secondNumber) {
		if (!Util.isNumeric(firstNumber) || !Util.isNumeric(secondNumber)) {
			throw new ResourceNotFoundException("Please, set a numeric value");
		}
		return Util.convertToDouble(firstNumber) - Util.convertToDouble(secondNumber);
	}

	public Double multiplicationService(String firstNumber, String secondNumber) {
		if (!Util.isNumeric(firstNumber) || !Util.isNumeric(secondNumber)) {
			throw new ResourceNotFoundException("Please, set a numeric value");
		}
		return Util.convertToDouble(firstNumber) * Util.convertToDouble(secondNumber);
	}

	public Double divisionService(String firstNumber, String secondNumber) {
		if (!Util.isNumeric(firstNumber) || !Util.isNumeric(secondNumber)) {
			throw new ResourceNotFoundException("Please, set a numeric value");
		}
		return Util.convertToDouble(firstNumber) / Util.convertToDouble(secondNumber);
	}

	public Double averageService(String firstNumber, String secondNumber) {
		if (!Util.isNumeric(firstNumber) || !Util.isNumeric(secondNumber)) {
			throw new ResourceNotFoundException("Please, set a numeric value");
		}
		return (Util.convertToDouble(firstNumber) + Util.convertToDouble(secondNumber)) / 2;
	}

	public Double squareRootService(String firstNumber) {
		if (!Util.isNumeric(firstNumber)) {
			throw new ResourceNotFoundException("Please, set a numeric value");
		}
		return Math.sqrt(Util.convertToDouble(firstNumber));
	}

}
