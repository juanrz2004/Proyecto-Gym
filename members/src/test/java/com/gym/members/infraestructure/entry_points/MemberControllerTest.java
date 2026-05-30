package com.gym.members.infraestructure.entry_points;

import com.gym.members.domain.model.Member;
import com.gym.members.domain.usecase.MemberUseCase;
import com.gym.members.infraestructure.mapper.MemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest {
    private MemberUseCase useCase;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        useCase = mock(MemberUseCase.class);
        MemberMapper mapper = new MemberMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new MemberController(useCase, mapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // ─── SAVE ───────────────────────────────────────────────────────────────

    @Test
    void shouldCreateMember() throws Exception {
        Member member = new Member("document-test", "fullName-test", "email-test", "phone-test", "status-test");
        when(useCase.saveMember(any(Member.class))).thenReturn(member);

        mockMvc.perform(post("/api/gym/member/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"document\":\"document-test\",\"fullName\":\"fullName-test\",\"email\":\"email-test\",\"phone\":\"phone-test\",\"status\":\"status-test\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.document").value(member.getDocument()));
    }

    @Test
    void shouldReturnBadRequestWhenCreateMemberWithDuplicateDocument() throws Exception {
        when(useCase.saveMember(any(Member.class)))
                .thenThrow(new IllegalArgumentException("Ya existe un registro con document: document-test"));

        mockMvc.perform(post("/api/gym/member/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"document\":\"document-test\",\"fullName\":\"fullName-test\",\"email\":\"email-test\",\"phone\":\"phone-test\",\"status\":\"status-test\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ya existe un registro con document: document-test"));
    }

    @Test
    void shouldReturnBadRequestWhenCreateMemberWithMissingFields() throws Exception {
        mockMvc.perform(post("/api/gym/member/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"document\":\"\",\"fullName\":\"\",\"email\":\"\",\"phone\":\"\",\"status\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    // ─── GET ─────────────────────────────────────────────────────────────────

    @Test
    void shouldGetMember() throws Exception {
        Member member = new Member("document-test", "fullName-test", "email-test", "phone-test", "status-test");
        when(useCase.getMemberByDocument(member.getDocument())).thenReturn(member);

        mockMvc.perform(get("/api/gym/member/get/{document}", member.getDocument()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.document").value(member.getDocument()));
    }

    @Test
    void shouldReturnNotFoundWhenGetMemberNotExists() throws Exception {
        when(useCase.getMemberByDocument("not-exists"))
                .thenThrow(new NoSuchElementException("No existe registro con document: not-exists"));

        mockMvc.perform(get("/api/gym/member/get/{document}", "not-exists"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe registro con document: not-exists"));
    }

    // ─── LIST ────────────────────────────────────────────────────────────────

    @Test
    void shouldListMembers() throws Exception {
        Member member1 = new Member("doc-1", "name-1", "email-1", "phone-1", "status-1");
        Member member2 = new Member("doc-2", "name-2", "email-2", "phone-2", "status-2");
        when(useCase.listMembers()).thenReturn(List.of(member1, member2));

        mockMvc.perform(get("/api/gym/member/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].document").value("doc-1"))
                .andExpect(jsonPath("$[1].document").value("doc-2"));
    }

    @Test
    void shouldListMembersEmpty() throws Exception {
        when(useCase.listMembers()).thenReturn(List.of());

        mockMvc.perform(get("/api/gym/member/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    @Test
    void shouldUpdateMember() throws Exception {
        Member member = new Member("document-test", "fullName-updated", "email-updated", "phone-updated", "status-updated");
        when(useCase.updateMember(eq("document-test"), any(Member.class))).thenReturn(member);

        mockMvc.perform(put("/api/gym/member/update/{document}", "document-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"document\":\"document-test\",\"fullName\":\"fullName-updated\",\"email\":\"email-updated\",\"phone\":\"phone-updated\",\"status\":\"status-updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("fullName-updated"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdateMemberNotExists() throws Exception {
        when(useCase.updateMember(eq("not-exists"), any(Member.class)))
                .thenThrow(new NoSuchElementException("No existe registro con document: not-exists"));

        mockMvc.perform(put("/api/gym/member/update/{document}", "not-exists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"document\":\"not-exists\",\"fullName\":\"fullName-test\",\"email\":\"email-test\",\"phone\":\"phone-test\",\"status\":\"status-test\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe registro con document: not-exists"));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateMemberWithMissingFields() throws Exception {
        mockMvc.perform(put("/api/gym/member/update/{document}", "document-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"document\":\"\",\"fullName\":\"\",\"email\":\"\",\"phone\":\"\",\"status\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────

    @Test
    void shouldDeleteMember() throws Exception {
        doNothing().when(useCase).deleteMemberByDocument("document-test");

        mockMvc.perform(delete("/api/gym/member/delete/{document}", "document-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Registro document-test eliminado correctamente"));
    }

    @Test
    void shouldReturnNotFoundWhenDeleteMemberNotExists() throws Exception {
        doThrow(new NoSuchElementException("No existe registro con document: not-exists"))
                .when(useCase).deleteMemberByDocument("not-exists");

        mockMvc.perform(delete("/api/gym/member/delete/{document}", "not-exists"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe registro con document: not-exists"));
    }
}