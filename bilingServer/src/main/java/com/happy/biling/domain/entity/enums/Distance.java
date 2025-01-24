package com.happy.biling.domain.entity.enums;

//public enum Distance {
//    거리무관,
//    km3,
//    km5,
//    km10
//}
public enum Distance {
    거리무관("거리무관"),
    KM_3("3km"),
    KM_5("5km"),
    KM_10("10km");

    private final String value;

    Distance(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // DB 값을 enum으로 변환하는 메서드 추가
    public static Distance fromValue(String value) {
        for (Distance distance : Distance.values()) {
            if (distance.getValue().equals(value)) {
                return distance;
            }
        }
        throw new IllegalArgumentException("Unknown distance value: " + value);
    }
}


