package cn.printf.api.services;

import org.springframework.stereotype.Service;

@Service
public class ShowService {

	private final TextService textService;

	public ShowService(TextService textService) {
		this.textService = textService;
	}

	public String getShowLabel() {
		return textService.getText();
	}
}
