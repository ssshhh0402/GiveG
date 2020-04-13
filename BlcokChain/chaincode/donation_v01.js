const shim = require('fabric-shim');

/**
 * 1. user 등록
 * 2. donationPJT 등록 == 모금 시작
 * 3. donationPJT 시작 == 모금이 완료되고 시작됨을 알림
 * 4. donate  -----------> query 구현
 * 5. donationPJT 목표 완료 = 자료도 함꼐 포함해야 된다.
 *                                          6. donationPJT 모금 종료
 * 6. donationPJT 종료 
 * 7. 환불 신청 ------------------------------> User refundAmount에 추가
 * 8. 환불 완료 ------------------->카카오 페이 API를 통해 완료되면 User refundAmount update
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
        let compositeKey = stub.createCompositeKey("User.Account.", [args[0]]);

        let duplicateCheck = await stub.getState(compositeKey);
        //get을 통해 가져와서 등록전에 똑같은애가 있는지를 확인한다.
        let isExist = function (value) {
            if (value == "" || value == null || value == undefined ||
                (value != null && typeof value == "object" && !Object.keys(value).length)) {
                return true;
            } else {
                //값이 들어있으면 여기
                return false;
            }
        };
        if (isExist(duplicateCheck) != true) {
            throw new Error('ResisterUser : UserAccount' + compositeKey + 'is already registered.');
        }

        let userInfo = {
            userID: '',
            userPW: '',
            auth: '',
            refundAmount: '',
            totalDonationAmount: 0,
            participatedDonationProjects: [],
            organizedDonationProject: [],
            createdAt: '',
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
     * [projecttID, organizer, targetAmount, fundraisingEndDate, projectGoals, goalAmounts]
     * 
     * projectGoals는 배열 형태여야 하는데 들어오는 값은 string => splite(',') 현재 
     * goalAmounts는 projectGoals의 index와 맵핑 되어있는 변수로 goal별 금액을 저장 값은 string => splite(',') 현재 
     */
    async registerDonationProject(stub, args) {

        //추가할 부분 user에 해당 프로젝트를 추가해야된다.

        if (args.length != 6) { //여기에 들어올 args 숫자를 명시해서 처리해준다. 일단 4
            throw new Error('Incorrect Number of arguments Expecting 6, but received input args.length : ' + args.length);
        }
        let compositeKey = stub.createCompositeKey("DonationProject.ID.", [args[0]]);
        //이거 "Asset." 그렇게 엄처나게 큰 의미가 있는것은아니고 ObjectType을 명시하기 위함인데
        //뒤의 arg와 합처저서 복합키를 많어 낸다.

        let duplicateCheck = await stub.getState(compositeKey);
        //get을 통해 가져와서 등록전에 똑같은애가 있는지를 확인한다.

        let isExist = function (value) {

            if (value == "" || value == null || value == undefined ||
                (value != null && typeof value == "object" && !Object.keys(value).length)) {
                return true;
            } else {
                //값이 들어있으면 여기
                return false;
            }
        };
        if (isExist(duplicateCheck) != true) {
            throw new Error('DonationProjectID' + compositeKey + 'is already registered.');
        }

        if (args[4].length.split(',').length !== args[5].split(',').length) {
            throw new Error(`projectGoals and goalAmounts is not equals length`);
        }
        if (args[4].length.split(',').length != 0) {
            throw new Error(`donationProject must have one or more goals`);
        }
        let donationProjectInfo = {
            projecttID: '',
            organizer: '',
            createdAt: '',
            targetAmount: '',
            currentAmount: '',
            participants: [],
            projectGoalMap: null,
            fundraisingEndDate: '',
            state: ''
        };

        projectGoalMap = new Map();
        const goalAmounts = args[5].split(',');
        args[4].split(',').forEach((item, index) => {
            projectGoalMap.set(item, { 'amount': goalAmounts[index], 'compelete': false, 'contents': null });
        });

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
        let searchKey = stub.createCompositeKey('DonationProject.ID.', [args[0]]);//projectID를 넣고 찾는다.

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
        let searchUserKey = stub.createCompositeKey('User.Account.', [args[0]]);
        let userUpdated = await stub.getState(searchUserKey);
        let userInfo = JSON.parse(userUpdated);

        //프로젝트 찾고 검사
        let searchProjectKey = stub.createCompositeKey('DonationProject.ID.', [args[1]]);
        let projectUpdated = await stub.getState(searchProjectKey);
        let projectInfo = JSON.parse(projectUpdated);

        //donation key 생성
        let compositeDonationKey = stub.createCompositeKey('Donation.Account', [args[0], args[1]]);


        if (projectInfo.state !== 'ProjectRegistered') {
            throw new Error('Unavailable project: Project has not been registered.');
        } else if (projectInfo.state === 'ProjectDeleted') {
            throw new Error('Unavailable project: Project has already been deleted.');
        } else {

            console.log(`participatedDonationProjects :  ${userInfo.participatedDonationProjects}`);
            console.log(`participatedDonationProjects.length : ${userInfo.participatedDonationProjects.length}`);

            ///여기 부분 아래를 싹 뜯어 고처야한다. 
            let updateDonation = await stub.getState(compositeDonationKey);
            //get을 통해 가져와서 등록전에 똑같은애가 있는지를 확인한다.
            let isExist = function (value) {
                if (value == "" || value == null || value == undefined ||
                    (value != null && typeof value == "object" && !Object.keys(value).length)) {
                    return true;
                } else {
                    //값이 들어있으면 여기
                    return false;
                }
            };
            let donationInfo = {
                amount : 0,
                state : '',
                createdAt : '',
            }
            if (isExist(updateDonation) != true) {
                //값이 있으면 업데이트 처리해주어야한다.
                //키값에 저장된 데이터만 업데이트 처리해 주면된다.
                let stateDonationInfo = JSON.parse(updateDonation);
                donationInfo.amount = stateDonationInfo.amount;
                donationInfo.state = stateDonationInfo.state;

            }else{   
                userInfo.participatedDonationProjects.push(args[1]);   
                projectInfo.participants.push(args[0]);
            }
            donationInfo.amount = parseInt(donationInfo.amount) + parseInt(args[2]);
            donationInfo.state = 'donated';
            

            userInfo.totalDonationAmount = parseInt(userInfo.totalDonationAmount) + parseInt(args[2]);
            projectInfo.currentAmount = parseInt(projectInfo.currentAmount) + parseInt(args[2]);

            let txTimestamp = stub.getTxTimestamp();
            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);
            let dataTimeObj = new Date(tsSecValue * 1000);
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            projectInfo.createdAt = timestampString;
            userInfo.createdAt = timestampString;
            donationInfo.createdAt = timestampString;

            await stub.putState(searchUserKey, Buffer.from(JSON.stringify(userInfo)));
            await stub.putState(searchProjectKey, Buffer.from(JSON.stringify(projectInfo)));
            await stub.putState(compositeDonationKey, Buffer.from(JSON.stringify(donationInfo)));
        }
    }//donateToProject-end

    /**
     * -5- donationPJT 목표 완료
     * @param {Object} stub 
     * @param {string[]} args 
     * [projecttID, organizer, goalID, contents ]
     */
    async compeleteGoalsDonationProject(stub, args) {
        if (args.length !== 4) {
            throw new Error('Incorrect Number of arguments Expecting 6, but received input args.length : ' + args.length);
        }
        let searchProjectKey = stub.createCompositeKey('DonationProject.ID.', [args[0]]);
        let projectUpdated = await stub.getState(searchProjectKey);
        let projectInfo = JSON.parse(projectUpdated);

        if (projectInfo.organizer !== args[1]) {
            throw new Error(`Incorrect organizer of project! ${args[1]} is not organizer this project `);
        }
        if (!(projectInfo.projectGoalMap.has(args[2]))) {
            throw new Error(`project doesn't have goal which name is ${args[2]}`);
        }
        if (projectInfo.projectGoalMap.get(args[2]).compelete) {
            throw new Error(`${args[2]} is already compelete`);
        }
        if (projectInfo.state !== 'ProjectRegistered') {
            throw new Error('Unavailable project: Project has not been registered.');
        } else if (projectInfo.state === 'ProjectDeleted') {
            throw new Error('Unavailable project: Project has already been deleted.');
        } else {
            projectInfo.projectGoalMap.set(args[2], { 'amount': projectInfo.projectGoalMap.get(args[2]).amount, 'compelete': true, 'contents': args[3] });
            await stub.putState(searchProjectKey, Buffer.from(JSON.stringify(projectInfo)));
        }
    }

    /**
     * -6- donationPJT 종료
     * @param {Object} stub 
     * @param {string[]} args 
     * [projecttID, userID]
     */
    async endDonationProject(stub, args) {
        if (args.length !== 2) {
            throw new Error('Incorrect Number of arguments Expecting 2, but received input args.length : ' + args.length);
        }
        let searchProjectKey = stub.createCompositeKey('DonationProject.ID.', [args[0]]);
        let projectUpdated = await stub.getState(searchProjectKey);
        let projectInfo = JSON.parse(projectUpdated);

        if (projectInfo.organizer !== args[1]) {
            throw new Error(`${args[1]} is not organizer about ${args[0]}`);
        }
        for (let [key, value] of projectInfo.projectGoalMap) {
            if (!value.compelete) {
                throw new Error(`${args[0]} doesn't conelete all goals`);
            }
        }
        if (projectInfo.state !== 'ProjectRegistered') {
            throw new Error('Unavailable project: Project has not been registered.');
        } else if (projectInfo.state === 'ProjectDeleted') {
            throw new Error('Unavailable project: Project has already been deleted.');
        } else {
            projectInfo.state = 'ProjectEnd';
            await stub.putState(searchProjectKey, Buffer.from(JSON.stringify(projectInfo)));
        }
    }

    /**
     * -7- 환불 신청 
     * @param {Object} stub 
     * @param {string[]} args 
     * [userID, projectID]
     */
    async requestRefund(stub, args) {
        if (args.length !== 2) {
            throw new Error('Incorrect Number of arguments Expecting 2, but received input args.length : ' + args.length);
        }
        //유저 찾기
        //UserID, projectID를 기반으로 dontion.Account찾아서 바꾸기 다른 부분 바꿀 필요없이
        
            
    }

    /**
     * 
     * @param {Object} stub 
     * @param {string[]} args 
     * [userID, refundAmount]
     */
    async compeleteRefund(stub, args) {
        if (args.length !== 2) {
            throw new Error('Incorrect Number of arguments Expecting 2, but received input args.length : ' + args.length);
        }
        // let searchUserKey = stub.createCompositeKey('User.Account.', )
    }

    //-==============================QUERY=============================
    /**
     * -query-
     * @param {Object} stub 
     * @param {string[]} args 
     * @return {string}
     * <userID, projectID...> length 1
     */
    async query(stub, args) {
        let searchKey = '';
        if (args.length !== 2) {
            throw new Error('query :  Incorrect Number of arguments Expecting 3, but received input args.length : ' + args.length);
        }
        if (args[0] === 'searchUser') {
            searchKey = stub.createCompositeKey('User.Account.', [args[0]]);

        } else if (args[0] === 'searchDonationProject') {
            //DonationProject.ID.
            searchKey = stub.createCompositeKey('User.Account.', [args[0]]);
        } else {
            throw new Error(`query don't have ${arg[0]}`);
        }
        let value = await stub.getState(searchKey);
        console.log(value);

        return value;
    }

}//class-end

shim.start(new Chaincode());