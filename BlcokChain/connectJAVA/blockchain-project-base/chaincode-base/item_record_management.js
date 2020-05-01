const shim = require('fabric-shim');


var Chaincode = class {
    /**
     * Chaincode Instantiation method.
     * @param {Object} stub 
     * @return {SuccessResponse} shim.success() returns a standard response object with status code 200 and an optional payload.
     */
    async Init(stub) {
        console.info('Instantiated completed');
        return shim.success();
    }

    /**
     * Chaincode Invoking method.
     * @param {Object} stub The chaincode object
     * @return {SuccessResponse} status code and optional payload
     */
    async Invoke(stub) {

        /** Get method name and parameter from the chaincode arguments */
        let ret = stub.getFunctionAndParameters();
        let method = this[ret.fcn];

        /** Undefined method calling exception(but do not throw error) */
        if (!method) {
            console.log('Method name [' + ret.fcn + '] is not defined');
            return shim.success();
        }

        /** Method call */
        try {
            let payload = await method(stub, ret.params);
            return shim.success(payload);

        } catch (err) {
            console.log(err);
            return shim.error(err);
        }
    }


    /**
     * Add a record of a new secondhand item registration 
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID 
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID, args[1]: recorder
     */
    async registerItem(stub, args) {

        /** Inappropriate argument exception */
        if (args.length != 2) {
            throw new Error('Incorrect number of arguments. Expecting 2, but received ' + args.length);
        }

        /** !!! Generate composite key !!! */
        let compositeKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Duplicated asset checking */
        let dupCheck = await stub.getState(compositeKey);

        var isExist = function(value) {
            if (value == "" || value == null || value == undefined ||
                (value != null && typeof value == "object" && !Object.keys(value).length)) {
                return true;
            } else {
                return false;
            }
        };

        if (isExist(dupCheck) != true) {
            throw new Error('AssetID ' + compositeKey + 'is already registered.');
        }

        /** Get transaction timestampe using 'stub' */
        let txTimestamp = stub.getTxTimestamp();
        /** Timestamp formatting 'YYYY-MM-DD HH:MM:SS' */

        let tsSec = txTimestamp.seconds;
        let tsSecValue = tsSec.low + (540 * 60);
        let dataTimeObj = new Date(tsSecValue * 1000);

        // let timestampString;
        // timestampString = dataTimeObj.getFullYear() + '-' + ('0' + (dataTimeObj.getMonth() + 1)).slice(-2) + '-' +
        //     ('0' + dataTimeObj.getDate()).slice(-2) + ' ' + (dataTimeObj.getHours() + 9) + ':' + ('0' + dataTimeObj.getMinutes()).slice(-2) + ':' + dataTimeObj.getSeconds();
        var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();



        /** Consist asset information structure */
        var itemInfo = {
            assetID: args[0],
            recorder: args[1],
            createdAt: timestampString,
            state: 'ItemRegistered'
        };

        /** Put the asset information */
        await stub.putState(compositeKey, Buffer.from(JSON.stringify(itemInfo)));

    }

    /**
     * Add a record of item de-registration 
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID 
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID, args[1]: recorder
     */
    async deregisterItem(stub, args) {

        /** Inappropriate argument exception */
        if (args.length != 2) {
            throw new Error('Incorrect number of arguments. Expecting 2, but received' + args.length);
        }

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get state of the asset */
        let itemDeregistered = await stub.getState(searchKey);
        let itemInfo = JSON.parse(itemDeregistered);

        /** Get transaction timestamp using 'stub' */
        let txTimestamp = stub.getTxTimestamp();

        let tsSec = txTimestamp.seconds;
        let tsSecValue = tsSec.low + (540 * 60);

        let dataTimeObj = new Date(tsSecValue * 1000);

        // let timestampString;
        // timestampString = dataTimeObj.getFullYear() + '-' + ('0' + (dataTimeObj.getMonth() + 1)).slice(-2) + '-' +
        //     ('0' + dataTimeObj.getDate()).slice(-2) + ' ' + (dataTimeObj.getHours() + 9) + ':' + ('0' + dataTimeObj.getMinutes()).slice(-2) + ':' + dataTimeObj.getSeconds();
        var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

        /** Update asset information
         *  recorder, createdAt, state
         *  state shuld be 'ItemDeleted'
         */
        itemInfo.recorder = args[1];
        itemInfo.createdAt = timestampString;
        itemInfo.state = 'ItemDeleted'

        /** Put the udpated asset information */
        await stub.putState(searchKey, Buffer.from(JSON.stringify(itemInfo)));

    }

    /**
     * Add a record that item's auction started 
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID 
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID, args[1]: recorder
     */
    async startAuction(stub, args) {

        /** Inappropriate argument exception */
        if (args.length != 2) {
            throw new Error('Incorrect number of arguments. Expecting 2, but received' + args.length);
        }

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get state of the asset */
        let itemUpdated = await stub.getState(searchKey);
        let itemInfo = JSON.parse(itemUpdated);

         /** asset state validity checking */
        if (itemInfo.state != 'ItemRegistered') {
            throw new Error('Unavailable asset: Item has not been registered.');
        } else if (itemInfo.state == 'ItemDeleted') {
            throw new Error('Unavailable asset: Item has already been deleted.');
        } else {
            /** Get transaction timestamp using 'stub' */
            let txTimestamp = stub.getTxTimestamp();

            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);

            let dataTimeObj = new Date(tsSecValue * 1000);

            // let timestampString;
            // timestampString = dataTimeObj.getFullYstear() + '-' + ('0' + (dataTimeObj.getMonth() + 1)).slice(-2) + '-' +
            //     ('0' + dataTimeObj.getDate()).slice(-2) + ' ' + (dataTimeObj.getHours() + 9) + ':' + ('0' + dataTimeObj.getMinutes()).slice(-2) + ':' + dataTimeObj.getSeconds();
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            /** Update asset information
             *  recorder, createdAt, statesp
             *  state should be 'AuctionStarted'
             */
            itemInfo.recorder = args[1];
            itemInfo.createdAt = timestampString;
            itemInfo.state = 'AuctionStarted'

            /** Put the udpated asset information */
            await stub.putState(searchKey, Buffer.from(JSON.stringify(itemInfo)));
        }
    }

    /**
     * Add a record that item's auction ended 
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID 
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID, args[1]: recorder
     */
    async endAuction(stub, args) {

        /** Inappropriate argument exception */
        if (args.length != 2) {
            throw new Error('Incorrect number of arguments. Expecting 2, but received' + args.length);
        }

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get state of the asset */
        let itemUpdated = await stub.getState(searchKey);
        let itemInfo = JSON.parse(itemUpdated);

        if (itemInfo.state != 'AuctionStarted') {
            throw new Error('Unavailable asset: No relevant record.');
        } else {
            /** Get transaction timestamp using 'stub' */
            let txTimestamp = stub.getTxTimestamp();

            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);

            let dataTimeObj = new Date(tsSecValue * 1000);

            // let timestampString;
            // timestampString = dataTimeObj.getFullYear() + '-' + ('0' + (dataTimeObj.getMonth() + 1)).slice(-2) + '-' +
            //     ('0' + dataTimeObj.getDate()).slice(-2) + ' ' + (dataTimeObj.getHours() + 9) + ':' + ('0' + dataTimeObj.getMinutes()).slice(-2) + ':' + dataTimeObj.getSeconds();
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            /** Update asset information
             *  recorder, createdAt, state
             *  state should be 'AuctionEnded'
             */
            itemInfo.recorder = args[1];
            itemInfo.createdAt = timestampString;
            itemInfo.state = 'AuctionEnded'

            /** Put the udpated asset information */
            await stub.putState(searchKey, Buffer.from(JSON.stringify(itemInfo)));
        }
    }

    /**
     * Add a record that item's auction cancelled
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID 
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID, args[1]: recorder
     */
    async cancelAuction(stub, args) {

        /** Inappropriate argument exception */
        if (args.length != 2) {
            throw new Error('Incorrect number of arguments. Expecting 2, but received' + args.length);
        }

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get state of the asset */
        let itemUpdated = await stub.getState(searchKey);
        let itemInfo = JSON.parse(itemUpdated);

        if (itemInfo.state != 'AuctionStarted') {
            throw new Error('Unavailable asset: No relevant record.');
        } else {
            /** Get transaction timestamp using 'stub' */
            let txTimestamp = stub.getTxTimestamp();

            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);

            let dataTimeObj = new Date(tsSecValue * 1000);

            // let timestampString;
            // timestampString = dataTimeObj.getFullYear() + '-' + ('0' + (dataTimeObj.getMonth() + 1)).slice(-2) + '-' +
            //     ('0' + dataTimeObj.getDate()).slice(-2) + ' ' + (dataTimeObj.getHours() + 9) + ':' + ('0' + dataTimeObj.getMinutes()).slice(-2) + ':' + dataTimeObj.getSeconds();
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            /** Update asset information
             *  recorder, createdAt, state
             *  state should be 'AuctionEnded'
             */
            itemInfo.recorder = args[1];
            itemInfo.createdAt = timestampString;
            itemInfo.state = 'AuctionCancelled'

            /** Put the udpated asset information */
            await stub.putState(searchKey, Buffer.from(JSON.stringify(itemInfo)));
        }
    }

    /**
     * Add a record that item's shipping start delevery.
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID 
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID, args[1]: recorder
     */
    async startDelivery(stub, args) {

        /** Inappropriate argument exception */
        if (args.length != 2) {
            throw new Error('Incorrect number of arguments. Expecting 2, but received' + args.length);
        }

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get state of the asset */
        let itemUpdated = await stub.getState(searchKey);
        let itemInfo = JSON.parse(itemUpdated);

        if (itemInfo.state != 'AuctionEnded') {
            throw new Error('Unavailable asset: No relevant record.');
        } else {
            /** Get transaction timestamp using 'stub' */
            let txTimestamp = stub.getTxTimestamp();

            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);

            let dataTimeObj = new Date(tsSecValue * 1000);

            // let timestampString;
            // timestampString = dataTimeObj.getFullYear() + '-' + ('0' + (dataTimeObj.getMonth() + 1)).slice(-2) + '-' +
            //     ('0' + dataTimeObj.getDate()).slice(-2) + ' ' + (dataTimeObj.getHours() + 9) + ':' + ('0' + dataTimeObj.getMinutes()).slice(-2) + ':' + dataTimeObj.getSeconds();
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            /** Update asset information
             *  recorder, createdAt, state
             *  state should be 'OnDelivery'
             */
            itemInfo.recorder = args[1];
            itemInfo.createdAt = timestampString;
            itemInfo.state = 'OnDelivery'

            /** Put the udpated asset information */
            await stub.putState(searchKey, Buffer.from(JSON.stringify(itemInfo)));
        }
    }

    /**
     * Add a record that buyer confirmed the item purchase.
     * Mandatory requirement: Create a composite key for state recording.
     * Composite key structure: Asset.assetID 
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID, args[1]: recorder
     */
    async confirmItem(stub, args) {

        /** Inappropriate argument exception */
        if (args.length != 2) {
            throw new Error('Incorrect number of arguments. Expecting 2, but received' + args.length);
        }

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get state of the asset */
        let itemUpdated = await stub.getState(searchKey);
        let itemInfo = JSON.parse(itemUpdated);

        if (itemInfo.state != 'OnDelivery') {
            throw new Error('Unavailable asset: No relevant record.');
        } else {
            /** Get transaction timestamp using 'stub' */
            let txTimestamp = stub.getTxTimestamp();

            let tsSec = txTimestamp.seconds;
            let tsSecValue = tsSec.low + (540 * 60);

            let dataTimeObj = new Date(tsSecValue * 1000);

            // let timestampString;
            // timestampString = dataTimeObj.getFullYear() + '-' + ('0' + (dataTimeObj.getMonth() + 1)).slice(-2) + '-' +
            //     ('0' + dataTimeObj.getDate()).slice(-2) + ' ' + (dataTimeObj.getHours() + 9) + ':' + ('0' + dataTimeObj.getMinutes()).slice(-2) + ':' + dataTimeObj.getSeconds();
            var timestampString = dataTimeObj.toISOString().replace(/T|Z|\.\d{3}/g, ' ').trim();

            /** Update asset information
             *  recorder, createdAt, state
             *  state should be 'Complete'
             */
            itemInfo.recorder = args[1];
            itemInfo.createdAt = timestampString;
            itemInfo.state = 'Complete'

            /** Put the udpated asset information */
            await stub.putState(searchKey, Buffer.from(JSON.stringify(itemInfo)));
        }
    }

    /**
     * Get digital asset information.
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID
     * @return {string} The asset information of assetID
     */
    async query(stub, args) {

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset." , [args[0]]);

        /** Get state */
        let asset = await stub.getState(searchKey);
        console.info(asset);
        /** Return asset state */
        return asset;
    }s


    /**
     * Get item's history of state
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID
     * @return {string} The history of records
     */
    async getItemHistory(stub, args) {

        /** Inappropriate argument exception */
        if (args.length != 1) {
            throw new Error('Incorrect number of arguments. Expecting assetID as an argument');
        }

        /** !!! Generate composite key !!! */
        let searchKey = stub.createCompositeKey("Asset.", [args[0]]);

        /** Get the history of state */
        var historyIter = await stub.getHistoryForKey(searchKey);

        /** Copy the history to array and parse to string*/
        let results = [];
        let res = { done: false };

        while (!res.done) {
            res = await historyIter.next();

            try {

                if (res && res.value && res.value.value) {
                    let val = res.value.value.toString('utf8');

                    if (val.length > 0) {
                        results.push(JSON.parse(val));
                        console.info(JSON.parse(val));
                    }
                }

            } catch (err) {
                console.info(err);
            }

            if (res && res.done) {
                try {
                    historyIter.close();
                } catch (err) {
                    console.info(err);
                }
            }
        }

        /** Return the history as string */
        return Buffer.from(JSON.stringify(results));
    }

};

/** Start chaincode */
shim.start(new Chaincode());