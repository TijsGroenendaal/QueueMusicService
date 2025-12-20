import * as hcloud from "@pulumi/hcloud";
import * as pulumi from "@pulumi/pulumi";
import * as command from "@pulumi/command";
import { NodeConfig } from "../index";
import { setupControlPlane } from "./control-plane";
import { setupWorkerNode } from "./worker";
import { Output } from "@pulumi/pulumi";
import * as tls from "@pulumi/tls";
import * as k8s from "@pulumi/kubernetes";

const SCRIPTS_PATH = './kubernetes/lib';
export const NODE_SCRIPTS_PATH = `/tmp/scripts`;

const setupServer = (
    nodeConfig: NodeConfig,
    publicKey: hcloud.SshKey,
    privateKey: Output<string>,
    options: Partial<pulumi.CustomResourceOptions> = {}
) => {
    const node = new hcloud.Server(`server-${nodeConfig.name}`, {
        serverType: nodeConfig.config.type,
        location: nodeConfig.config.location,
        image: nodeConfig.config.image,
        publicNets: [{
            ipv4Enabled: nodeConfig.config.publicNet.ipv4,
            ipv6Enabled: nodeConfig.config.publicNet.ipv6,
        }],
        labels: {
            environment: pulumi.getStack(),
            managedBy: "pulumi",
        },
        sshKeys: [
            publicKey.id
        ]
    }, options);

    const copyToRemote = new command.remote.CopyToRemote(`copy-scripts-${nodeConfig.name}`, {
        connection: {
            host: node.ipv4Address,
            user: 'root',
            privateKey,
        },
        remotePath: NODE_SCRIPTS_PATH,
        source: new pulumi.asset.FileArchive(SCRIPTS_PATH),
    }, {dependsOn: [node]});

    return {node, copyToRemote}
}


export function createNodes(nodesConfig: NodeConfig[], privateKey: Output<string>): {
    provider: k8s.Provider,
}  {
    if (nodesConfig.filter(config => config.config.controlPlane).length > 1) {
        throw new Error("Multiple node are marked as controlPlane in the configuration.");
    }

    const publicKey = new hcloud.SshKey(`primary-sshkey`, {
        name: `primary-sshkey`,
        publicKey: privateKey.apply(value => tls.getPublicKey({
            privateKeyOpenssh: value
        })).publicKeyOpenssh
    });

    const controlPlane = nodesConfig.find(config => config.config.controlPlane);
    if (controlPlane === undefined) {
        throw new Error("No controlPlane node found in the configuration.");
    }
    const workers = nodesConfig.filter(config => !config.config.controlPlane);

    const {node: controlPlaneServer, copyToRemote} = setupServer(
        controlPlane,
        publicKey,
        privateKey,
    );
    const controlPlaneOutput = setupControlPlane(
        controlPlane.name,
        controlPlaneServer.ipv4Address,
        privateKey,
        {dependsOn: [copyToRemote]}
    );

    workers.forEach((workerConfig: NodeConfig) => {
        const {node: workerServer, copyToRemote} = setupServer(workerConfig, publicKey, privateKey);

        setupWorkerNode(
            workerConfig.name,
            workerServer.ipv4Address,
            controlPlaneServer.ipv4Address,
            privateKey,
            controlPlaneOutput.token.stdout,
            {dependsOn: [copyToRemote]}
        );
    });

    const provider = new k8s.Provider(`${pulumi.getStack()}-k8s`, {
        kubeconfig: controlPlaneOutput.config.stdout,
    })

    return {
        provider
    };
}