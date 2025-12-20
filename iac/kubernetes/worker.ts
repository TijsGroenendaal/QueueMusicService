import * as command from "@pulumi/command";
import * as pulumi from "@pulumi/pulumi";
import { NODE_SCRIPTS_PATH } from "./node";
import { Command } from "@pulumi/command/remote";
import { Output } from "@pulumi/pulumi";

const WORKER_SCRIPT = 'worker.sh';

export const setupWorkerNode = (
    name: string,
    ipv4Address: pulumi.Output<string>,
    controlPlaneIpv4: pulumi.Output<string>,
    privateKey: Output<string>,
    joinToken: pulumi.Output<string>,
    options: Partial<pulumi.CustomResourceOptions> = { }
) => {
    const output: Command = new command.remote.Command(`command-init-${name}`, {
        connection: {
            host: ipv4Address,
            user: 'root',
            privateKey,
        },
        create: `bash ${NODE_SCRIPTS_PATH}/lib/${WORKER_SCRIPT}`,
    }, options);

    new command.remote.Command(`join-command-${name}`, {
        connection: {
            host: ipv4Address,
            user: 'root',
            privateKey,
        },
        create: pulumi.interpolate`kubeadm join ${controlPlaneIpv4}:6443 --token ${joinToken} --discovery-token-unsafe-skip-ca-verification`,
    }, { dependsOn: [output] });
}