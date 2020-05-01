pragma solidity ^0.4.24;

/**
 * @title Ownable
 * @dev The Ownable contract has an owner address, and provides basic authorization control
 * functions, this simplifies the implementation of "user permissions".
 */
contract Ownable {
  address public owner;

  event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);

  /**
   * @dev The Ownable constructor sets the original `owner` of the contract to the sender
   * account.
   */
  constructor() public {
    owner = msg.sender;
  }


  /**
   * @dev Throws if called by any account other than the owner.
   */
  modifier onlyOwner() {
    require(msg.sender == owner);
    _;
  }


  /**
   * @dev Allows the current owner to transfer control of the contract to a newOwner.
   * @param newOwner The address to transfer ownership to.
   */
  function transferOwnership(address newOwner) public onlyOwner {
    require(newOwner != address(0));
    emit OwnershipTransferred(owner, newOwner);
    owner = newOwner;
  }

}

/// @dev AuctionFactory
contract AuctionFactory is Ownable {
    
    /// 컨트랙트 배포 주소
    address public admin;
    // 생성된 모든 auction 리스트 
    address[] public auctions;
    
    event AuctionCreated(address auctionContract, address owner, uint numAuctions, address[] allAuctions);
    event NewAuction(address auctionContract, address owner, uint workId, uint minValue, uint startTime, uint endTime);

    constructor() public {
        admin = msg.sender;
    }

   /**
     * @dev 해당 state를 가지는 새로운 Auction을 생성합니다. 
     * @param itemId 상품의 Id를 나타냅니다.
     * @param minValue 경매시 입찰할 수 있는 최소가를 말합니다.
     * @param purchasePrice 즉시 구매가능한 가격을 말합니다.
     * @param startTime 경매 시작 시각으로 timestamp 형태로 받습니다.
     * @param endTime 경매 종료 시각으로 timestamp 형태로 받습니다.
     */
    function createAuction(uint itemId, uint minValue, uint purchasePrice, uint startTime, uint endTime) public returns (address){
        // todo 내용을 완성 합니다. 
    }

    /**
     * @dev 다음과 같이 함수를 추가해도 좋습니다. 
     */
    function allAuctions() public constant returns (address[]) {
        return auctions;
    }
}

/// @title Auction
contract Auction {

  // 생성자에 의해 정해지는 값
  address public seller;
  address admin;
  uint public auctionStartTime;
  uint public auctionEndTime;
  uint public minPrice;
  uint public purchasePrice;
  uint public itemId;

  // 현재 최고 입찰 상태
  address public highestBidder;
  uint public highestBid;

  mapping(address => uint) pendingReturns;
  address[] bidders;

  bool public ended = false;
  bool public purchased = false;

  event HighestBidIncereased(address bidder, uint amount);
  event AuctionEnded(address winner, uint amount);

  //**
   * @dev AuctionFactory의 createAuction함수에서 호출하는 생성자입니다.
   * 경매에서 고려해야하는 제한사항을 고려하여 상태변수를 초기화합니다. 
   */
  constructor(address _admin, address _seller, uint _itemId, uint _minPrice, uint _purchasePrice, uint startTime, uint endTime) public {
      // todo 내용을 완성 합니다.  
  }

  //**
   * @dev 입찰을 위한 함수입니다. 
   */
  function bid() public payable 
  onlyNotSeller {   
      // todo 내용을 완성 합니다. 
  }


  //**
   * @dev 즉시 구매를 위한 함수입니다. 
   */
  function purchase() payable public 
  onlyNotSeller {
       // todo 내용을 완성 합니다. 
  }
  
  //**
   * @dev 구매 확정을 위한 함수입니다. 
   * 구매 확정시 판매자에게 지불이 완료됩니다.
   */
  function confirmItem() public {
      require(ended == true && msg.sender == highestBidder);
      
      if(!seller.send(highestBid)){
          revert();
      }
  }

  //**
   * @dev 환불을 위한 함수입니다. 
   * 환불은 입찰 당사자가 해당 함수를 호출함으로써 가능합니다.
   */
  function withdraw() public returns (bool) {
       // todo 내용을 완성 합니다. 
  }

  //**
   * @dev 경매 종료를 위한 함수입니다.
   * 경매 생성자만이 경매를 종료시킬 수 있습니다.
   * 현재까지의 입찰 중 최고가를 선택하여 경매를 종료합니다. 
   */
  function endAuction() public {
    // todo 내용을 완성 합니다. 
  }

  //**
   * @dev 경매 취소를 위한 함수입니다. 
   * 경매 생성자만이 경매를 취소할 수 있습니다.
   * 모든 입찰에 대해 환불을 수행하고 경매를 종료합니다.  
   */
  function cancelAuction() public {
    // todo 내용을 완성 합니다. 
  }

  //**
   * @dev 이와 같이 추가 함수를 구현해보아도 좋습니다.  
   */
  function getPendingReturnsBy(address _address) view public returns (uint){
      return pendingReturns[_address];
  }
  
  function getAuctionInfo() view public returns (uint, uint, uint, uint, uint, address, uint, bool){
      return (auctionStartTime, auctionEndTime, minPrice, purchasePrice, itemId, highestBidder, highestBid, ended);
  }

  //**
   * @dev 이와 같이 추가 modifier를 구현하는 것을 권장합니다.   
   */
  modifier onlyNotSeller {
    require(msg.sender != seller);
    _;
  }
}