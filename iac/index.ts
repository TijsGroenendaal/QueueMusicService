import * as pulumi from "@pulumi/pulumi";
import * as argocd from "./apps/argocd";
import * as certManager from "./apps/cert-manager";
import { traefik } from "./apps/networking";
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

const config = new pulumi.Config("queuemusic");

const privateSshKey = config.requireSecret("sshkeyPrivate")

const nodesConfig = config.getObject<NodeConfig[]>("nodes");

const { provider: k8sProvider } = createNodes(nodesConfig ?? [], privateSshKey);

const gc = traefik.install({
    provider: k8sProvider,
    version: config.require("traefik-version"),
    values: config.getObject("traefik-values") ?? {},
    dependsOn: [k8sProvider]
})

const certManagerVersion = config.require("certmanager-version")
const certManagerValues = config.getObject("certmanager-values") ?? {}
const certManagerEmail = config.requireSecret("certmanager-email")

const { certmanager: cm } = certManager.install({
    provider: k8sProvider,
    version: certManagerVersion,
    email: certManagerEmail,
    values: certManagerValues,
    dependsOn: [gc],
})

const argocdVersion = config.require("argocd-version")
const argocdValues = config.getObject("argocd-values") ?? {}

argocd.install({
    version: argocdVersion,
    values: argocdValues,
    provider: k8sProvider,
    dependsOn: [cm],
})

