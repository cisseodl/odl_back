----------------------------------------
/var/log/web.stdout.log
----------------------------------------
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.aop.framework.CglibAopProxy.access$000(CglibAopProxy.java:85)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:704)
Jan 26 14:28:06 ip-172-31-34-63 web: at com.odc.aws_learning.app.controller.CoursesController$$EnhancerBySpringCGLIB$$974d52d2.getAllCourses(<generated>)
Jan 26 14:28:06 ip-172-31-34-63 web: at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
Jan 26 14:28:06 ip-172-31-34-63 web: at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
Jan 26 14:28:06 ip-172-31-34-63 web: at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
Jan 26 14:28:06 ip-172-31-34-63 web: at java.base/java.lang.reflect.Method.invoke(Method.java:566)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1072)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)
Jan 26 14:28:06 ip-172-31-34-63 web: at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
Jan 26 14:28:06 ip-172-31-34-63 web: at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:209)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:111)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:337)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.access.intercept.AuthorizationFilter.doFilter(AuthorizationFilter.java:96)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:122)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:116)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:126)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:81)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:109)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:149)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at com.odc.aws_learning.auth.config.JwtAuthenticationFilter.doFilterInternal(JwtAuthenticationFilter.java:79)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:103)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:89)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:112)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:82)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:55)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:221)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:186)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:354)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:267)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:481)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:130)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:926)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1791)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
Jan 26 14:28:06 ip-172-31-34-63 web: at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
Jan 26 14:28:06 ip-172-31-34-63 web: at java.base/java.lang.Thread.run(Thread.java:829)
Jan 26 14:28:06 ip-172-31-34-63 web: âš ï¸ [CourseMapper] Aucune catÃ©gorie trouvÃ©e, retour 'Non catÃ©gorisÃ©'


----------------------------------------
/var/log/eb-engine.log
----------------------------------------
2026/01/26 14:25:47.300389 [INFO] stop X-Ray ...
2026/01/26 14:25:47.300399 [INFO] Running command: systemctl show -p PartOf xray.service
2026/01/26 14:25:47.307970 [WARN] stopProcess Warning: process xray is not registered
2026/01/26 14:25:47.307986 [INFO] Running command: systemctl stop xray.service
2026/01/26 14:25:47.317286 [INFO] Executing instruction: stop proxy
2026/01/26 14:25:47.317302 [INFO] Running command: systemctl show -p PartOf httpd.service
2026/01/26 14:25:47.321919 [WARN] deregisterProcess Warning: process httpd is not registered, skipping...

2026/01/26 14:25:47.321935 [INFO] Running command: systemctl show -p PartOf nginx.service
2026/01/26 14:25:47.326888 [INFO] Running command: systemctl is-active nginx.service
2026/01/26 14:25:47.330553 [INFO] Running command: systemctl show -p PartOf nginx.service
2026/01/26 14:25:47.334920 [INFO] Running command: systemctl stop nginx.service
2026/01/26 14:25:47.354707 [INFO] Running command: systemctl disable nginx.service
2026/01/26 14:25:47.451007 [INFO] Running command: systemctl daemon-reload
2026/01/26 14:25:47.543209 [INFO] Running command: systemctl reset-failed
2026/01/26 14:25:47.547808 [INFO] Executing instruction: FlipApplication
2026/01/26 14:25:47.547820 [INFO] Fetching environment variables...
2026/01/26 14:25:47.548071 [INFO] Purge old process...
2026/01/26 14:25:47.548087 [INFO] Running command: systemctl stop eb-app.target
2026/01/26 14:25:47.554372 [INFO] Running command: systemctl show -p ConsistsOf eb-app.target | cut -d= -f2
2026/01/26 14:25:47.567346 [INFO] web.service

2026/01/26 14:25:47.567360 [INFO] deregistering process: web
2026/01/26 14:25:47.567369 [INFO] Running command: systemctl show -p PartOf web.service
2026/01/26 14:25:47.583191 [INFO] Running command: systemctl is-active web.service
2026/01/26 14:25:47.597209 [INFO] Running command: systemctl disable web.service
2026/01/26 14:25:47.756994 [INFO] Removed symlink /etc/systemd/system/multi-user.target.wants/web.service.

2026/01/26 14:25:47.757104 [INFO] Running command: systemctl daemon-reload
2026/01/26 14:25:47.859778 [INFO] Running command: systemctl reset-failed
2026/01/26 14:25:47.864494 [INFO] Running command: systemctl is-active web.service
2026/01/26 14:25:47.868144 [INFO] Process web has been fully terminated
2026/01/26 14:25:47.868156 [INFO] All processes have been fully terminated
2026/01/26 14:25:47.868161 [INFO] Removing /var/app/current/ if it exists
2026/01/26 14:25:47.872587 [INFO] Renaming /var/app/staging/ to /var/app/current/
2026/01/26 14:25:47.872609 [INFO] Register application processes...
2026/01/26 14:25:47.872615 [INFO] Registering the proc: web

2026/01/26 14:25:47.872622 [INFO] Running command: systemctl show -p PartOf web.service
2026/01/26 14:25:47.879712 [INFO] Running command: systemctl daemon-reload
2026/01/26 14:25:47.977452 [INFO] Running command: systemctl reset-failed
2026/01/26 14:25:47.982080 [INFO] Running command: systemctl is-enabled eb-app.target
2026/01/26 14:25:47.985896 [INFO] Running command: systemctl enable eb-app.target
2026/01/26 14:25:48.074805 [INFO] Running command: systemctl start eb-app.target
2026/01/26 14:25:48.080301 [INFO] Running command: systemctl enable web.service
2026/01/26 14:25:48.173507 [INFO] Created symlink from /etc/systemd/system/multi-user.target.wants/web.service to /etc/systemd/system/web.service.

2026/01/26 14:25:48.173538 [INFO] Running command: systemctl show -p PartOf web.service
2026/01/26 14:25:48.179059 [INFO] Running command: systemctl is-active web.service
2026/01/26 14:25:48.182749 [INFO] Running command: systemctl start web.service
2026/01/26 14:25:48.201668 [INFO] Executing instruction: start X-Ray
2026/01/26 14:25:48.201683 [INFO] X-Ray is not enabled.
2026/01/26 14:25:48.201690 [INFO] Executing instruction: start proxy with new configuration
2026/01/26 14:25:48.201707 [INFO] Running command: /usr/sbin/nginx -t -c /var/proxy/staging/nginx/nginx.conf
2026/01/26 14:25:48.219371 [INFO] nginx: the configuration file /var/proxy/staging/nginx/nginx.conf syntax is ok
nginx: configuration file /var/proxy/staging/nginx/nginx.conf test is successful

2026/01/26 14:25:48.219570 [INFO] Running command: cp -rp /var/proxy/staging/nginx/* /etc/nginx
2026/01/26 14:25:48.227546 [INFO] Running command: systemctl show -p PartOf nginx.service
2026/01/26 14:25:48.238567 [INFO] Running command: systemctl daemon-reload
2026/01/26 14:25:48.387818 [INFO] Running command: systemctl reset-failed
2026/01/26 14:25:48.399202 [INFO] Running command: systemctl show -p PartOf nginx.service
2026/01/26 14:25:48.410543 [INFO] Running command: systemctl is-active nginx.service
2026/01/26 14:25:48.423236 [INFO] Running command: systemctl start nginx.service
2026/01/26 14:25:48.524824 [INFO] Executing instruction: configureSqsd
2026/01/26 14:25:48.524840 [INFO] This is a web server environment instance, skip configure sqsd daemon ...
2026/01/26 14:25:48.524846 [INFO] Executing instruction: startSqsd
2026/01/26 14:25:48.524849 [INFO] This is a web server environment instance, skip start sqsd daemon ...
2026/01/26 14:25:48.524854 [INFO] Executing instruction: Track pids in healthd
2026/01/26 14:25:48.524858 [INFO] This is an enhanced health env...
2026/01/26 14:25:48.524870 [INFO] Running command: systemctl show -p ConsistsOf aws-eb.target | cut -d= -f2
2026/01/26 14:25:48.543769 [INFO] cfn-hup.service nginx.service healthd.service

2026/01/26 14:25:48.543787 [INFO] Running command: systemctl show -p ConsistsOf eb-app.target | cut -d= -f2
2026/01/26 14:25:48.557958 [INFO] web.service

2026/01/26 14:25:48.558119 [INFO] Executing instruction: RunAppDeployPostDeployHooks
2026/01/26 14:25:48.558149 [INFO] Executing platform hooks in .platform/hooks/postdeploy/
2026/01/26 14:25:48.558165 [INFO] The dir .platform/hooks/postdeploy/ does not exist
2026/01/26 14:25:48.558170 [INFO] Finished running scripts in /var/app/current/.platform/hooks/postdeploy
2026/01/26 14:25:48.558176 [INFO] Executing cleanup logic
2026/01/26 14:25:48.558252 [INFO] CommandService Response: {"status":"SUCCESS","api_version":"1.0","results":[{"status":"SUCCESS","msg":"Engine execution has succeeded.","returncode":0,"events":[{"msg":"Instance deployment successfully detected a JAR file in your source bundle.","timestamp":1769437544854,"severity":"INFO"},{"msg":"Instance deployment successfully generated a 'Procfile'.","timestamp":1769437545004,"severity":"INFO"},{"msg":"Instance deployment completed successfully.","timestamp":1769437548558,"severity":"INFO"}]}]}

2026/01/26 14:25:48.559384 [INFO] Platform Engine finished execution on command: app-deploy

2026/01/26 14:33:16.770690 [INFO] Starting...
2026/01/26 14:33:16.770731 [INFO] Starting EBPlatform-PlatformEngine
2026/01/26 14:33:16.770753 [INFO] reading event message file
2026/01/26 14:33:16.771084 [INFO] Engine received EB command cfn-hup-exec

2026/01/26 14:33:16.842600 [INFO] Running command: /opt/aws/bin/cfn-get-metadata -s arn:aws:cloudformation:us-east-1:016299216814:stack/awseb-e-rafruf9ypt-stack/21700810-efd8-11f0-967a-0affd2545ccb -r AWSEBAutoScalingGroup --region us-east-1
2026/01/26 14:33:17.185460 [INFO] Running command: /opt/aws/bin/cfn-get-metadata -s arn:aws:cloudformation:us-east-1:016299216814:stack/awseb-e-rafruf9ypt-stack/21700810-efd8-11f0-967a-0affd2545ccb -r AWSEBBeanstalkMetadata --region us-east-1
2026/01/26 14:33:17.517670 [INFO] checking whether command tail-log is applicable to this instance...
2026/01/26 14:33:17.517687 [INFO] this command is applicable to the instance, thus instance should execute command
2026/01/26 14:33:17.517692 [INFO] Engine command: (tail-log)

2026/01/26 14:33:17.517757 [INFO] Executing instruction: GetTailLogs
2026/01/26 14:33:17.517763 [INFO] Tail Logs...
2026/01/26 14:33:17.518103 [INFO] Running command: tail -n 100 /var/log/web.stdout.log
2026/01/26 14:33:17.520099 [INFO] Running command: tail -n 100 /var/log/eb-engine.log


----------------------------------------
/var/log/eb-hooks.log
----------------------------------------


----------------------------------------
/var/log/nginx/access.log
----------------------------------------
172.31.33.212 - - [26/Jan/2026:14:23:54 +0000] "GET /awsodclearning/api/reviews/all HTTP/1.1" 200 475 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.33.212 - - [26/Jan/2026:14:24:03 +0000] "GET /awsodclearning/api/testimonials HTTP/1.1" 404 121 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:24:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:24:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:24:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:24:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:24:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:24:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:24:51 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:24:53 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:25:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:25:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:25:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:25:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:25:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:25:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:25:51 +0000] "GET / HTTP/1.1" 502 150 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:25:53 +0000] "GET / HTTP/1.1" 502 150 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:26:06 +0000] "GET / HTTP/1.1" 502 150 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:26:08 +0000] "GET / HTTP/1.1" 502 150 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:26:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:26:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:26:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:26:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:26:51 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:26:53 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:27:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:27:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:27:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:27:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:27:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:27:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:27:47 +0000] "OPTIONS /awsodclearning/api/odc-formations/read HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:47 +0000] "OPTIONS /awsodclearning/courses/read HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:47 +0000] "OPTIONS /awsodclearning/api/dashboard/public-stats HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:47 +0000] "GET /awsodclearning/api/testimonials HTTP/1.1" 403 121 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:47 +0000] "GET /awsodclearning/api/odc-formations/read HTTP/1.1" 200 1145 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:48 +0000] "GET /awsodclearning/api/dashboard/public-stats HTTP/1.1" 200 215 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:48 +0000] "GET /awsodclearning/courses/read HTTP/1.1" 200 1489 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:48 +0000] "OPTIONS /awsodclearning/api/profile/me HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:48 +0000] "OPTIONS /awsodclearning/cohorte/read HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:48 +0000] "OPTIONS /awsodclearning/api/profile/me/certificates HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:48 +0000] "GET /awsodclearning/api/profile/me/certificates HTTP/1.1" 403 132 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:48 +0000] "GET /awsodclearning/api/profile/me HTTP/1.1" 403 119 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:48 +0000] "GET /awsodclearning/cohorte/read HTTP/1.1" 200 73 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:51 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:27:53 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:27:54 +0000] "OPTIONS /awsodclearning/cohorte/read HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:27:54 +0000] "GET /awsodclearning/cohorte/read HTTP/1.1" 200 73 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:28:05 +0000] "OPTIONS /awsodclearning/auth/signin HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:28:05 +0000] "POST /awsodclearning/auth/signin HTTP/1.1" 200 1640 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:28:06 +0000] "GET /awsodclearning/api/odc-formations/read HTTP/1.1" 200 1145 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:28:06 +0000] "GET /awsodclearning/api/dashboard/public-stats HTTP/1.1" 200 215 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:28:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:28:06 +0000] "GET /awsodclearning/courses/read HTTP/1.1" 200 1489 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:28:06 +0000] "GET /awsodclearning/api/testimonials HTTP/1.1" 404 121 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.33.212 - - [26/Jan/2026:14:28:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:28:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:28:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:28:27 +0000] "POST /awsodclearning/api/testimonials HTTP/1.1" 404 121 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "154.118.156.78"
172.31.72.116 - - [26/Jan/2026:14:28:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:28:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:28:51 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:28:53 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:29:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:29:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:29:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:29:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:29:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:29:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:29:51 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:29:53 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:30:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:30:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:30:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:30:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:30:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:30:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:30:51 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:30:53 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:31:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:31:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:31:17 +0000] "GET /.env HTTP/1.1" 404 431 "-" "Mozilla/5.0 (Linux; Android 7.0; SM-G892A Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.107 Mobile Safari/537.36" "175.178.116.123"
172.31.33.212 - - [26/Jan/2026:14:31:17 +0000] "POST / HTTP/1.1" 404 431 "-" "Mozilla/5.0 (Linux; Android 7.0; SM-G892A Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.107 Mobile Safari/537.36" "175.178.116.123"
172.31.72.116 - - [26/Jan/2026:14:31:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:31:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:31:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:31:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:31:51 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:31:53 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:32:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:32:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:32:21 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:32:23 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:32:36 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:32:38 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:32:51 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:32:53 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [26/Jan/2026:14:33:06 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [26/Jan/2026:14:33:08 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"


----------------------------------------
/var/log/nginx/error.log
----------------------------------------
2026/01/21 20:21:06 [error] 509#509: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 20:21:12 [error] 509#509: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /owa/auth/logon.aspx HTTP/1.1", upstream: "http://127.0.0.1:5000/owa/auth/logon.aspx", host: "107.22.27.182"
2026/01/21 20:21:17 [error] 509#509: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 20:21:21 [error] 509#509: *9 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 00:24:29 [error] 7987#7987: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 00:24:33 [error] 7987#7987: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 00:24:44 [error] 7987#7987: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 00:24:48 [error] 7987#7987: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 08:26:32 [warn] 7987#7987: *8621 an upstream response is buffered to a temporary file /var/lib/nginx/tmp/proxy/1/00/0000000001 while reading upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/swagger-ui/swagger-ui-bundle.js HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/swagger-ui/swagger-ui-bundle.js", host: "odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com", referrer: "http://odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com/awsodclearning/swagger-ui/index.html"
2026/01/22 09:18:37 [warn] 7987#7987: *9695 an upstream response is buffered to a temporary file /var/lib/nginx/tmp/proxy/2/00/0000000002 while reading upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/swagger-ui/swagger-ui-bundle.js HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/swagger-ui/swagger-ui-bundle.js", host: "odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com", referrer: "http://odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com/awsodclearning/swagger-ui/index.html"
2026/01/22 09:36:13 [error] 23891#23891: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 09:36:16 [error] 23891#23891: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 09:36:28 [error] 23891#23891: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 09:36:31 [error] 23891#23891: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 12:52:37 [error] 4114#4114: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 12:52:38 [error] 4114#4114: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/api/evaluations/get-all HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/evaluations/get-all", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 12:52:40 [error] 4114#4114: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/cohorte/read HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/cohorte/read", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/22 12:52:40 [error] 4114#4114: *6 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/api/profile/me HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/profile/me", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/22 12:52:40 [error] 4114#4114: *9 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/api/profile/me/certificates HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/profile/me/certificates", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/22 12:52:41 [error] 4114#4114: *11 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 12:52:42 [error] 4114#4114: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/courses/read/by-instructor/5 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/courses/read/by-instructor/5", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 12:52:52 [error] 4114#4114: *14 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 12:52:53 [error] 4114#4114: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/api/evaluations/get-all HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/evaluations/get-all", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 12:52:54 [error] 4114#4114: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/api/notifications/stats HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/notifications/stats", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 12:52:55 [error] 4114#4114: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/courses/read/by-instructor/5 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/courses/read/by-instructor/5", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 12:52:56 [error] 4114#4114: *19 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 13:25:24 [error] 22935#22935: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 13:25:28 [error] 22935#22935: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 13:25:32 [error] 22935#22935: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "POST / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "api.smart-odc.com"
2026/01/22 13:25:33 [error] 22935#22935: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "POST / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "api.smart-odc.com"
2026/01/22 13:25:39 [error] 22935#22935: *8 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 15:37:01 [error] 31912#31912: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 15:37:04 [error] 31912#31912: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 15:37:16 [error] 31912#31912: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 15:37:19 [error] 31912#31912: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 16:40:49 [error] 3305#3305: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 16:40:53 [error] 3305#3305: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 16:40:53 [error] 3305#3305: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/api/categories/read HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/categories/read", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 16:40:57 [error] 3305#3305: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/api/dashboard/instructor HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/dashboard/instructor", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 16:41:04 [error] 3305#3305: *8 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 16:41:07 [error] 3305#3305: *10 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/api/notifications/stats HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/notifications/stats", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 16:41:08 [error] 3305#3305: *12 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 17:00:42 [error] 14752#14752: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:44 [error] 14752#14752: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:44 [error] 14752#14752: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:46 [error] 14752#14752: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:47 [error] 14752#14752: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:47 [error] 14752#14752: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:47 [error] 14752#14752: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:47 [error] 14752#14752: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:47 [error] 14752#14752: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/courses/2 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/courses/2", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:00:50 [error] 14752#14752: *12 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 17:00:54 [error] 14752#14752: *14 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 17:00:54 [error] 14752#14752: *16 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/api/notifications/stats HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/notifications/stats", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/22 17:01:05 [error] 14752#14752: *18 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 17:31:37 [error] 32547#32547: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 17:31:40 [error] 32547#32547: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 17:31:52 [error] 32547#32547: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/22 18:53:18 [warn] 32547#32547: *1690 an upstream response is buffered to a temporary file /var/lib/nginx/tmp/proxy/1/00/0000000001 while reading upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/swagger-ui/swagger-ui-bundle.js HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/swagger-ui/swagger-ui-bundle.js", host: "odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com", referrer: "http://odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com/awsodclearning/swagger-ui/index.html"
2026/01/23 12:37:19 [warn] 32547#32547: *20223 an upstream response is buffered to a temporary file /var/lib/nginx/tmp/proxy/2/00/0000000002 while reading upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/swagger-ui/swagger-ui-bundle.js HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/swagger-ui/swagger-ui-bundle.js", host: "odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com", referrer: "http://odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com/awsodclearning/swagger-ui/index.html"
2026/01/23 15:31:56 [warn] 32547#32547: *23285 an upstream response is buffered to a temporary file /var/lib/nginx/tmp/proxy/3/00/0000000003 while reading upstream, client: 172.31.72.116, server: , request: "GET /awsodclearning/swagger-ui/swagger-ui-bundle.js HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/swagger-ui/swagger-ui-bundle.js", host: "odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com", referrer: "http://odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com/awsodclearning/swagger-ui/index.html"
2026/01/23 16:40:21 [error] 28637#28637: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/23 16:40:32 [error] 28637#28637: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/23 16:40:36 [error] 28637#28637: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/23 17:22:35 [error] 20120#20120: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/23 17:22:38 [error] 20120#20120: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/23 17:22:50 [error] 20120#20120: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/23 19:26:59 [error] 25276#25276: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/23 19:27:11 [error] 25276#25276: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/23 19:27:14 [error] 25276#25276: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/24 13:53:37 [warn] 25276#25276: *19967 using uninitialized "year" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:37 [warn] 25276#25276: *19967 using uninitialized "month" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:37 [warn] 25276#25276: *19967 using uninitialized "day" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:37 [warn] 25276#25276: *19967 using uninitialized "hour" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:39 [warn] 25276#25276: *19970 using uninitialized "year" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:39 [warn] 25276#25276: *19970 using uninitialized "month" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:39 [warn] 25276#25276: *19970 using uninitialized "day" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:39 [warn] 25276#25276: *19970 using uninitialized "hour" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:40 [warn] 25276#25276: *19971 using uninitialized "year" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:40 [warn] 25276#25276: *19971 using uninitialized "month" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:40 [warn] 25276#25276: *19971 using uninitialized "day" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:40 [warn] 25276#25276: *19971 using uninitialized "hour" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:41 [warn] 25276#25276: *19974 using uninitialized "year" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:41 [warn] 25276#25276: *19974 using uninitialized "month" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:41 [warn] 25276#25276: *19974 using uninitialized "day" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/24 13:53:41 [warn] 25276#25276: *19974 using uninitialized "hour" variable while logging request, client: 172.31.33.212, server: , request: "POST /boaform/webLogin HTTP/1.1", host: "107.22.27.182"
2026/01/26 12:13:14 [error] 18619#18619: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 12:13:16 [error] 18619#18619: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 12:41:46 [error] 2488#2488: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 12:41:47 [error] 2488#2488: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 12:42:01 [error] 2488#2488: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 12:42:02 [error] 2488#2488: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 13:35:33 [error] 327#327: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 13:35:35 [error] 327#327: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 13:35:48 [error] 327#327: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 13:35:50 [error] 327#327: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 14:25:51 [error] 28850#28850: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 14:25:53 [error] 28850#28850: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 14:26:06 [error] 28850#28850: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/26 14:26:08 [error] 28850#28850: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"

