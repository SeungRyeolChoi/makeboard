package com.study.makeboard.controller;

import com.study.makeboard.entity.Board;
import com.study.makeboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService; //boardwritepro에 작성한 boardService가 controller 입장에선 뭔지 모르니까
    @GetMapping("/board/write")
    public String boardwriteForm(){

        return "boardwrite"; //html의 이름
    }

    @PostMapping("/board/writepro") //form tag action에 넣어준 url과 일치하여야함
    public String boardwritePro(Board board, Model model, MultipartFile file) throws IOException {//매개변수 넣어주어도 되나 많아지면 귀찮아지므로 Board 엔티티를 만들었으므로 그 클래스를 그대로 받아줄 수 있다.
        //System.out.println(board.getTitle());//Board안에 있는 id나 title을 받고싶을 때 필요한게 lombok. Board 엔티티에 @Data를 추가하자

        boardService.write(board, file);

        model.addAttribute("message","글 작성이 완료되었습니다");
        model.addAttribute("searchUrl","/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword){

        Page<Board> list = null;

        if(searchKeyword == null) {
            list = boardService.boardList(pageable);
        }
        else{
            list = boardService.boardSearchList(searchKeyword,pageable);

        }
         int nowPage=list.getPageable().getPageNumber()+1 ;
         int startPage=Math.max(nowPage-4,1);
         int endPage=Math.min(nowPage+5, list.getTotalPages());

        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "boardlist";
    }

    @GetMapping("/board/view")  //localhost:8080/board/view?id=1
    public String boardView(Model model,Integer id){

        model.addAttribute("board",boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id){

        boardService.boardDelete(id);
        return "redirect:/board/list"; //게시글 삭제후 게시글 리스트로 이동
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,
                              Model model){ //@PathVariable은 넘어갈 때 /뒤에 숫자가 깔끔하게 넘어감

        model.addAttribute("board",boardService.boardView(id));//수정버튼을 눌렀을 때 게시판에 써져있던 원래 데이터들도 가지고 와야함으로
        return "boardmodify"; //boardmodify.html은 boardwrite.html이랑 똑같음, 수정해야 하니까
    }


    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, MultipartFile file) throws IOException {

        //기존에 있던 글 불러오기
        Board boardTemp =  boardService.boardView(id);

        //기존에 있던 글에 새로 쓴 내용 덮어쓰기
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);

        return "redirect:/board/list";
    }

}
