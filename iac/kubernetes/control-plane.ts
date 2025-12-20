import * as command from "@pulumi/command";
import { NODE_SCRIPTS_PATH } from "./node";
import * as pulumi from "@pulumi/pulumi";
import { CustomResourceOptions, Output } from "@pulumi/pulumi";
import { Command } from "@pulumi/command/remote";
import * as k8s from "@pulumi/kubernetes";

const CONTROL_PLANE_SCRIPT = 'control-plane.sh';

interface ControlPlaneOutput {
    token: Command;
    config: Command;
}

export const setupControlPlane = (
    name: string,
    ipv4Address: pulumi.Output<string>,
    privateKey: Output<string>,
    options: Partial<CustomResourceOptions> = { }
): ControlPlaneOutput => {
    const output = new command.remote.Command(`command-init-${name}`, {
        connection: {
            host: ipv4Address,
            user: 'root',
            privateKey,
        },
        create: `bash ${NODE_SCRIPTS_PATH}/lib/${CONTROL_PLANE_SCRIPT}`,
    }, options);

    const token = new command.remote.Command(`get-token-${name}`, {
        connection: {
            host: ipv4Address,
            user: 'root',
            privateKey,
        },
        create: `kubeadm token create`,
    }, { dependsOn: [output] });

    const kubeconfig = new command.remote.Command(`get-kubeconfig-${name}`, {
        connection: {
            host: ipv4Address,
            user: 'root',
            privateKey
        },
        create: `cat /etc/kubernetes/admin.conf`,
    }, { dependsOn: [token] });

    return {
        config: kubeconfig,
        token: token
    }
}