package com.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickets.dto.TicketCreateDto;
import com.tickets.dto.TicketResponseDto;
import com.tickets.dto.TicketUpdateDto;
import com.tickets.service.ITicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock private ITicketService iTicketService;

    @InjectMocks private TicketController ticketController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    void testGetById() throws Exception {
        TicketResponseDto responseDto = new TicketResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Test Ticket");

        when(iTicketService.getById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Ticket"));

        verify(iTicketService, times(1)).getById(1L);
    }

    @Test
    void testGetAll() throws Exception {
        TicketResponseDto ticket1 = new TicketResponseDto();
        ticket1.setId(1L);
        ticket1.setTitle("Ticket 1");

        Page<TicketResponseDto> page = new PageImpl<>(List.of(ticket1));
        when(iTicketService.get(any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/tickets?page=0&limit=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Ticket 1"));
    }

    @Test
    void testCreate() throws Exception {
        TicketCreateDto createDto = new TicketCreateDto();
        createDto.setTitle("New Ticket");

        TicketResponseDto responseDto = new TicketResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("New Ticket");

        when(iTicketService.create(any(TicketCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Ticket"));

        verify(iTicketService, times(1)).create(any(TicketCreateDto.class));
    }

    @Test
    void testUpdate() throws Exception {
        TicketUpdateDto updateDto = new TicketUpdateDto();
        updateDto.setTitle("Updated Ticket");

        TicketResponseDto responseDto = new TicketResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Updated Ticket");

        when(iTicketService.update(eq(1L), any(TicketUpdateDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Ticket"));

        verify(iTicketService, times(1)).update(eq(1L), any(TicketUpdateDto.class));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(iTicketService).delete(1L);

        mockMvc.perform(delete("/tickets/1"))
                .andExpect(status().isNoContent());

        verify(iTicketService, times(1)).delete(1L);
    }

}