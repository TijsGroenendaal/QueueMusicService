import * as k8s from "@pulumi/kubernetes"
import * as components from '../components';
import * as certmanager from './cert-manager';
import { Input, Resource } from "@pulumi/pulumi";

export interface CertManagerConfig {
    version: string
    email: string
    values: any
}

export const install = ({
    provider,
    config,
    dependsOn,
}: {
    provider: k8s.Provider,
    config: CertManagerConfig,
    dependsOn: Input<Input<Resource>[]>,
}) => {

    const ns = components.namespace.install({
        name: "cert-manager",
        provider,
        dependsOn
    });

    const gw = components.gateway.install({
        namespace: ns,
        provider,
        name: "cert-manager-gateway",
        listeners: [
            {
                name: "http",
                protocol: "HTTP",
                port: 80,
                allowedRoutes: {
                    namespaces: {
                        from: "Same"
                    }
                }
            }
        ],
        dependsOn: [ns]
    })

    const cm = certmanager.install({
        namespace: ns,
        provider,
        values: config.values,
        version: config.version,
        dependsOn: [gw]
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
                    email: config.email,
                    privateKeySecretRef: {
                        name: "letsencrypt-production",
                    },
                    solvers: [
                        {
                            http01: {
                                gatewayHTTPRoute: {
                                    parentRefs: [
                                        {
                                            name: gw.id,
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