async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`)
    // console.log("서버로 부터 응답 받은 result 확인 : " , result)
    // return result.data
    return result
}

// 댓글 목록을 출력하는 함수.
// 비유 : 동기 함수,
// 손님1 : 커피 주문, , 직원 : 주문 받고, 커피 만들기, 받을 때 까지 대기.
// 그동안, 손님1은 그 앞에서 대기, 다른 손님들은 주문을 못하고 줄을 서서 기다립니다.
// 손님1이 커피 완성이 되면,
// 그다음에
// 손님2 번이 주문 가능함.

// 비유,: 비동기함수는,
// 카페에서,
// 손님1번 커피 주문함. 직원 : 진동벨을 전달 해줌..
// 직원 : 다시 다른 손님의 주문을 받음.
// 손님 2번 커피 주문함. 직원 : 진동벨을 전달 해줌..

// 결론, 클라이언트가, 서버에게 데이터를 요청하면,
// 기다림 + 진동벨을 들고 기다림, -> 서버가 데이터를 다 만들어서 전달해줌.
// 클라이언트 데이터를 받으면 됨.
async function getList({bno, page, size, goLast}){
    const result = await axios.get(`/replies/list/${bno}`,
        {params : {page,size}})

    // 댓글의 마지막 가는 여부를 체크해서, 가장 맨끝에 바로가기,
    if(goLast) {
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))
        return getList({bno:bno,page:lastPage, size:size})
    }

    return result.data
}

// 댓글 작성.
async function addReply(replyObj) {
    const response = await axios.post(`/replies/`, replyObj)
    return response.data

}

// 댓글 하나 조회.
async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}

// 댓글 수정
async function modifyReply (replyObj) {
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    return response.data
}

// 댓글 삭제
async function removeReply (rno) {
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}