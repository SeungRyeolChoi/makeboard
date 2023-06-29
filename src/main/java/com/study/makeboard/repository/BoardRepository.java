package com.study.makeboard.repository;

import com.study.makeboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//db에 전송하려면 필요한게 repository
@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {//JpaRepository를 상속받고, 엔티티 board와 id로 지정해준거에 type을 넣어주면 됨

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);

}
