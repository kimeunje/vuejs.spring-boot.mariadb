package com.taskagile.domain.application.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.taskagile.domain.common.event.DomainEventPublisher;
import com.taskagile.domain.model.board.Board;
import com.taskagile.domain.model.board.BoardManagement;
import com.taskagile.domain.model.board.BoardRepository;
import com.taskagile.domain.model.user.UserId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 보드 관련 애플리케이션 서비스 테스트
 *
 * 테스트 메소드는 [작업 단위_테스트 중인 상태_ 예상되는 행동] 명명 규약을 따른다.
 */
public class BoardServiceImplTests {
  private BoardManagement boardnManagementMock;
  private DomainEventPublisher domainEventPublisherMock;
  private BoardServiceImpl instance;
  private BoardRepository boardRepositoryMock;

  @BeforeEach
  public void setUp() {
    boardnManagementMock = mock(BoardManagement.class);
    domainEventPublisherMock = mock(DomainEventPublisher.class);
    boardRepositoryMock = mock(BoardRepository.class);
    instance = new BoardServiceImpl(boardnManagementMock, domainEventPublisherMock,
        boardRepositoryMock);
  }

  // -------------------------------------------
  // 메서드 findBoardsByMembership()
  // -------------------------------------------
  @Test
  public void findBoardsByMembership_noExistUserId_shouldFail() {
    // Arrange
    UserId noExistUserId = new UserId(999L);
    when(boardRepositoryMock.findBoardsByMembership(noExistUserId)).thenReturn(Collections.emptyList());

    // Act
    List<Board> resultBoards = instance.findBoardsByMembership(noExistUserId);

    // Assert
    assertTrue(resultBoards.isEmpty());
    verify(boardRepositoryMock).findBoardsByMembership(noExistUserId);
  }

  @Test
  public void findBoardsByMembership_existUserId_shouldSucceed() {
    // Arrange
    UserId userId = new UserId(1L);
    List<Board> expectedBoards = Arrays.asList(new Board(), new Board());
    when(boardRepositoryMock.findBoardsByMembership(userId)).thenReturn(expectedBoards);

    // Act
    List<Board> resultBoards = instance.findBoardsByMembership(userId);

    // Assert
    assertEquals(expectedBoards, resultBoards);
    verify(boardRepositoryMock).findBoardsByMembership(userId);
  }

  // -------------------------------------------
  // 메서드 createBoard()
  // -------------------------------------------

  @Test
  public void createBoard_nullCommand_shouldFail() {
    assertThrows(IllegalArgumentException.class, () -> {
      instance.createBoard(null);
    });
  }
}
