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
            refundAmount: 0,
            refundDonations: [],
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
        
        console.log("=================================");
        console.log(userInfo);
        console.log("=================================");
        console.log(JSON.stringify(userInfo));
        console.log("=================================");
        console.log(Buffer.from(JSON.stringify(userInfo)));
        
        return await stub.putState(compositeKey, Buffer.from(JSON.stringify(userInfo)));
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

        if (args[4].split(',').length !== args[5].split(',').length) {
            throw new Error(`projectGoals and goalAmounts is not equals length`);
        }
        if (args[4].split(',').length == 0) {
            throw new Error(`donationProject must have one or more goals`);
        }
        let donationProjectInfo = {
            projecttID: '',
            organizer: '',
            createdAt: '',
            targetAmount: '0',
            currentAmount: '0',
            participants: [],
            projectGoalMap: null,
            fundraisingEndDate: '',
            state: ''
        };
        
        let projectGoalMap = new Map();
        const goalAmounts = args[5].split(',');
        
        const goalNames = args[4].split(',');
        for(let i = 0; i < goalNames.length; i++){
            console.log(`i : ${i} / goalNames[i] : ${goalNames[i]} / ${goalAmounts[i]}`);
            projectGoalMap.set( goalNames[i], {'amount': goalAmounts[i], 'compelete': false, 'contents': null });
        }
        donationProjectInfo.projecttID = args[0];
        donationProjectInfo.organizer = args[1];
        donationProjectInfo.targetAmount = args[2];
        donationProjectInfo.fundraisingEndDate = args[3];
        donationProjectInfo.state = 'ProjectRegistered';
        donationProjectInfo.projectGoalMap = JSON.stringify(Array.from( projectGoalMap.entries()));

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
     * [projectID, organizerID]
     */
    async startDonationProject(stub, args) {
        //args = [projecttID, organizer]
        if (args.length != 2) {
            throw new Error('startDonationProject :  Incorrect Number of arguments Expecting 3, but received input args.length : ' + args.length);
        }
        let searchKey = stub.createCompositeKey('DonationProject.ID.', [args[0]]);//projectID를 넣고 찾는다.

        let projectUpdated = await stub.getState(searchKey); //렛저에서 해당하는 부분을 가져온다.
        let projectInfo = JSON.parse(projectUpdated); //파싱을 통해 사용할 수 있는 형태로 변환

        if (projectInfo.state !== 'ProjectRegistered') {
            throw new Error('Unavailable project: Project has not been registered.');
        } else if (projectInfo.state === 'ProjectDeleted') {
            throw new Error('Unavailable project: Project has already been deleted.');
        } else if (parseInt(projectInfo.targetAmount) > parseInt(projectInfo.currentAmount)) { //목표 금액을 달성하지 못하면 Exception 발생
            throw new Error('Unavailable project: Project has not filled targetAmount.')
        } else if (projectInfo.organizer != args[1]){
            throw new Error(`Unavailable project: ${args[1]} is not organizer`);
        } 
        else {
            let txTimestamp = stub.getTxTimestamp();
            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);
            let dataTimeObj = new Date(tsSecValue * 1000);
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            //TODO-1 조건이 빠젔다. organizer 인증 부분이 빠졌다.

            projectInfo.createdAt = timestampString;
            projectInfo.state = 'ProjectStarted';

            await stub.putState(searchKey, Buffer.from(JSON.stringify(projectInfo)));
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
        let compositeDonationKey = stub.createCompositeKey('Donation.Account.', [args[0], args[1]]);


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
                amount: 0,
                state: '',
                createdAt: '',
            }
            if (isExist(updateDonation) != true) {
                //값이 있으면 업데이트 처리해주어야한다.
                //키값에 저장된 데이터만 업데이트 처리해 주면된다.
                let stateDonationInfo = JSON.parse(updateDonation);
                donationInfo.amount = stateDonationInfo.amount;
                donationInfo.state = stateDonationInfo.state;

                donationInfo.amount = parseInt(donationInfo.amount) + parseInt(args[2]);
                donationInfo.state = 'donated';

            } else {
                userInfo.participatedDonationProjects.push(args[1]);
                projectInfo.participants.push(args[0]);
                donationInfo.amount = parseInt(donationInfo.amount) + parseInt(args[2]);
                donationInfo.state = 'donated';
            }
            
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
        let projectGoalMap = new Map(JSON.parse(projectInfo.projectGoalMap));

        if (projectInfo.organizer !== args[1]) {
            throw new Error(`Incorrect organizer of project! ${args[1]} is not organizer this project `);
        }
        if (!(projectGoalMap.has(args[2]))) {
            throw new Error(`project doesn't have goal which name is ${args[2]}`);
        }
        if (projectGoalMap.get(args[2]).compelete) {
            throw new Error(`${args[2]} is already compelete`);
        }
        if (projectInfo.state === 'ProjectRegistered') {
            throw new Error('Unavailable project: Project has not been registered.');
        } else if (projectInfo.state === 'ProjectDeleted') {
            throw new Error('Unavailable project: Project has already been deleted.');
        } else if(!projectGoalMap.has(args[2])){
            throw new Error(`Unavailable project: Project do not have ${args[2]}`);
        } else {
            projectGoalMap.set(args[2], { 'amount': projectGoalMap.get(args[2]).amount, 'compelete': true, 'contents': args[3] });
            projectInfo.projectGoalMap = JSON.stringify(Array.from( projectGoalMap.entries()));
            await stub.putState(searchProjectKey, Buffer.from(JSON.stringify(projectInfo)));
        }
    }

    /**
     * -6- donationPJT 종료
     * @param {Object} stub 
     * @param {string[]} args 
     * [projecttID, organizer]
     */
    
    async endDonationProject(stub, args) {
        if (args.length !== 2) {
            throw new Error('Incorrect Number of arguments Expecting 2, but received input args.length : ' + args.length);
        }
        let searchProjectKey = stub.createCompositeKey('DonationProject.ID.', [args[0]]);
        let projectUpdated = await stub.getState(searchProjectKey);
        let projectInfo = JSON.parse(projectUpdated);
        let projectGoalMap = new Map(JSON.parse(projectInfo.projectGoalMap));

        if (projectInfo.organizer !== args[1]) {
            throw new Error(`${args[1]} is not organizer about ${args[0]}`);
        }
        for (let [key, value] of projectGoalMap) {
            if (!value.compelete) {
                throw new Error(`${args[0]} doesn't conelete all goals`);
            }
        }
        if (projectInfo.state === 'ProjectRegistered') {
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
        //총 환불금만 더해주고 환불 내역을 따로 저장을 해야한다.
        //유저 환불 정보 생성
        //도네이션을 불러와서 그 도네이션에 대한 것을 환불을 해야겠지
        
        
        let compositeRefund = stub.createCompositeKey('User.Refund.', [args[0], args[1]]);
        let duplicateCheck = await stub.getState(compositeRefund);
        //유저 찾기
        let searchUserKey = stub.createCompositeKey('User.Account.', [args[0]]);
        let userUpdated = await stub.getState(searchUserKey);
        let userInfo = JSON.parse(userUpdated);

        //프로젝트 찾기
        let searchProjectKey = stub.createCompositeKey('DonationProject.ID.', [args[1]]);
        let projectUpdated = await stub.getState(searchProjectKey);
        let projectInfo = JSON.parse(projectUpdated);

        //도네이션 찾기
        let searchDonationProject = stub.createCompositeKey('Donation.Account.', [args[0], args[1]]);
        let donationUpdated = await stub.getState(searchDonationProject);
        let donationInfo = JSON.parse(donationUpdated);


        //애들 다 찾았으면

        let refundInfo = {
            amount: 0,
            state: '',
            createdAt: ''
        }

        //1. 검사
        let isExist = function (value) {
            if (value == "" || value == null || value == undefined ||
                (value != null && typeof value == "object" && !Object.keys(value).length)) {
                return true;
            } else {
                //값이 들어있으면 여기
                return false;
            }
        };
        if (isExist(donationInfo)) {
            throw new Error(`${args[0]} haven't been donated ${args[1]}`);
        }
        if (!(isExist(duplicateCheck))) {
            // throw new Error(`${args[1]} already refund about ${args[1]}`);
            let duplicatInfo = JSON.parse(duplicateCheck); 
            refundInfo = duplicatInfo;
        }else{
            userInfo.refundDonations.push(args[1]);
        }
        // if(donationInfo.state === 'Refunded'){ //한번 환불했어도 또 할 수 있어야하니까
        // throw new Error(`${args[1]} already refund about ${args[1]}`);
        // }
        if(projectInfo.state!=='ProjectRegistered'){
            throw new Error('Unavailable project: Project is not status registered.');
        }

        //2. 유저 정보 변경
        userInfo.totalDonationAmount = parseInt(userInfo.totalDonationAmount) - parseInt(donationInfo.amount);
        userInfo.refundAmount = parseInt(userInfo.refundAmount) + parseInt(donationInfo.amount);

        //3. 프로젝트 정보 변경
        projectInfo.currentAmount = parseInt(projectInfo.currentAmount) - parseInt(donationInfo.amount);

        //4. 도네이션 정보 변경
        donationInfo.state = 'Refunded';
        donationInfo.amount = 0;
        //5. 환불 정보입력

        refundInfo.amount = parseInt(refundInfo.amount) + parseInt(donationInfo.amount);
        refundInfo.state = 'Required';

        let txTimestamp = stub.getTxTimestamp();
        let tsSec = txTimestamp.seconds;
        let tsSecValue = tsSec.low + (540 * 60);
        let dataTimeObj = new Date(tsSecValue * 1000);
        var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

        refundInfo.createdAt = timestampString;

        //6. 업데이트 putstate
        await stub.putState(searchUserKey, Buffer(JSON.stringify(userInfo)));
        await stub.putState(searchProjectKey, Buffer(JSON.stringify(projectInfo)));
        await stub.putState(searchDonationProject, Buffer(JSON.stringify(donationInfo)));
        await stub.putState(compositeRefund, Buffer(JSON.stringify(refundInfo)));

    }

    /**
     * -8- 환불 완료
     * @param {Object} stub 
     * @param {string[]} args 
     * [userID]
     */
    async compeleteRefund(stub, args) {
        if (args.length !== 2) {
            throw new Error('Incorrect Number of arguments Expecting 2, but received input args.length : ' + args.length);
        }
        //일단 내비둬

        //유저 찾기
        let searchUserKey = stub.createCompositeKey('User.Account.', [args[0],]);
        let userUpdated = await stub.getState(searchUserKey);
        let userInfo = JSON.parse(userUpdated);
        if (userInfo.refundAmount == 0) {
            throw new Error(`${args[0]}'s refundAmount required is 0`);
        }
        //환불 내역 찾기
        let updateRefundList = [];

        for (let i = 0; i < userInfo.refundDonations; i++) {
            let value = userInfo.refundDonations[i];
            let searchRefundKey = stub.createCompositeKey('User.Refund.', [args[0], value]);
            let checkInfo = await stub.getState(searchRefundKey);

            if (checkInfo.state === 'Required') {
                updateRefundList.push([searchRefundKey, checkInfo]);
            }

        }
        if (updateRefundList.length === 0) {
            throw new Error(`${args[0]} doens't have any refund required`);
        }

        let txTimestamp = stub.getTxTimestamp();
        let tsSec = txTimestamp.seconds;
        let tsSecValue = tsSec.low + (540 * 60);
        let dataTimeObj = new Date(tsSecValue * 1000);
        var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();
        //complete로 바꾸기

        for (let i = 0; i < updateRefundList.length; i++) {
            let value = updateRefundList[i];
            userInfo.refundAmount = parseInt(userInfo.refundAmount) - value[1].amount;
            value[1].amount = 0;
            value[1].state = 'Compeleted';
            value[1].createdAt = timestampString;

            if (userInfo.refundAmount < 0) {
                throw new Error(`refundAmount can't be refundAmount<0`);
            }
            await stub.putState(value[0], Buffer(JSON.stringify(value[1])));

        }

        userInfo.refundDonations = [];
        await stub.putState(searchUserKey, Buffer(JSON.stringify(userInfo)));
    }

    /**
     * 
     * @param {Object} stub 
     * @param {string[]} args
     * [projectID]
     */
    async doNotCompeleteGoal(stub, args) {

        /** 프로젝트 찾고
        *    검사하고 이미 끝났는지? start중인지?
        *    현재 남은 잔고는 map을 돌면서 계산하고.
        *    사용자list를 통해서 donationkey를 찾아서 금액을 찾고 
        *    전체 금액의 %만큼 남은 잔고에서 환불해준다.
        *    refundkey를 만들어서 넣어주고 해당 유저의 정보도 업데이트를 해주어야한다.
        */

        if (args.length !== 1) {
            throw new Error('Incorrect Number of arguments Expecting 1, but received input args.length : ' + args.length);
        }

        //프로젝트 찾기
        let searchProjectKey = stub.createCompositeKey('DonationProject.ID.', [args[0]]);
        let updateProject = await stub.getState(searchProjectKey);
        let projectInfo = JSON.parse(updateProject);

        //검사 project state확인 오직 ProjectStarted 여야만 한다. ProjectRegistered, ProjectEnd
        if (projectInfo.state !== 'ProjectStarted' && projectInfo.state!== 'ProjectRegistered') {
            throw new Error(`To do doNotCompeleteGoal method, DonationProject state must be ProjectStarted. not ${projectInfo.state}`);
        }

        //잔고 계산
        let totalAmount = parseInt(projectInfo.currentAmount);
        //let projectBalance = parseInt(projectInfo.currentAmount);
        //let projectGoalMap = new Map(JSON.parse(projectInfo.projectGoalMap));
    
        //for (let [key, value] of projectGoalMap) {
          //  if (value.compelete) {
            //    projectBalance -= Math.ceil(totalAmount / (parseInt(value.amount)/100));
            //}
        //}
        //참여자 리스트를 순회하며, 전채 금액의 %를 확인하여 잔고의 %만큼 반환해준다.
        //또한 donationAccount 업데이트, refundkey 만들어서 putState해줘야한다.

        for (let i = 0; i < projectInfo.participants.length; i++) {
            let value = projectInfo.participants[i];
            //user찾기
            let searchUserKey = stub.createCompositeKey('User.Account.', [value]);
            let updateUserInfo = await stub.getState(searchUserKey);
            let userInfo = JSON.parse(updateUserInfo);

            //donation.Account. 찾기
            let searchDonationAccount = stub.createCompositeKey('Donation.Account.', [value, args[0]]);
            let updateDonationAccount = await stub.getState(searchDonationAccount);
            let donationAccount = JSON.parse(updateDonationAccount);

            //refundkey만들기
            let compositeRefund = stub.createCompositeKey('User.Refund.', [value, args[0]]);
            let duplicateCheck = await stub.getState(compositeRefund);
            

            let refundInfo = {
                amount: 0,
                state: '',
                createdAt: ''
            }

            // 검증 중복 등등
            let isExist = function (value) {
                if (value == "" || value == null || value == undefined ||
                    (value != null && typeof value == "object" && !Object.keys(value).length)) {
                    return true;
                } else {
                    //값이 들어있으면 여기
                    return false;
                }
            };
            if (isExist(donationAccount)) {
                throw new Error(`${args[0]} haven't been donated ${args[1]}`);
            }
            if (!(isExist(duplicateCheck))) {
                let duplicatInfo = JSON.parse(duplicateCheck);
                // throw new Error(`${args[1]} already refund about ${args[1]}`);
                refundInfo = duplicatInfo;
            }
            let percentAmount = donationAccount.amount;//Math.ceil((donationAccount.amount/totalAmount)*projectBalance);
            userInfo.totalDonationAmount = parseInt(userInfo.totalDonationAmount) - parseInt(percentAmount);
            userInfo.refundDonations.push(args[1]);
            userInfo.refundAmount = parseInt(userInfo.refundAmount) + parseInt(percentAmount);

            projectInfo.currentAmount = parseInt(projectInfo.currentAmount) - parseInt(percentAmount);

            donationAccount.state = 'Refunded';
            donationAccount.amount = '0';
            refundInfo.amount = parseInt(refundInfo.amount) + parseInt(percentAmount);
            refundInfo.state = 'Required';

            await stub.putState(searchUserKey, Buffer(JSON.stringify(userInfo)));
            await stub.putState(searchDonationAccount, Buffer(JSON.stringify(donationAccount)));
            await stub.putState(compositeRefund, Buffer(JSON.stringify(refundInfo)));
        }
	projectInfo.state='ProjectStopped';
        await stub.putState(searchProjectKey, Buffer(JSON.stringify(projectInfo)));
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
        
        if (args[0] === 'searchUser') {
            if (args.length !== 2) {
                throw new Error('query :  Incorrect Number of arguments Expecting 2, but received input args.length : ' + args.length);
            }
            searchKey = stub.createCompositeKey('User.Account.', [args[1]]);

        } else if (args[0] === 'searchDonationProject') {
            //DonationProject.ID.
            if (args.length !== 2) {
                throw new Error('query :  Incorrect Number of arguments Expecting 2, but received input args.length : ' + args.length);
            }
            searchKey = stub.createCompositeKey('DonationProject.ID.', [args[1]]);
        } else if(args[0] === 'searchDonation'){
            if (args.length !== 3) {
                throw new Error('query :  Incorrect Number of arguments Expecting 3, but received input args.length : ' + args.length);
            }
            //[userID, projectID]
            searchKey = stub.createCompositeKey('Donation.Account.',[args[1], args[2]]);
        } else if(args[0] === 'searchRefundation'){
            if (args.length !== 3) {
                throw new Error('query :  Incorrect Number of arguments Expecting 3, but received input args.length : ' + args.length);
            }
            //[userID, projectID]
            searchKey = stub.createCompositeKey('User.Refund.',[args[1], args[2]]);
        } else {
            throw new Error(`query don't have ${args[0]}`);
        }
        let value = await stub.getState(searchKey);
        console.log(value);

        return value;
    }

}//class-end

shim.start(new Chaincode());
