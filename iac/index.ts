import * as pulumi from "@pulumi/pulumi";
import * as argocd from "./apps/argocd";
import { ArgocdConfig } from "./apps/argocd";
import * as certManager from "./apps/cert-manager";
import { CertManagerConfig } from "./apps/cert-manager";
import { createNodes } from "./kubernetes/node";

export interface NodeConfig {
    name: string;
    config: {
        type: string;
        location: string;
        image: string;
        publicNet: {
            ipv4: boolean;
            ipv6: boolean;
            dnsPtr: boolean;
        }
        controlPlane: boolean;
    }
}

interface AppsConfig {
    certManager: CertManagerConfig;
    argocd: ArgocdConfig;
}

const config = new pulumi.Config("queuemusic");

const privateSshKey = config.requireSecret("sshkeyPrivate")

const nodesConfig = config.getObject<NodeConfig[]>("nodes");

const {provider: k8sProvider} = createNodes(nodesConfig ?? [], privateSshKey);

const appsConfig = config.requireObject<AppsConfig>("apps")

const { certmanager: cm } = certManager.install({
    provider: k8sProvider,
    config: appsConfig.certManager,
    dependsOn: [k8sProvider],
})

argocd.install({
    config: appsConfig.argocd,
    provider: k8sProvider,
    dependsOn: [cm],
})

