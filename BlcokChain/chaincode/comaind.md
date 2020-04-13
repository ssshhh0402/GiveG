sudo docker exec -e "CORE_PEER_LOCALMSPID=HFTeam2MSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.HFTeam2 peer chaincode install -n donationtest_v_05 -v 0.1 -l node -p /var/hyperledger/chaincode/node



sudo docker exec -e "CORE_PEER_LOCALMSPID=HFTeam2MSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.HFTeam2 peer chaincode instantiate -o orderer0.ordererorg:7050 -C team2channel -n donationtest_v_05 -v 0.1 -c '{"Args":["init"]}' -P "OR('HFTeam2MSP.member')"



-user 생성-

docker exec -e "CORE_PEER_LOCALMSPID=HFTeam2MSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.HFTeam2 peer chaincode invoke -o orderer0.ordererorg:7050 -C team2channel -n donationtest_v_05 -c '{"Args":["registerUser","qwer","1234","admin"]}'



-user 확인-

docker exec -e "CORE_PEER_LOCALMSPID=HFTeam2MSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.HFTeam2 peer chaincode query -o orderer0.ordererorg:7050 -C team2channel -n donationtest_v_05 -c '{"Args":["query","qwer"]}'



-registerDonationProject-

docker exec -e "CORE_PEER_LOCALMSPID=HFTeam2MSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.HFTeam2 peer chaincode invoke -o orderer0.ordererorg:7050 -C team2channel -n donationtest_v_05 -c '{"Args":["registerDonationProject","qwerPJT","qwer","10","2020.04.30"]}'



-pjt 확인-

docker exec -e "CORE_PEER_LOCALMSPID=HFTeam2MSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.HFTeam2 peer chaincode query -o orderer0.ordererorg:7050 -C team2channel -n donationtest_v_05 -c '{"Args":["query","qwerPJT"]}'



-donation-

docker exec -e "CORE_PEER_LOCALMSPID=HFTeam2MSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.HFTeam2 peer chaincode invoke -o orderer0.ordererorg:7050 -C team2channel -n donationtest_v_05 -c '{"Args":["donateToProject","qwer","qwerPJT","10"]}'

docker exec -e "CORE_PEER_LOCALMSPID=HFTeam2MSP" -e "CORE_PEER_MSPCONFIGPATH=/var/hyperledger/users/msp" peer0.HFTeam2 peer chaincode query -o orderer0.ordererorg:7050 -C team2channel -n donationtest_v_05 -c '{"Args":["donateToProject","qwer","qwerPJT","10"]}'

docker rmi -f {imageid}



~/dev/ledger/HFTeamX/chaincodes