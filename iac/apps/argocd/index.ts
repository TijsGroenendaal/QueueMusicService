import * as argocd from './argocd';
import * as k8s from "@pulumi/kubernetes";
import * as components from '../components';
import { Input, Resource } from "@pulumi/pulumi";

export interface ArgocdConfig {
    version: string
    values: any
}

export const install = ({
    config,
    provider,
    dependsOn,
}: {
    config: ArgocdConfig
    provider: k8s.Provider
    dependsOn: Input<Input<Resource>[]>
}) => {
    const ns = components.namespace.install({
        name: "argocd",
        provider,
        dependsOn,
    });

    argocd.install({
        values: config.values,
        version: config.version,
        provider,
        namespace: ns,
        dependsOn: [ns],
    })
}