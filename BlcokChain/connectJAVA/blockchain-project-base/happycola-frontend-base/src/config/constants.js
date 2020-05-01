/**
 * 아래의 상수들을 임의로 변경하여 구현할 수 있습니다. 
 */
export const CATEGORIES = {
    DIGITAL: "D",
    CHILD: "C",
    HOBBY: "H"
};

export const AUCTION_STATUS = {
    AUCTION: {
        symbol: "A",
        explanation: "경매중"
    },
    BIDDED: {
        symbol: "B",
        explanation: "낙찰 완료"
    },
    CANCELED: {
        symbol: "C",
        explanation: "판매자에 의해 취소됨"
    },
    PURCHASED: {
        symbol: "P",
        explanation: "즉시 구매됨"
    },
    EXPIRED: {
        symbol: "E",
        explanation: "기간 만료로 판매 종료됨"
    },
    symbolToStatus(symbol) {
        switch (symbol) {
            case "A":
                return this.AUCTION;
            case "B":
                return this.BIDDED;
            case "C":
                return this.CANCELED;
            case "P":
                return this.PURCHASED;
            case "E":
                return this.EXPIRED;
        }
    }
};

export const ITEM_STATUS = {
    ADDED: {
        symbol: "A",
        explanation: "등록됨"
    },
    ONSALE: {
        symbol: "S",
        explanation: "판매중"
    },
    DELIVER: {
        symbol: "D",
        explanation: "배송중"
    },
    CONFIRMED: {
        symbol: "C",
        explanation: "판매완료(구매확정)"
    },
    CANCELED: {
        symbol: "X",
        explanation: "취소됨"
    },
    DELETED: {
        symbol: "N",
        explanation: "삭제됨"
    },
    symbolToStatus(symbol) {
        switch (symbol) {
            case "A":
                return this.ADDED;
            case "S":
                return this.ONSALE;
            case "D":
                return this.DELIVER;
            case "C":
                return this.CONFIRMED;
            case "X":
                return this.CANCELED;
            case "N":
                return this.DELETED;
        }
    }
};