const shim = require('fabric-shim');


var Chaincode = class {
    /**
     * Chaincode Instantiation method.
     * @param {Object} stub 
     * @return {SuccessResponse} shim.success() returns a standard response object with status code 200 and an optional payload.
     */
    async Init(stub) {

    }

    /**
     * Chaincode Invoking method.
     * @param {Object} stub The chaincode object
     * @return {SuccessResponse} status code and optional payload
     */
    async Invoke(stub) {

        /** Get method name and parameter from the chaincode arguments */


        /** Undefined method calling exception(but do not throw error) */


        /** Method call */


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

        /** !!! Generate composite key !!! */

        /** Duplicated asset checking */

        /** Get transaction timestampe using 'stub' */

        /** Timestamp formatting 'YYYY-MM-DD HH:MM:SS' */

        /** Consist asset information structure */

        /** Put the asset information */

    }



    /**
     * Get digital asset information.
     * @param {Object} stub 
     * @param {string[]} args args[0]: assetID
     * @return {string} The asset information of assetID
     */
    async query(stub, args) {

        /** !!! Generate composite key !!! */

        /** Get state */

        /** Return asset state */
    }

};

/** Start chaincode */
shim.start(new Chaincode());