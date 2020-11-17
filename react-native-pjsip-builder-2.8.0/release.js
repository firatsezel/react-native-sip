console.log(`Release is ${__dirname}`);
console.log("Remove dist folder");

const fs = require("fs");

if (fs.existsSync(__dirname + "/dist")) {
    fs.rmdirSync(__dirname + "/dist", { recursive: true });
}


fs.mkdirSync(__dirname + '/dist');

const { spawn } = require('child_process');


async function main() {

    /*console.log("Execute build_android.js");
    let script = spawn(`node ${__dirname}/build_android.js`, { shell: true });

    for await (const data of script.stdout) {
        console.log(`${data}`);
    }*/

    console.log("Execute build_ios.js");
    script = spawn(`node ${__dirname}/build_ios.js`, { shell: true });

    for await (const data of script.stdout) {
        console.log(`${data}`);
    }

}

main();