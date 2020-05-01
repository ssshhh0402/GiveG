const shim = require('fabric-shim');

/**
 * 1. user 등록
 * 2. donationPJT 등록 == 모금 시작
 * 3. donationPJT 시작 == 모금이 완료되고 시작됨을 알림
 * 4. donate  -----------> query 구현
 * 5. donationPJT 완료
 * 6. donationPJT 모금 종료
 * 7. donationPJT 목표 완료
 * 8. 환불 신청 ------------------------------> User refundAmount에 추가
 * 9. 환불 완료 ------------------->카카오 페이 API를 통해 완료되면 User refundAmount update
 * 9. donationPJT 목표 미완수시 환불------------> User refundAmount에 추가
 * 
 */

var Chaincode = class {
    /**
     * 
     * @param {Object} stub 
     * @return {SuccessResponse} =>shim.success() 
     */
    async Init(stub) {
        console.info('Instantiated completed');
        return shim.success();
    }

    /**
     * @param {Object} stub
     * @return {SuccessResponse} 
     */
    async Invoke(stub) {
        let retrieve = stub.getFunctionAndParameters();
        let method = this[retrieve.fcn];

        if (!method) {
            console.log('Method name [' + retrieve.fcn + '] is not defined');
            return shim.success();
        }
        try {
            let payload = await method(stub, retrieve.params);
            return shim.success(payload);
        } catch (err) {
            console.log(err);
            return shim.error(err);
        }
    }

    /**
     * -1- user 등록
     * @param {Object} stub 
     * @param {string[]} args 
     */
    async registerUser(stub, args) {

        if (args.length != 3) {
            throw new Error('ResisterUser :  Incorrect Number of arguments Expecting 3, but received input args.length : ' + args.length);
        }
        let compositeKey = stub.createCompositeKey("Asset.", [args[0]]);

        let duplicateCheck = await stub.getState(compositeKey);
        //get을 통해 가져와서 등록전에 똑같은애가 있는지를 확인한다.
        var isExist = function (value) {
            if (value == "" || value == null || value == undefined ||
                (value != null && typeof value == "object" && !Object.keys(value).length)) {
                return true;
            } else {
                //값이 들어있으면 여기
                return false;
            }
        };
        if (isExist(duplicateCheck) != true) {
            throw new Error('ResisterUser : AssetID' + compositeKey + 'is already registered.');
        }

        var userInfo = {
            userID: '',
            userPW: '',
            auth: '',
            refundAmount: '',
            totalDonationAmount: 0,
            participatedDanationProjects: [],
            organizedDanationProject: [],
            createdAt:'',
        }
        userInfo.userID = args[0];
        userInfo.userPW = args[1];
        userInfo.auth = args[2];

        let txTimestamp = stub.getTxTimestamp();
        let tsSec = txTimestamp.seconds;
        let tsSecValue = tsSec.low + (540 * 60);
        let dataTimeObj = new Date(tsSecValue * 1000);
        var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

        userInfo.createdAt = timestampString;

        await stub.putState(compositeKey, Buffer.from(JSON.stringify(userInfo)));
    }
    /**
     * -2- donationPJT 등록
     * @param {Object} stub
     * @param {string[]} args
     */
    async registerDonationProject(stub, args) {

        //추가할 부분 user에 해당 프로젝트를 추가해야된다.
        
        if (args.length != 4) { //여기에 들어올 args 숫자를 명시해서 처리해준다. 일단 4
            throw new Error('Incorrect Number of arguments Expecting 4, but received input args.length : ' + args.length);
        }
        let compositeKey = stub.createCompositeKey("Asset.", [args[0]]);
        //이거 "Asset." 그렇게 엄처나게 큰 의미가 있는것은아니고 ObjectType을 명시하기 위함인데
        //뒤의 arg와 합처저서 복합키를 많어 낸다.
        console.log("T01");
        let duplicateCheck = await stub.getState(compositeKey);
        //get을 통해 가져와서 등록전에 똑같은애가 있는지를 확인한다.
        console.log("T02");
        var isExist = function (value) {
            console.log("T03");
            if (value == "" || value == null || value == undefined ||
                (value != null && typeof value == "object" && !Object.keys(value).length)) {
                return true;
            } else {
                //값이 들어있으면 여기
                return false;
            }
        };
        console.log("T04");
        if (isExist(duplicateCheck) != true) {
            throw new Error('AssetID' + compositeKey + 'is already registered.');
        }
        console.log("T05");
        let donationProjectInfo = {
            projecttID: '',
            organizer: '',
            createdAt: '',
            targetAmount: '',
            currentAmount: '',
            participants: [],
            fundraisingEndDate: '',
            state: ''
        };
        console.log("T06");
        donationProjectInfo.projecttID = args[0];
        donationProjectInfo.organizer = args[1];
        donationProjectInfo.targetAmount = args[2];
        donationProjectInfo.fundraisingEndDate = args[3];
        donationProjectInfo.state = 'ProjectRegistered';

        let txTimestamp = stub.getTxTimestamp();
        let tsSec = txTimestamp.seconds;
        let tsSecValue = tsSec.low + (540 * 60);
        let dataTimeObj = new Date(tsSecValue * 1000);
        var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

        donationProjectInfo.createdAt = timestampString;
        console.log("T07");
        console.log(donationProjectInfo);
        console.log(Buffer.from(JSON.stringify(donationProjectInfo)));
        await stub.putState(compositeKey, Buffer.from(JSON.stringify(donationProjectInfo)));
    }

    /**
     * -3- donation start
     * @param {Object} stub 
     * @param {string[]} args 
     */
    async startDonationProject(stub, args) {
        //args = [projecttID, organizer]
        if (args.length != 3) {
            throw new Error('startDonationProject :  Incorrect Number of arguments Expecting 3, but received input args.length : ' + args.length);
        }
        let searchKey = stub.createCompositeKey('Asset.', [args[0]]);//projectID를 넣고 찾는다.

        let projectUpdated = await stub.getState(searchKey); //렛저에서 해당하는 부분을 가져온다.
        let projectInfo = JSON.parse(projectUpdated); //파싱을 통해 사용할 수 있는 형태로 변환

        if (projectInfo.state !== 'ProjectRegistered') {
            throw new Error('Unavailable project: Project has not been registered.');
        } else if (projectInfo.state === 'ProjectDeleted') {
            throw new Error('Unavailable project: Project has already been deleted.');
        } else if (parseInt(projectInfo.targetAmount) <= parseInt(projectInfo.currentAmount)) { //목표 금액을 달성하지 못하면 Exception 발생
            throw new Error('Unavailable project: Project has not filled targetAmount.')
        } else {

            let txTimestamp = stub.getTxTimestamp();
            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);
            let dataTimeObj = new Date(tsSecValue * 1000);
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            projectInfo.organizer = args[1];
            projectInfo.createdAt = timestampString;
            projectInfo.state = 'ProjectStarted';

            await stub.putState(searchkey, Buffer.from(JSON.stringify(projectInfo)));
        }
    }//startDonationProject-end

    /**
     * -4- donate
     * @param {Object} stub 
     * @param {string[]} args 
     * [userID, projectID, amount]
     */
    async donateToProject(stub, args) {
        if (args.length !== 3) {
            throw new Error('donateToProject :  Incorrect Number of arguments Expecting 3, but received input args.length : ' + args.length);
        }
        //유저 찾기
        let searchUserKey = stub.createCompositeKey('Asset.', [args[0]]);
        let userUpdated = await stub.getState(searchUserKey);
        let userInfo = JSON.parse(userUpdated);

        //프로젝트 찾고 검사
        let searchProjectKey = stub.createCompositeKey('Asset.', [args[1]]);
        let projectUpdated = await stub.getState(searchProjectKey);
        let projectInfo = JSON.parse(projectUpdated);

        if (projectInfo.state !== 'ProjectRegistered') {
            throw new Error('Unavailable project: Project has not been registered.');
        } else if (projectInfo.state === 'ProjectDeleted') {
            throw new Error('Unavailable project: Project has already been deleted.');
        } else {
            
            console.log(`participatedDanationProjects :  ${userInfo.participatedDanationProjects}`);
            console.log(`participatedDanationProjects.length : ${userInfo.participatedDanationProjects.length}`);
            let isParticipated = userInfo.participatedDanationProjects.findIndex((item, index)=>{return item === args[1] }) >= 0;
            
            if(isParticipated===false){
                userInfo.participatedDanationProjects.push(args[1]);
                projectInfo.participants.push(args[0]);
            }
            userInfo.totalDonationAmount =  parseInt(userInfo.totalDonationAmount) + parseInt(args[2]);
            projectInfo.currentAmount = parseInt(projectInfo.currentAmount) + parseInt(args[2]);
            
            let txTimestamp = stub.getTxTimestamp();
            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);
            let dataTimeObj = new Date(tsSecValue * 1000);
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            projectInfo.createdAt = timestampString;
            userInfo.createdAt = timestampString;

            await stub.putState(searchUserKey, Buffer.from(JSON.stringify(userInfo)));
            await stub.putState(searchProjectKey, Buffer.from(JSON.stringify(projectInfo)));
        }
    }//donateToProject-end



    /**
     * -query-
     * @param {Object} stub 
     * @param {string[]} args 
     * @return {string}
     * <userID, projectID...> length 1
     */
    async query(stub, args){
        
        let searchKey = stub.createCompositeKey('Asset.',[args[0]]);
        let asset = await stub.getState(searchKey);
        console.log(asset);

        return asset;
    }

}//class-end

shim.start(new Chaincode());