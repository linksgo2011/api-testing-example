package cn.printf.api.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("A example to test show service with mock strategy")
public class ShowServiceTests {
    private static final String MOCK_OUTPUT = "Mocked show label";

    @Mock
    private TextService textService;

    @InjectMocks
    private ShowService showService;

    @BeforeEach
    void setMockOutput() {
        when(textService.getText()).thenReturn(MOCK_OUTPUT);
    }

    @Test
    @DisplayName("Mock the output of the text service using mockito")
    public void contextLoads() {
        assertEquals(showService.getShowLabel(), MOCK_OUTPUT);
    }
}
