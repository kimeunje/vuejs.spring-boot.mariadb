import { describe, it, expect, beforeEach, afterEach } from 'vitest'
import axios from 'axios'
import MockAdapter from 'axios-mock-adapter'
import authenticationService from '@/services/authentication'

describe('services/registration', () => {
  let mock: MockAdapter

  const loginDetail = {
    username: 'sunny',
    password: 'MyPassword123!'
  }

  beforeEach(() => {
    mock = new MockAdapter(axios)
  })

  afterEach(() => {
    mock.restore()
  })

  it('`/authentications` API를 호출해야 한다.', () => {
    mock.onPost('/authentications').reply(200, { result: 'success' })
    return authenticationService.authenticate(loginDetail)
  })

  it('요청이 성공하면 호출한 곳에 응답해야 한다.', async () => {
    expect.assertions(1)

    mock.onPost('/authentications').reply(200, { result: 'success' })

    try {
      const response: any = await authenticationService.authenticate(loginDetail)
      expect(response.result).toEqual('success')
    } catch (error) {
      throw error
    }
  })

  it('요청이 실패하면 호출한 곳에 에러를 전파해야 한다.', async () => {
    expect.assertions(1)

    mock.onPost('/authentications').reply(400, { message: 'Bad request' })

    try {
      const response: any = await authenticationService.authenticate(loginDetail)
    } catch (error: any) {
      expect(error.response.data.message).toEqual('Bad request')
    }
  })
})