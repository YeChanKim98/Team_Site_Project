package com.teamboard.TeamBoard.controller;

import com.teamboard.TeamBoard.board.BoardService;
import com.teamboard.TeamBoard.board.Form.WriteForm;
import com.teamboard.TeamBoard.board.free_Board;
import com.teamboard.TeamBoard.board.notice_Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
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
        int[] pageInfo = pagination(page.get(),  -1); // 페이지네이션 정보
        if(page.get()>pageInfo[0]){
            page = Optional.of(pageInfo[0]); // 최고페이지 이상으로 가려고하면 강제로 마지막페이지로 이동
            pageInfo[1] = page.get();
        }
        List<notice_Board> noticeList = boardService.listNoticeView();
        model.addAttribute("noticeList",noticeList);
        List<free_Board> boardList = boardService.mainView(page.get()); // 마지막 페이지보다 높은 페이지 요구하면 강제로 마지막 페이지 반환
        model.addAttribute("boardList",boardList);
        model.addAttribute("from","main");
        model.addAttribute("page_now",page.get());
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
    public String freeWrite(@RequestParam(required = false)String setAnonymous, HttpServletRequest request, WriteForm writeForm){ // 리덱터리 리스팅 회피 : writer변수와 세션에서 받은 변수의 값이 일치하지 않으면 메인으로 리다이렉트

        free_Board board = new free_Board();
        HttpSession session = request.getSession(); // 로그인 여부는 이전에 거르므로, 백에서는 로그인 한 것으로 신뢰 -> 한번만 쓰는데 변수 없이 바로 가져다 쓸지?
        System.out.println("익명성 여부 : "+setAnonymous);
        if(session.getAttribute("loginID")==null) {
            board.setFboard_writer("익명(비로그인)");
        }else if(setAnonymous!=null){
            board.setFboard_writer("익명(로그인)");
        }else{
            board.setFboard_writer(session.getAttribute("loginID").toString());
        }

        board.setFboard_title(writeForm.getFboard_title());
        board.setFboard_content(writeForm.getFboard_content());
        boardService.writeBoard(board);
        return "redirect:/freeBoard/view/main/1"; // 게시판 메인으로 이동
    }

    // 삭제
    @GetMapping("{kinds}/delete/{num}")
    public String freeDelete(@PathVariable String kinds,@PathVariable int num, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
            if(kinds.equals("freeBoard")) {
                PrintWriter out = response.getWriter();
                boardService.deleteBoard(num);
                out.println("<script>alert('게시글 삭제 완료');history.go(-2);</script>");
                out.flush();
                out.close();
                System.out.println("클로즈");
            }
        return "";
    }

    // 수정
    @GetMapping("{kinds}/update/{id}/{num}")
    public String freeUpdate(@PathVariable String kinds,@PathVariable String id, @PathVariable int num, Model model){
        if(kinds.equals("freeBoard")) {
            free_Board board = boardService.viewBoard_free(num);
            model.addAttribute("fboard_num", num);
            model.addAttribute("fboard_id", id);
            model.addAttribute("fboard_title", board.getFboard_title());
            model.addAttribute("fboard_content", board.getFboard_content());
        }
        return "redirect:/"; // 입력폼에 넘기고 값이 있으면 넣고 없으면 안 넣고, 추가 요소 확인
    }

    @PostMapping("{kinds}/Update/{id}/{fboard_num}")
    public String freeUpdate(@PathVariable String kinds, @PathVariable String id, @PathVariable int fboard_num, WriteForm writeForm){
        if(kinds.equals("freeBoard")) boardService.updateBoard(writeForm);
        return "redirect:freeBoard/view/main/1"; // 게시판 메인으로 이동
    }

    // 조회
    @GetMapping("{kinds}/view/{num}")
    public String viewBoard(@PathVariable String kinds, @PathVariable int num, Model model){
        System.out.println("[viewBoard]["+kinds+"] 진입 : "+num);
        if(kinds.equals("freeBoard")){
            System.out.println("[viewBoard][freeBoard] 서비스 호출");
            free_Board post = boardService.viewBoard_free(num);
            System.out.println("[viewBoard][freeBoard] 조건에 맞는 대상 찾기 완료");
            model.addAttribute("num",post.getFboard_num());
            model.addAttribute("writer",post.getFboard_writer());
            model.addAttribute("title",post.getFboard_title());
            model.addAttribute("content",post.getFboard_content());
            model.addAttribute("view_cnt",post.getFboard_view_count());
            model.addAttribute("comment_cnt",post.getFboard_comment_count());
            model.addAttribute("reg_date",post.getFboard_reg_date());
            model.addAttribute("from_kinds",kinds);
            System.out.println("[viewBoard][freeBoard] 뷰에 전달할 모델 생성 완료");
        }else if(kinds.equals("notice")){
            System.out.println("[viewBoard][notice] 서비스 호출");
            notice_Board post = boardService.viewBoard_notice(num);
            System.out.println("[viewBoard][notice] 조건에 맞는 대상 찾기 완료");
            model.addAttribute("num",post.getNotice_num());
            model.addAttribute("writer",post.getNotice_writer());
            model.addAttribute("title",post.getNotice_title());
            model.addAttribute("content",post.getNotice_content());
            model.addAttribute("view_cnt",post.getNotice_view_count());
            model.addAttribute("comment_cnt",post.getNotice_comment_count());
            model.addAttribute("reg_date",post.getNotice_reg_date());
            model.addAttribute("from_kinds",kinds);
            System.out.println("[viewBoard][notice] 뷰에 전달할 모델 생성 완료");
        }else{
            System.out.println("[viewBoard][Get]잘못된 페이지 요청");
            return "/freeBoard/view/main/1"; //이전 페이지로 돌아가도록
        }
        return"boards/PostView";
    }

    // 게시글 검색
    @GetMapping({"freeBoard/view/search","freeBoard/view/search/{page}"})
    public String searchFboard(@PathVariable(required=false) Optional<Integer> page, @RequestParam String search_option, @RequestParam String keyword, Model model){


        if(keyword.equals(""))return "redirect:/freeBoard/view/main/1"; // 키워드 없는 검색은 전체 조회(게시판 메인으로)

        if(page.isEmpty()){page= Optional.of(1);}
        List<free_Board> boardList = boardService.findBoard(search_option, keyword, page.get());
        int max = boardService.search_post_cnt(search_option,keyword).intValue();
        int[] pageInfo = pagination(page.get(), max);
        if(page.get()>pageInfo[0]){
            page = Optional.of(pageInfo[0]);
            pageInfo[1] = page.get();
        }
        if(boardList.isEmpty()){
            System.out.println("검색 결과 없음"); // 검색결과가 없을 때 보여줄 페이지 아직 안 만듬 : 일단 메시지 띄우고 메인으로 날림
            // 혹은 모델에 새로운 어트리뷰트를 넣어서 해당 어트리뷰트를 만나면 게시판 메인에 결과없음 페이지가 보여지도록
            return "redirect:/";
        }
        if(page.get()>max){return "redirect:/freeBoard/view/search/"+pageInfo[2];}
        model.addAttribute("boardList",boardList);
        model.addAttribute("keyword",keyword); // 검색결과를 뷰로 전송 후 hidden에 넣었다가 다시 전송 받아서 페이지네이션 실행
        model.addAttribute("search_option",search_option); // 검색결과를 뷰로 전송 후 hidden에 넣었다가 다시 전송 받아서 페이지네이션 실행
        model.addAttribute("from","search");
        model.addAttribute("page_now",page.get());
        model.addAttribute("page_total",pageInfo[0]);
        model.addAttribute("block_start",pageInfo[1]);
        model.addAttribute("block_end",pageInfo[2]);
        return "boards/free/FreeBoardMain";
    }

    // 페이지네이션 : 메인 / 검색 용
    public int[] pagination(int page, int max){
        int start = 1;
        int total_page;
        if(page%5==0){start = (((int)page/5)-1)*5+1;}  // 마지막페이지(5배수 처리)
        else{start = ((int)page/5)*5+1;}               // 나머지
        int end = start+4;
        if(max==-1){total_page = Math.toIntExact(boardService.totalPost())+5;} // +5 : 공지글 5개로 고정이므로 5개씩 밀려서 페이징 됨 그래서 +5로 맞춰줌
        else{total_page=max;}
        if(total_page%10!=0) total_page=total_page/10+1;
        else total_page=total_page/10;
        if(total_page < end) end = total_page;          // 마지막 블록이 5개 미만이면 마지막 블록번호에 맞춰서 끝

        return new int[]{total_page, start, end};
    }
}
