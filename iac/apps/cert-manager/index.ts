import * as k8s from "@pulumi/kubernetes"
import * as components from '../components';
import * as certmanager from './cert-manager';
import { Input, Output, Resource } from "@pulumi/pulumi";

export const install = ({
    provider,
    version,
    values,
    email,
    dependsOn,
}: {
    provider: k8s.Provider,
    version: string
    values: any,
    email: Output<string>,
    dependsOn: Input<Input<Resource>[]>,
}) => {

    const ns = components.namespace.install({
        name: "cert-manager",
        provider,
        dependsOn
    });

    const cm = certmanager.install({
        namespace: ns,
        provider,
        values: values,
        version: version,
        dependsOn: [ns]
    });

    new k8s.apiextensions.CustomResource(
        "production-issuer",
        {
            apiVersion: "cert-manager.io/v1",
            kind: "ClusterIssuer",
            metadata: {
                name: "letsencrypt-production",
            },
            spec: {
                acme: {
                    server: "https://acme-v02.api.letsencrypt.org/directory",
                    email: email,
                    privateKeySecretRef: {
                        name: "letsencrypt-production",
                    },
                    solvers: [
                        {
                            http01: {
                                gatewayHTTPRoute: {
                                    parentRefs: [
                                        {
                                            name: "traefik",
                                        }
                                    ]
                                }
                            }
                        },
                    ],
                },
            },
        },
        {
            provider,
            dependsOn: [cm],
        },
    );

    return {
        certmanager: cm
    }
}