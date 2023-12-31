package com.taskagile.domain.application.impl;

import java.util.List;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taskagile.domain.application.BoardService;
import com.taskagile.domain.application.commands.CreateBoardCommand;
import com.taskagile.domain.common.event.DomainEventPublisher;
import com.taskagile.domain.model.board.Board;
import com.taskagile.domain.model.board.BoardManagement;
import com.taskagile.domain.model.board.BoardRepository;
import com.taskagile.domain.model.board.events.BoardCreatedEvent;
import com.taskagile.domain.model.user.UserId;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

  private BoardRepository boardRepository;
  private BoardManagement boardManagement;
  private DomainEventPublisher domainEventPublisher;

  public BoardServiceImpl(BoardManagement boardManagement, DomainEventPublisher domainEventPublisher,
      BoardRepository boardRepository) {
    this.boardManagement = boardManagement;

    this.domainEventPublisher = domainEventPublisher;
    this.boardRepository = boardRepository;
  }

  @Override
  public List<Board> findBoardsByMembership(UserId userId) {
    return boardRepository.findBoardsByMembership(userId);
  }

  @Override
  public Board createBoard(CreateBoardCommand command) {
    Assert.notNull(command, "`command` 파라미터는 null이면 안됩니다.");
    Board board = boardManagement.createBoard(command.getUserId(), command.getName(), command.getDescription(),
        command.getTeamId());
    domainEventPublisher.publish(new BoardCreatedEvent(this, board));
    return board;
  }
}
