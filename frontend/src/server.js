'use strict';

const app = required('rc-tools/lib/server/')();
const parse = require('co-busboy');
const fs = require('fs');

app.post('./images.do', function*(){
    try{
        const parts= parse(this,{
            autoFields: true
        });
        let part, files = [];
        while (part = yield parts){
            files.push(part.filename);
            part.resume();
        }
        let ret = '';
        this.status = 200;
        this.set('Content-Type', 'text/html');
        yield wait(2000);
        if(parts.fields[0] && parts.fields[0][0] === '_documentDomain'){
            ret += '<script>document.domain=' + parts.fields[0][1] + '";</script>';
        }
        ret += JSON.stringify(files);
        this.body = ret;
    }catch(e){
        this.body = e.stack;
    }
});

const port = process.env.npm_package_config_port;
app.listen(port);
process.on('uncaughtException', err => {
    console.log(err.stack);
})