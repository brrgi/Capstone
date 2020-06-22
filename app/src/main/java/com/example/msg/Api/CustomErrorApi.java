package com.example.msg.Api;

public class CustomErrorApi {

    /*
    에러코드에 관한 정의
    0: onFailureListner가 호출되었습니다. Exception e를 참조해야합니다.
    1: 태스크가 실패하였습니다. 대표적으로 쿼리 도중에 쿼리가 취소된 경우가 있습니다.
    2: 다큐먼트가 null입니다.
     */
    public static String makeErrorMessageForUser(int errorCode, Error e) {
        String errorMessage = "";

        switch(errorCode) {
            case 0:
            case 1:
                errorMessage = String.format("데이터를 불러오는데 실패하였습니다. 인터넷 상태를 확인해주세요. 에러 메세지: %s", e.toString());
                break;
            case 2:
                errorMessage = "불러올 수 있는 데이터가 존재하지 않습니다. 관리자한테 문의해주세요.";
                break;
            default:
                errorMessage = "알수 없는 에러입니다. 관리자한테 문의해주세요.";
                break;
        }

        return errorMessage;
    }
}
