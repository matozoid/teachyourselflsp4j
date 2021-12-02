import { ExtensionContext } from 'vscode';
import { Executable, LanguageClient, LanguageClientOptions, StreamInfo } from 'vscode-languageclient';

export function activate(context: ExtensionContext) {

    const serverOptions: Executable = {
        command: "C:/PROGRA~1/Zulu/zulu-10/bin/java",
        args: ["-jar", "F:/work/TeachYourselfLsp4j/server/build/libs/TeachYourselfLsp4j-1.0-SNAPSHOT.jar"]
    }

    //function serverOptions(): Thenable<StreamInfo> {
    //    const client = require("net").connect(8080)
    //    return Promise.resolve({
    //        writer: client,
    //        reader: client
    //    })
    //}

    const clientOptions: LanguageClientOptions = {
        documentSelector: [{ scheme: 'file', language: 'csv' }]
    }

    const disposable = new LanguageClient('myLanguageServer', 'MyLanguageServer', serverOptions, clientOptions).start();
    context.subscriptions.push(disposable);
}
