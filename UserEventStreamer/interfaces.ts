export type UserEventTaskType = 'ADD' | 'REMOVE' | 'ACCEPT' | 'VOTE'
export type UserEventTaskVoterType = 'PLUS' | 'MINUS'

export interface UserEventTask {
    sessionId: string,
    type: UserEventTaskType,
    song: {
        id: string,
        title: string,
        authors: string,
        album: string
    },
    user: string,
    voters: [
        user: string,
        type: UserEventTaskVoterType
    ]
}