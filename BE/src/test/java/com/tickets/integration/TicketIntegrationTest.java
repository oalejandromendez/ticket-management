package com.tickets.integration;

import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TicketIntegrationTest {

    @Autowired private MockMvc mockMvc;

    private String tokenAdmin;

    @BeforeEach
    void setup() throws Exception {
        tokenAdmin = authenticate();
    }

    private String authenticate() throws Exception {
        String loginJson = """
              {
                "username": "admin",
                "password": "admin123"
              }
           """;
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(response, "$.token");
    }

    @Test
    void createTicket() throws Exception {
        String json = """
            {
              "title": "Error en login",
              "description": "El login falla con 500",
              "status": "OPEN",
              "priority": "HIGH"
            }
            """;

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Error en login"));
    }

    @Test
    void getTicketById() throws Exception {

        String json = """
            {
              "title": "Ticket test",
              "description": "Probando getById",
              "status": "OPEN",
              "priority": "LOW"
            }
            """;

        String response = mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                .header("Authorization", "Bearer " + tokenAdmin))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number id = JsonPath.read(response, "$.id");

        mockMvc.perform(get("/tickets/{id}", id.longValue())
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Ticket test"));
    }

    @Test
    void getTickets() throws Exception {
        mockMvc.perform(get("/tickets?page=0&limit=5")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void updateTickers() throws Exception {
        String createJson = """
            {
              "title": "Bug en reporte",
              "description": "Error inicial",
              "status": "OPEN",
              "priority": "MEDIUM"
            }
            """;

        String response = mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson)
                .header("Authorization", "Bearer " + tokenAdmin))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number id = JsonPath.read(response, "$.id");

        String updateJson = """
            {
              "title": "Bug corregido",
              "description": "Actualizado",
              "status": "CLOSED",
              "priority": "LOW"
            }
            """;

        mockMvc.perform(patch("/tickets/{id}", id.longValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Bug corregido"))
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void deleteTicket() throws Exception {
        String json = """
        {
          "title": "Eliminar ticket",
          "description": "Test delete",
          "status": "OPEN",
          "priority": "LOW"
        }
        """;

        String response = mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number id = JsonPath.read(response, "$.id");

        mockMvc.perform(delete("/tickets/{id}", id.longValue())
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tickets/{id}", id)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNotFound());
    }

}
