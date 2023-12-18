package edu.wpi.first.deployutils.deploy.sessions;

import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.common.channel.ChannelRequestHandler;
import org.apache.sshd.common.util.buffer.Buffer;
import org.apache.sshd.common.util.logging.AbstractLoggingBean;

public class ClientKeepAliveHandler extends AbstractLoggingBean implements ChannelRequestHandler  {
    public static final ClientKeepAliveHandler INSTANCE = new ClientKeepAliveHandler();

    @Override
    public Result process(Channel t, String request, boolean wantReply, Buffer buffer) throws Exception {
        // some clients use different strings - e.g., keep-alive@bitvise.com, keepalive@putty.projects.tartarus.org
        if ((!request.startsWith("keepalive@")) && (!request.startsWith("keep-alive@"))) {
            if (log.isDebugEnabled()) {
                log.debug("ClientKeepAliveHandler({}) request={}, want-reply={}", t, request, wantReply);
            }
            return Result.Unsupported;
        }

        return Result.ReplySuccess;
    }

}
