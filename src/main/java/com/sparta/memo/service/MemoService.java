package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@RequiredArgsConstructor //lombok 사용
public class MemoService {
    private final MemoRepository memoRepository;


    //@Autowired 생성자 하나 일때는 생략가능
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }
//    public MemoService(ApplicationArguments context){
            // 1. 'bean' 이름으로 가져오기
//        MemoRepository memoRepository = (MemoRepository) context.getBean("memoRepository");
//        this.memoRepository = memoRepository;
//          // 2. 클래스 로 가져오기
//        MemoRepository memoRepository = context.getBean(MemoRepository.class);
//    }
//    @Autowired // 함수 주입
//    public void setDi(MemoRepository memoRepository){
//        this.memoRepository = memoRepository;
//

//    }

//    @Autowired // 필드 주입
//    private final MemoRepository memoRepository;

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);
        Memo saveMemo = memoRepository.save(memo);
        // Entity -> ResponseDto
        return new MemoResponseDto(memo);
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAll().stream().map(MemoResponseDto::new).toList();
        // DB 조회
    }

    @Transactional
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        memo.update(requestDto);
        return id;

    }


    public Long deleteMemo(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        memoRepository.delete(memo);
        return id;

    }

    private Memo findMemo(Long id){
        return memoRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
    }
}
