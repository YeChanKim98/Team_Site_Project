package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class BoardController {

    private final BoardService boardService;
    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    // 자유게시판 메인
    @GetMapping({"freeBoard/view/main/{page}","freeBoard/view/main"}) // 페이지 받아서 페이징 기능 추가
    public String freeBoardMain(@PathVariable(required=false) Optional<Integer> page, Model model){

        if(page.isEmpty()){page= Optional.of(1);} // 페이지 미선택은 기본으로 1값
        List<free_Board> boardList = boardService.mainView(page.get()); // 마지막 페이지보다 높은 페이지 요구하면 강제로 마지막 페이지 반환
        model.addAttribute("boardList",boardList);
        model.addAttribute("page_now",page.get());

        int[] pageInfo = pagination(page.get());
        System.out.println("토탈페이지 : "+pageInfo[0]);
        System.out.println("Page.get() "+page.get()+"\tStart : "+pageInfo[1]+"\tEnd : "+pageInfo[2]);
        model.addAttribute("page_total",pageInfo[0]);
        model.addAttribute("block_start",pageInfo[1]);
        model.addAttribute("block_end",pageInfo[2]);


        return "boards/free/FreeBoardMain"; // 메인페이지로 이동}
    }

    // 작성
    @GetMapping("freeBoard/write")
    public String freeWrite(){
        return "boards/free/WriteForm";
    }

    @PostMapping("freeBoard/write")
    public String freeWrite(HttpServletRequest request, WriteForm writeForm){
        // 리덱터리 리스팅 회피 : writer변수와 세션에서 받은 변수의 값이 일치하지 않으면 메인으로 리다이렉트
        System.out.println("라이트 백 진입");
        free_Board board = new free_Board();
        HttpSession session = request.getSession(); // 로그인 여부는 이전에 거르므로, 백에서는 로그인 한 것으로 신뢰
        board.setFboard_writer(session.getAttribute("loginID").toString());
        board.setFboard_title(writeForm.getFboard_title());
        board.setFboard_content(writeForm.getFboard_content());
        System.out.println("게시글 작성 객체 입력 완료");
        boardService.writeBoard(board);
        System.out.println("서비스 입력 완료");
        return "redirect:/freeBoard/view/main/1"; // 게시판 메인으로 이동
    }

    // 삭제
    @PostMapping("freeBoard/delete/{fboard_num}")
    public String freeDelete(@PathVariable int fboard_num){
        boardService.deleteBoard(fboard_num);
        return "redirect:/freeBoard/view/main/"; // 게시판 메인으로 이동
    }

    // 수정
    @GetMapping("freeBoard/Update/{id}/{fboard_num}")
    public String freeUpdate(@PathVariable String id, @PathVariable int fboard_num, Model model){
        free_Board board = boardService.viewBoard(fboard_num);
        model.addAttribute("fboard_num",fboard_num);
        model.addAttribute("fboard_id",id);
        model.addAttribute("fboard_title",board.getFboard_title());
        model.addAttribute("fboard_content",board.getFboard_content());
        return "redirect:/"; //수성 폼으로  이동
    }

    @PostMapping("freeBoard/Update/{id}/{fboard_num}")
    public String freeUpdate(@PathVariable String id, @PathVariable int fboard_num, WriteForm writeForm){
        boardService.updateBoard(writeForm);
        return "redirect:freeBoard/view/main/"; // 게시판 메인으로 이동
    }

    // 조회
    @GetMapping("freeBoard/view/{fboard_num}")
    public String viewFboard(@PathVariable int fboard_num, Model model){
        free_Board board = boardService.viewBoard(fboard_num);
        model.addAttribute("view",board);
        return"boards/free/FreeBoardView"; // 뷰페이지로 이동
    }

    // 게시글 검색
    @GetMapping("freeBoard/view/search")
    public String searchFboard(@RequestParam String search_option, @RequestParam String keyword, Model model){
        System.out.println("================================>> 검색 조건 : "+search_option+" / "+keyword);
        List<free_Board> boardList = boardService.findBoard(search_option, keyword);
        if(boardList.isEmpty()){
            System.out.println("검색 결과 없음"); // 검색결과가 없을 때 보여줄 페이지 아직 안 만듬 : 일단 메시지 띄우고 메인으로 날림
                                                // 혹은 모델에 새로운 어트리뷰트를 넣어서 해당 어트리뷰트를 만나면 게시판 메인에 결과없음 페이지가 보여지도록
            return "redirect:/";
        }
//        System.out.println("=====검색 결과=====");
//        for(free_Board board : boardList){
//            System.out.println("제목 : "+board.getFboard_title()+" / 작성자 : "+board.getFboard_writer());
//        }

        model.addAttribute("boardList",boardList);
        return "boards/free/FreeBoardMain";
    }

    // 페이지네이션 : 메인 / 검색 용
    public int[] pagination(int page){

        int start = 1;
        if(page%5==0){start = (((int)page/5)-1)*5+1;}  // 마지막페이지(5배수 처리)
        else{start = ((int)page/5)*5+1;}               // 나머지
        int end = start+4;int total_page = Math.toIntExact(boardService.totalPost());
        if(total_page%10!=0) total_page=total_page/10+1;
        else total_page=total_page/10;
        if(total_page < end) end = total_page;          // 마지막 블록이 5개 미만이면 마지막 블록번호에 맞춰서 끝

        return new int[]{total_page, start, end};
    }
}
