import { deepMerge } from "../../deepmerge";
import * as k8s from "@pulumi/kubernetes";
import { Chart } from "@pulumi/kubernetes/helm/v4";
import { Input, Resource } from "@pulumi/pulumi";

export const install = ({
    values,
    namespace,
    version,
    provider,
    dependsOn,
}: {
    namespace: k8s.core.v1.Namespace,
    provider: k8s.Provider,
    values: any,
    version: string
    dependsOn: Input<Input<Resource>[]>,
}): Chart => {
    const baseValues = {
        config: {
            apiVersion: "controller.config.cert-manager.io/v1alpha1",
            kind: "ControllerConfiguration",
            enableGatewayAPI: true
        },
        crds: {
            enabled: true,
        }
    }

    const merged = deepMerge(baseValues, values)

    return new k8s.helm.v4.Chart("cert-manager", {
        namespace: namespace.id,
        version: version,
        name: "cert-manager",
        chart: "cert-manager",
        repositoryOpts: {
            repo: "https://charts.jetstack.io",
        },
        values: merged,
    }, { provider, dependsOn })
}