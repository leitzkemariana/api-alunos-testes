package com.delegrego.api_alunos_testes.service;

import com.delegrego.api_alunos_testes.dto.AlunoRequest;
import com.delegrego.api_alunos_testes.dto.AlunoResponse;
import com.delegrego.api_alunos_testes.model.Aluno;
import com.delegrego.api_alunos_testes.repository.AlunoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AlunoServiceTest {
    @InjectMocks
    AlunoService service;

    @Mock
    AlunoRepository repository;

    @Test
    void deveSalvarERetornarAluno(){
        //Arrange
        AlunoRequest request = new AlunoRequest("Rock", "rock@email.com", "123654", LocalDate.of(2020, 11,15), 10);

        //Stub
        Mockito.when(repository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);

        Aluno aluno = new Aluno(1L, request.getNome(), request.getEmail(), request.getSenha(), request.getDataNascimento(), request.getMedia());
        Mockito.when(repository.save(Mockito.any(Aluno.class))).thenReturn(aluno);

        //Act
        AlunoResponse response = service.cadastrarAluno(request);

        //Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isInstanceOf(AlunoResponse.class);

        Assertions.assertThat(response.getId()).isGreaterThan(0);
        Assertions.assertThat(response.getNome()).isEqualTo(request.getNome());
        Assertions.assertThat(response.getEmail()).isEqualTo(request.getEmail());
        Assertions.assertThat(response.getDataNascimento()).isEqualTo(request.getDataNascimento());
        Assertions.assertThat(response.getMedia()).isEqualTo(request.getMedia());
    }

    @Test
    void deveRetornarListaDeAlunos(){
        //Arrange
        Aluno aluno1 = new Aluno(1L, "Maria", "maria@email.com", "123", LocalDate.of(2008, 10,25), 9.8);
        Aluno aluno2 = new Aluno(2L, "Carla", "carla@email.com", "123", LocalDate.of(2008, 4,5), 9.7);

        //Stub
        Mockito.when(repository.findAll()).thenReturn(List.of(aluno1, aluno2));

        //Act
        List<AlunoResponse> alunos = service.listarAlunos();

        //Assert
        Assertions.assertThat(alunos).hasSize(2);
    }

    @Test
    void deveRetornarAlunoPorID(){
        Aluno aluno1 = new Aluno(1L, "Maria", "maria@email.com", "123", LocalDate.of(2008, 10,25), 9.8);

        Mockito.when(repository.findById(aluno1.getId())).thenReturn(Optional.of(aluno1));

        AlunoResponse response = service.obterAlunoPorId(aluno1.getId());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isInstanceOf(AlunoResponse.class);
    }

    @Test
    void deveAtualizarAluno(){
        Aluno aluno1 = new Aluno(1L, "Carla", "carla@email.com", "123", LocalDate.of(2008, 10,25), 9.8);
        AlunoRequest request = new AlunoRequest("Carla Maria", "carla@email.com", "123", LocalDate.of(2008, 10,25), 9.8);

        Mockito.when(repository.findById(aluno1.getId())).thenReturn(Optional.of(aluno1));
        Mockito.when(repository.save(Mockito.any(Aluno.class))).thenReturn(aluno1);

        AlunoResponse response = service.atualizarAluno(aluno1.getId(), request);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isInstanceOf(AlunoResponse.class);
        Assertions.assertThat(response.getId()).isEqualTo(aluno1.getId());
        Assertions.assertThat(response.getNome()).isEqualTo(request.getNome());
    }
}
