package com.example.msg.Domain;

public class ErrorCodeApi {
    /*
    에러코드에 관한 정의
    0: onFailureListner가 호출되었습니다. Exception e를 참조해야합니다.
    1: 태스크가 실패하였습니다. 대표적으로 쿼리 도중에 쿼리가 취소된 경우가 있습니다.
    2: 다큐먼트가 null입니다.
    10: 파일 업로드 실패.
     */
    public static String getCustomErrorMessage(int errorCode, Error e) {
       String customErrorMessage = null;
        switch(errorCode) {
            case 0:
                customErrorMessage = "데이터를 읽는데 실패햐였습니다. 다시 한 번 시도해주세요.";
                break;
            case 1:
                customErrorMessage = "데이터를 읽는 작업이 취소되었습니다. 다시 한 번 시도해주세요.";
                break;
            case 2:
                customErrorMessage = "조회한 데이터가 존재하지 않습니다. 다시 한 번 시도해주세요.";
                break;
            case 10:
                customErrorMessage = "파일을 읽어오는데 실패하였습니다. 다시 한 번 시도 해주세요.";
                break;
            default:
                customErrorMessage = "정의되지 않은 에러입니다. 관리자에게 문의하세요.";
                break;
        }
        return customErrorMessage;
    }
}
