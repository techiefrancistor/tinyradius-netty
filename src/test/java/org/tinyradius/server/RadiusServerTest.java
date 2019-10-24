package org.tinyradius.server;

import io.netty.channel.ChannelFactory;
import io.netty.channel.ReflectiveChannelFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tinyradius.packet.RadiusPacket;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RadiusServerTest {

    private final ChannelFactory<NioDatagramChannel> channelFactory = new ReflectiveChannelFactory<>(NioDatagramChannel.class);
    private final NioEventLoopGroup eventExecutors = new NioEventLoopGroup(2);

    @Mock
    private HandlerAdapter<RadiusPacket, SecretProvider> authHandler;

    @Mock
    private HandlerAdapter<RadiusPacket, SecretProvider> acctHandler;

    @Test
    void serverStartStop() throws InterruptedException {
        final RadiusServer server = new RadiusServer(
                eventExecutors, channelFactory, authHandler, acctHandler, new InetSocketAddress(1024), new InetSocketAddress(1025));

        // not registered with eventLoop
        assertFalse(server.getAcctChannel().isRegistered());
        assertFalse(server.getAuthChannel().isRegistered());

        // not bound to socket
        assertNull(server.getAcctChannel().localAddress());
        assertNull(server.getAuthChannel().localAddress());

        // no handlers registered
        String TAIL_CONTEXT = "DefaultChannelPipeline$TailContext#0";
        assertEquals(Collections.singletonList(TAIL_CONTEXT), server.getAcctChannel().pipeline().names());
        assertEquals(Collections.singletonList(TAIL_CONTEXT), server.getAuthChannel().pipeline().names());

        server.start().syncUninterruptibly();

        // registered with eventLoop
        assertTrue(server.getAcctChannel().isRegistered());
        assertTrue(server.getAuthChannel().isRegistered());

        // bound to socket
        assertNotNull(server.getAcctChannel().localAddress());
        assertNotNull(server.getAuthChannel().localAddress());

        // handlers registered
        final List<String> acctPipeline = server.getAcctChannel().pipeline().names();
        assertEquals(TAIL_CONTEXT, acctPipeline.get(1));
        assertTrue(acctPipeline.get(0).contains("HandlerAdapter$MockitoMock$"));

        final List<String> authPipeline = server.getAuthChannel().pipeline().names();
        assertEquals(TAIL_CONTEXT, authPipeline.get(1));
        assertTrue(authPipeline.get(0).contains("HandlerAdapter$MockitoMock$"));

        server.stop().syncUninterruptibly();
        Thread.sleep(500);

        // not registered with eventLoop
        assertFalse(server.getAcctChannel().isRegistered());
        assertFalse(server.getAuthChannel().isRegistered());

        // no handlers registered
        assertEquals(Collections.singletonList(TAIL_CONTEXT), server.getAcctChannel().pipeline().names());
        assertEquals(Collections.singletonList(TAIL_CONTEXT), server.getAuthChannel().pipeline().names());
    }
}