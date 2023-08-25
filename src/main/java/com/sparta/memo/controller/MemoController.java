package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class MemoController {

    private final Map<Long,Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto){
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        Long maxId = memoList.size()>0 ? Collections.max(memoList.keySet())+1:1;
        memo.setId(maxId);

        memoList.put(memo.getId(),memo);

        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos(){
        // Map To List
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(memo -> new MemoResponseDto(memo)).toList();
        return responseList;

    }

    @PutMapping("/memos/{id}")
    public long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto){
        // 해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)){
            Memo memo = memoList.get(id);

            // memo 수정
            memo.update(requestDto);
            return memo.getId();
        }
        else{
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id){
        if(memoList.containsKey(id)) {
            memoList.remove(id);
            return id;
        }
        else throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
    }
}
