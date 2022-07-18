// socket已经连接成功
let socketOpen = false;
// socket已经调用关闭function
let socketClose = false;
// socket发送的消息队列
let socketMsgQueue = [];
// 判断心跳变量
let heart = '';
// 心跳失败次数
let heartBeatFailCount = 0;
// 终止心跳
let heartBeatTimeOut = null;
// 终止重新连接
let connectSocketTimeOut = null;
let i = 1;
let ws;
// socket链接地址，需要在微信后台配置
const url = 'wss://zydrs.beisun.com/vcs/api/websocket/';
const webSocket = {
  connectSocket(userId) {
    socketOpen = false;
    socketClose = false;
    socketMsgQueue = [];
    if (userId === null && userId === '') {
      return;
    }
    ws = new WebSocket(url + userId);
    ws.onmessage = this.wsonmessage;
    ws.onopen = this.wsonopen;
    ws.onerror = this.wsonerror;
    ws.onclose = this.wsonclose;
    // ws.connectSocket({
    //   url: url + userId,
    //   // header: { 'ticket': wx.getStorageSync('token')},
    //   success(res) {
    //     if (res) {
    //       // 成功回调
    //       console.log(res);
    //       // options.success && options.success(res);
    //     }
    //   },
    //   fail(res) {
    //     if (res) {
    //       // 失败回调
    //       // options.fail && options.fail(res);
    //     }
    //   },
    // });
  },

  /**
   * 通过 WebSocket 连接发送数据
   * @param {options}
   *   data    String / ArrayBuffer    是    需要发送的内容
   *   success    Function    否    接口调用成功的回调函数
   *   fail    Function    否    接口调用失败的回调函数
   *   complete    Function    否    接口调用结束的回调函数（调用成功、失败都会执行）
   */
  sendSocketMessage(options) {
    if (socketOpen) {
      ws.send(options.msg);
    } else {
      socketMsgQueue.push(options.msg);
    }
  },

  /**
   * 关闭 WebSocket 连接。
   * @param {options}
   *   code    Number    否    一个数字值表示关闭连接的状态号，表示连接被关闭的原因。如果这个参数没有被指定，默认的取值是1000 （表示正常连接关闭）
   *   reason    String    否    一个可读的字符串，表示连接被关闭的原因。这个字符串必须是不长于123字节的UTF-8 文本（不是字符）
   *   fail    Function    否    接口调用失败的回调函数
   *   complete    Function    否    接口调用结束的回调函数（调用成功、失败都会执行）
   */
  closeSocket(options) {
    socketClose = true;
    if (connectSocketTimeOut) {
      clearTimeout(connectSocketTimeOut);
      clearTimeout(heartBeatTimeOut);
      heartBeatTimeOut = null;
      connectSocketTimeOut = null;
    }

    const self = this;
    self.stopHeartBeat();
    ws.closeSocket({
      success(res) {
        console.log('WebSocket 已关闭！');
        if (options) {
          options.success && options.success(res);
        }
      },
      fail(res) {
        console.log('WebSocket 关闭失败！');
        if (options) {
          options.fail && options.fail(res);
        }
      },
    });
  },

  // 收到消息回调
  onSocketMessageCallback(_msg) {

  },

  // 开始心跳
  startHeartBeat() {
    console.log('socket开始心跳');
    const self = this;
    heart = 'heart';
    self.heartBeat();
  },

  // 结束心跳
  stopHeartBeat() {
    console.log('socket结束心跳');
    // const self = this;
    heart = '';
    if (heartBeatTimeOut) {
      clearTimeout(heartBeatTimeOut);
      heartBeatTimeOut = null;
    }
    if (connectSocketTimeOut) {
      clearTimeout(connectSocketTimeOut);
      connectSocketTimeOut = null;
    }
  },

  // 心跳
  heartBeat() {
    const self = this;
    console.log(`socketClose=${socketClose}`);
    if (!heart || socketClose === true) {
      return;
    }
    self.sendSocketMessage({
      msg: JSON.stringify({
        contentType: 'heart', // 心跳
        toUserId: '0',
        content: '0',
      }),
      success() {
        console.log('socket心跳成功');
        if (heart) {
          heartBeatTimeOut = setTimeout(() => {
            self.heartBeat();
          }, 30000);
        }
      },
      fail() {
        console.log('socket心跳失败');
        if (heartBeatFailCount > 2) {
          // 重连
          this.connectSocket();
        }
        if (heart) {
          heartBeatTimeOut = setTimeout(function () {
            this.heartBeat();
          }, 30000);
        }
        // eslint-disable-next-line no-plusplus
        heartBeatFailCount++;
      },
    });
  },
  // 监听WebSocket连接打开事件。callback 回调函数
  wsonopen() {
    console.log('WebSocket连接已打开！');

    // 如果已经调用过关闭function
    if (socketClose) {
      console.log('WebSocket开始关闭！');
      webSocket.closeSocket();
    } else {
      socketOpen = true;
      for (let i = 0; i < socketMsgQueue.length; i++) {
        webSocket.sendSocketMessage(socketMsgQueue[i]);
      }
      socketMsgQueue = [];
      webSocket.startHeartBeat();
    }
  },

  // 监听WebSocket错误。
  wsonerror(res) {
    console.log('WebSocket连接打开失败，请检查！', res);
  },

  // 监听WebSocket接受到服务器的消息事件。
  wsonmessage(res) {
    console.log(`收到服务器内容：${res.data}`);
    if (res.data === 'SUCCESS') {
      return;
    }
    webSocket.onSocketMessageCallback(res.data);
  },

  // 监听WebSocket关闭。
  wsonclose() {
    console.log('WebSocket 已关闭!');
    // if (!socketClose) {
    // eslint-disable-next-line no-restricted-properties
    const time = Math.pow(2, i);
    // eslint-disable-next-line no-plusplus
    i++;
    if (time === 8) {
      i = 1;
    }
    clearTimeout(connectSocketTimeOut);
    // connectSocketTimeOut = setTimeout(() => {
    //   webSocket.connectSocket();
    // }, time*1000);
    // }
  },
};

export default webSocket;
