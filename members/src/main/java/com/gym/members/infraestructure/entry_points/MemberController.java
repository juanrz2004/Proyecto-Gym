package com.gym.members.infraestructure.entry_points;

import com.gym.members.application.dto.MemberRequestDTO;
import com.gym.members.application.dto.MemberResponseDTO;
import com.gym.members.application.dto.MemberUpdateDTO;
import com.gym.members.domain.model.Member;
import com.gym.members.domain.usecase.MemberUseCase;
import com.gym.members.infraestructure.mapper.MemberMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gym/member")
public class MemberController {
    private final MemberUseCase useCase;
    private final MemberMapper mapper;

    public MemberController(MemberUseCase useCase, MemberMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @PostMapping("/save")
    public ResponseEntity<MemberResponseDTO> saveMember(@Valid @RequestBody MemberRequestDTO requestDTO) {
        Member member = useCase.saveMember(mapper.toMemberFromDTO(requestDTO));
        return new ResponseEntity<>(mapper.toMemberResponseDTO(member), HttpStatus.CREATED);
    }

    @GetMapping("/get/{document}")
    public ResponseEntity<MemberResponseDTO> getMember(@PathVariable String document) {
        Member member = useCase.getMemberByDocument(document);
        return ResponseEntity.ok(mapper.toMemberResponseDTO(member));
    }

    @GetMapping("/list")
    public ResponseEntity<List<MemberResponseDTO>> listMembers() {
        return ResponseEntity.ok(useCase.listMembers().stream().map(mapper::toMemberResponseDTO).toList());
    }

    @PutMapping("/update/{document}")
    public ResponseEntity<MemberResponseDTO> updateMember(@PathVariable String document, @Valid @RequestBody MemberUpdateDTO updateDTO) {
        Member member = useCase.updateMember(document, mapper.toMemberFromUpdateDTO(updateDTO));
        return ResponseEntity.ok(mapper.toMemberResponseDTO(member));
    }

    @DeleteMapping("/delete/{document}")
    public ResponseEntity<Map<String, String>> deleteMember(@PathVariable String document) {
        useCase.deleteMemberByDocument(document);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Registro " + document + " eliminado correctamente");
        return ResponseEntity.ok(response);
    }
}
