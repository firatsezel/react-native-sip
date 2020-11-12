console.log("Start creating iOS container");


const getRandomInt = (min, max) => {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min;
};


const IMAGE_NAME="react-native-pjsip-builder/ios"
const CONTAINER_NAME=`react-native-pjsip-builder-${getRandomInt(1, 100)}`;

const fs = require("fs");

if (fs.existsSync(__dirname + "/dist/ios")) {
    fs.rmdirSync(__dirname + "/dist/ios", { recursive: true });
}


const { spawn } = require('child_process');

async function main() {

    console.log("Docker building...");
    let container = spawn('docker build -t react-native-pjsip-builder/ios ./ios/', { shell: true, cwd: __dirname });

    for await (const data of container.stdout) {
        console.log(`${data}`);
    }

    console.log("Docker running...");
    container = spawn(`docker run --name ${CONTAINER_NAME} ${IMAGE_NAME} bin/true`, { shell: true, cwd: __dirname });

    for await (const data of container.stdout) {
        console.log(`${data}`);
    }

    console.log("Docker copying...");
    container = spawn(`docker cp ${CONTAINER_NAME}:/dist/ios ./dist/ios`, { shell: true, cwd: __dirname });

    for await (const data of container.stdout) {
        console.log(`${data}`);
    }

    console.log("Docker removing...");
    container = spawn(`docker rm ${CONTAINER_NAME}`, { shell: true, cwd: __dirname });

    for await (const data of container.stdout) {
        console.log(`${data}`);
    }

}

main();