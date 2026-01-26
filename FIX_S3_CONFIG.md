----------------------------------------
/var/log/web.stdout.log
----------------------------------------
Jan 24 11:52:10 ip-172-31-34-63 web: at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
Jan 24 11:52:10 ip-172-31-34-63 web: at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
Jan 24 11:52:10 ip-172-31-34-63 web: at java.base/java.lang.reflect.Method.invoke(Method.java:566)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1072)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898)
Jan 24 11:52:10 ip-172-31-34-63 web: at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
Jan 24 11:52:10 ip-172-31-34-63 web: at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:209)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:111)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:337)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.access.intercept.AuthorizationFilter.doFilter(AuthorizationFilter.java:96)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:122)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:116)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:126)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:81)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:109)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:149)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at com.odc.aws_learning.auth.config.JwtAuthenticationFilter.doFilterInternal(JwtAuthenticationFilter.java:79)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:103)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:89)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:112)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:82)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:55)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:346)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:221)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:186)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:354)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:267)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:117)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:178)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:153)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:481)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:130)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:926)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1791)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
Jan 24 11:52:10 ip-172-31-34-63 web: at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
Jan 24 11:52:10 ip-172-31-34-63 web: at java.base/java.lang.Thread.run(Thread.java:829)
Jan 24 11:52:10 ip-172-31-34-63 web: âš ï¸ [CourseMapper] Aucune catÃ©gorie trouvÃ©e, retour 'Non catÃ©gorisÃ©'
Jan 24 11:52:10 ip-172-31-34-63 web: Total instructeurs actifs: 0
Jan 24 11:52:10 ip-172-31-34-63 web: Cours le plus consultÃ© (max inscriptions): 4
Jan 24 11:52:10 ip-172-31-34-63 web: Taux de satisfaction: 98.0% (rating moyen: null)
Jan 24 11:52:10 ip-172-31-34-63 web: Statistiques publiques: {totalInstructors=0, totalStudents=4, mostViewedCourses=4, totalCourses=2, satisfactionRate=98.0}


----------------------------------------
/var/log/eb-engine.log
----------------------------------------
2026/01/23 19:26:57.163149 [INFO] stop X-Ray ...
2026/01/23 19:26:57.163159 [INFO] Running command: systemctl show -p PartOf xray.service
2026/01/23 19:26:57.170824 [WARN] stopProcess Warning: process xray is not registered
2026/01/23 19:26:57.170843 [INFO] Running command: systemctl stop xray.service
2026/01/23 19:26:57.180469 [INFO] Executing instruction: stop proxy
2026/01/23 19:26:57.180484 [INFO] Running command: systemctl show -p PartOf httpd.service
2026/01/23 19:26:57.185104 [WARN] deregisterProcess Warning: process httpd is not registered, skipping...

2026/01/23 19:26:57.185119 [INFO] Running command: systemctl show -p PartOf nginx.service
2026/01/23 19:26:57.189867 [INFO] Running command: systemctl is-active nginx.service
2026/01/23 19:26:57.193559 [INFO] Running command: systemctl show -p PartOf nginx.service
2026/01/23 19:26:57.197900 [INFO] Running command: systemctl stop nginx.service
2026/01/23 19:26:57.218508 [INFO] Running command: systemctl disable nginx.service
2026/01/23 19:26:57.314214 [INFO] Running command: systemctl daemon-reload
2026/01/23 19:26:57.405591 [INFO] Running command: systemctl reset-failed
2026/01/23 19:26:57.410028 [INFO] Executing instruction: FlipApplication
2026/01/23 19:26:57.410040 [INFO] Fetching environment variables...
2026/01/23 19:26:57.410259 [INFO] Purge old process...
2026/01/23 19:26:57.410276 [INFO] Running command: systemctl stop eb-app.target
2026/01/23 19:26:57.415888 [INFO] Running command: systemctl show -p ConsistsOf eb-app.target | cut -d= -f2
2026/01/23 19:26:57.435976 [INFO] web.service

2026/01/23 19:26:57.435990 [INFO] deregistering process: web
2026/01/23 19:26:57.435999 [INFO] Running command: systemctl show -p PartOf web.service
2026/01/23 19:26:57.450484 [INFO] Running command: systemctl is-active web.service
2026/01/23 19:26:57.470509 [INFO] Running command: systemctl disable web.service
2026/01/23 19:26:57.615485 [INFO] Removed symlink /etc/systemd/system/multi-user.target.wants/web.service.

2026/01/23 19:26:57.615589 [INFO] Running command: systemctl daemon-reload
2026/01/23 19:26:57.735074 [INFO] Running command: systemctl reset-failed
2026/01/23 19:26:57.739830 [INFO] Running command: systemctl is-active web.service
2026/01/23 19:26:57.743493 [INFO] Process web has been fully terminated
2026/01/23 19:26:57.743506 [INFO] All processes have been fully terminated
2026/01/23 19:26:57.743511 [INFO] Removing /var/app/current/ if it exists
2026/01/23 19:26:57.748442 [INFO] Renaming /var/app/staging/ to /var/app/current/
2026/01/23 19:26:57.748466 [INFO] Register application processes...
2026/01/23 19:26:57.748471 [INFO] Registering the proc: web

2026/01/23 19:26:57.748505 [INFO] Running command: systemctl show -p PartOf web.service
2026/01/23 19:26:57.755680 [INFO] Running command: systemctl daemon-reload
2026/01/23 19:26:57.845335 [INFO] Running command: systemctl reset-failed
2026/01/23 19:26:57.849708 [INFO] Running command: systemctl is-enabled eb-app.target
2026/01/23 19:26:57.853641 [INFO] Running command: systemctl enable eb-app.target
2026/01/23 19:26:57.941734 [INFO] Running command: systemctl start eb-app.target
2026/01/23 19:26:57.947100 [INFO] Running command: systemctl enable web.service
2026/01/23 19:26:58.037829 [INFO] Created symlink from /etc/systemd/system/multi-user.target.wants/web.service to /etc/systemd/system/web.service.

2026/01/23 19:26:58.037860 [INFO] Running command: systemctl show -p PartOf web.service
2026/01/23 19:26:58.043226 [INFO] Running command: systemctl is-active web.service
2026/01/23 19:26:58.046859 [INFO] Running command: systemctl start web.service
2026/01/23 19:26:58.065357 [INFO] Executing instruction: start X-Ray
2026/01/23 19:26:58.065371 [INFO] X-Ray is not enabled.
2026/01/23 19:26:58.065376 [INFO] Executing instruction: start proxy with new configuration
2026/01/23 19:26:58.065392 [INFO] Running command: /usr/sbin/nginx -t -c /var/proxy/staging/nginx/nginx.conf
2026/01/23 19:26:58.082957 [INFO] nginx: the configuration file /var/proxy/staging/nginx/nginx.conf syntax is ok
nginx: configuration file /var/proxy/staging/nginx/nginx.conf test is successful

2026/01/23 19:26:58.083161 [INFO] Running command: cp -rp /var/proxy/staging/nginx/* /etc/nginx
2026/01/23 19:26:58.089289 [INFO] Running command: systemctl show -p PartOf nginx.service
2026/01/23 19:26:58.101778 [INFO] Running command: systemctl daemon-reload
2026/01/23 19:26:58.258842 [INFO] Running command: systemctl reset-failed
2026/01/23 19:26:58.268123 [INFO] Running command: systemctl show -p PartOf nginx.service
2026/01/23 19:26:58.279419 [INFO] Running command: systemctl is-active nginx.service
2026/01/23 19:26:58.291174 [INFO] Running command: systemctl start nginx.service
2026/01/23 19:26:58.387904 [INFO] Executing instruction: configureSqsd
2026/01/23 19:26:58.387920 [INFO] This is a web server environment instance, skip configure sqsd daemon ...
2026/01/23 19:26:58.387925 [INFO] Executing instruction: startSqsd
2026/01/23 19:26:58.387929 [INFO] This is a web server environment instance, skip start sqsd daemon ...
2026/01/23 19:26:58.387933 [INFO] Executing instruction: Track pids in healthd
2026/01/23 19:26:58.387937 [INFO] This is an enhanced health env...
2026/01/23 19:26:58.387948 [INFO] Running command: systemctl show -p ConsistsOf aws-eb.target | cut -d= -f2
2026/01/23 19:26:58.403932 [INFO] cfn-hup.service nginx.service healthd.service

2026/01/23 19:26:58.403952 [INFO] Running command: systemctl show -p ConsistsOf eb-app.target | cut -d= -f2
2026/01/23 19:26:58.417824 [INFO] web.service

2026/01/23 19:26:58.417983 [INFO] Executing instruction: RunAppDeployPostDeployHooks
2026/01/23 19:26:58.418015 [INFO] Executing platform hooks in .platform/hooks/postdeploy/
2026/01/23 19:26:58.418033 [INFO] The dir .platform/hooks/postdeploy/ does not exist
2026/01/23 19:26:58.418037 [INFO] Finished running scripts in /var/app/current/.platform/hooks/postdeploy
2026/01/23 19:26:58.418044 [INFO] Executing cleanup logic
2026/01/23 19:26:58.418125 [INFO] CommandService Response: {"status":"SUCCESS","api_version":"1.0","results":[{"status":"SUCCESS","msg":"Engine execution has succeeded.","returncode":0,"events":[{"msg":"Instance deployment successfully detected a JAR file in your source bundle.","timestamp":1769196414802,"severity":"INFO"},{"msg":"Instance deployment successfully generated a 'Procfile'.","timestamp":1769196414953,"severity":"INFO"},{"msg":"Instance deployment completed successfully.","timestamp":1769196418418,"severity":"INFO"}]}]}

2026/01/23 19:26:58.419328 [INFO] Platform Engine finished execution on command: app-deploy

2026/01/24 11:52:19.328191 [INFO] Starting...
2026/01/24 11:52:19.328235 [INFO] Starting EBPlatform-PlatformEngine
2026/01/24 11:52:19.328257 [INFO] reading event message file
2026/01/24 11:52:19.328594 [INFO] Engine received EB command cfn-hup-exec

2026/01/24 11:52:19.399437 [INFO] Running command: /opt/aws/bin/cfn-get-metadata -s arn:aws:cloudformation:us-east-1:016299216814:stack/awseb-e-rafruf9ypt-stack/21700810-efd8-11f0-967a-0affd2545ccb -r AWSEBAutoScalingGroup --region us-east-1
2026/01/24 11:52:19.743213 [INFO] Running command: /opt/aws/bin/cfn-get-metadata -s arn:aws:cloudformation:us-east-1:016299216814:stack/awseb-e-rafruf9ypt-stack/21700810-efd8-11f0-967a-0affd2545ccb -r AWSEBBeanstalkMetadata --region us-east-1
2026/01/24 11:52:20.076244 [INFO] checking whether command tail-log is applicable to this instance...
2026/01/24 11:52:20.076261 [INFO] this command is applicable to the instance, thus instance should execute command
2026/01/24 11:52:20.076265 [INFO] Engine command: (tail-log)

2026/01/24 11:52:20.076326 [INFO] Executing instruction: GetTailLogs
2026/01/24 11:52:20.076331 [INFO] Tail Logs...
2026/01/24 11:52:20.076701 [INFO] Running command: tail -n 100 /var/log/web.stdout.log
2026/01/24 11:52:20.078712 [INFO] Running command: tail -n 100 /var/log/eb-engine.log


----------------------------------------
/var/log/eb-hooks.log
----------------------------------------


----------------------------------------
/var/log/nginx/access.log
----------------------------------------
172.31.33.212 - - [24/Jan/2026:11:49:34 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:49:46 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [24/Jan/2026:11:49:49 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:50:01 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [24/Jan/2026:11:50:04 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:50:16 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [24/Jan/2026:11:50:19 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:50:31 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [24/Jan/2026:11:50:34 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:50:45 +0000] "OPTIONS /awsodclearning/auth/signin HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:45 +0000] "POST /awsodclearning/auth/signin HTTP/1.1" 200 950 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:46 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "OPTIONS /awsodclearning/analytics/admin-dashboard-analytics HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "OPTIONS /awsodclearning/api/analytics/user-growth?timeFilter=3-months HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "OPTIONS /awsodclearning/analytics/comparison-stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "OPTIONS /awsodclearning/analytics/admin-dashboard-analytics HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "OPTIONS /awsodclearning/api/admin/reports/audit/recent?limit=5 HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.33.212 - - [24/Jan/2026:11:50:49 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "GET /awsodclearning/api/admin/reports/audit/recent?limit=5 HTTP/1.1" 200 106 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "GET /awsodclearning/api/analytics/user-growth?timeFilter=3-months HTTP/1.1" 200 405 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "GET /awsodclearning/analytics/comparison-stats HTTP/1.1" 200 437 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "GET /awsodclearning/analytics/admin-dashboard-analytics HTTP/1.1" 200 457 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:49 +0000] "GET /awsodclearning/analytics/admin-dashboard-analytics HTTP/1.1" 200 457 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/cohorte/read HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/get-all HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "GET /awsodclearning/cohorte/read HTTP/1.1" 200 73 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "GET /awsodclearning/api/apprenants/get-all HTTP/1.1" 200 3668 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/1/stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/3/stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/2/stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/4/stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/14/stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/5/stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/15/stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "OPTIONS /awsodclearning/api/apprenants/7/stats HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "GET /awsodclearning/api/apprenants/2/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "GET /awsodclearning/api/apprenants/1/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "GET /awsodclearning/api/apprenants/4/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:55 +0000] "GET /awsodclearning/api/apprenants/3/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:56 +0000] "GET /awsodclearning/api/apprenants/7/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:56 +0000] "GET /awsodclearning/api/apprenants/15/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:56 +0000] "GET /awsodclearning/api/apprenants/14/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:50:56 +0000] "GET /awsodclearning/api/apprenants/5/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:01 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:51:03 +0000] "OPTIONS /awsodclearning/api/apprenants/15 HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:03 +0000] "DELETE /awsodclearning/api/apprenants/15 HTTP/1.1" 200 125 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.33.212 - - [24/Jan/2026:11:51:04 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:51:09 +0000] "OPTIONS /awsodclearning/api/apprenants/14 HTTP/1.1" 200 0 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:09 +0000] "DELETE /awsodclearning/api/apprenants/14 HTTP/1.1" 200 125 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/cohorte/read HTTP/1.1" 200 73 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/get-all HTTP/1.1" 200 2775 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/get-all HTTP/1.1" 200 2775 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/1/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/7/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/4/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/3/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/2/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/5/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/2/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/7/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/1/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/4/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/5/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:11 +0000] "GET /awsodclearning/api/apprenants/3/stats HTTP/1.1" 200 182 "https://admin.smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:16 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:51:19 +0000] "OPTIONS /awsodclearning/cohorte/read HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:19 +0000] "GET /awsodclearning/cohorte/read HTTP/1.1" 200 73 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.33.212 - - [24/Jan/2026:11:51:19 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:51:23 +0000] "GET /awsodclearning/cohorte/read HTTP/1.1" 200 73 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:51:31 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [24/Jan/2026:11:51:34 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:51:46 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [24/Jan/2026:11:51:49 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:52:01 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [24/Jan/2026:11:52:04 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.72.116 - - [24/Jan/2026:11:52:07 +0000] "POST /awsodclearning/auth/signup HTTP/1.1" 200 812 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:08 +0000] "OPTIONS /awsodclearning/api/apprenants/create HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:08 +0000] "POST /awsodclearning/api/apprenants/create HTTP/1.1" 200 506 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/api/dashboard/student HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/courses/read HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/api/profile/me HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/api/dashboard/public-stats HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/api/learn/recent-activity?limit=3 HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/api/profile/me/certificates HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/api/learn/learning-progress?period=week HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/details-course/my-completed-courses HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/api/learn/next-steps HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "OPTIONS /awsodclearning/api/learn/upcoming-deadlines HTTP/1.1" 200 0 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/api/learn/next-steps HTTP/1.1" 200 210 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/api/learn/learning-progress?period=week HTTP/1.1" 200 377 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/api/profile/me/certificates HTTP/1.1" 200 103 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/api/dashboard/student HTTP/1.1" 200 223 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/api/profile/me HTTP/1.1" 200 154 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/api/learn/upcoming-deadlines HTTP/1.1" 200 103 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/api/learn/recent-activity?limit=3 HTTP/1.1" 200 109 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/details-course/my-completed-courses HTTP/1.1" 200 108 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/api/dashboard/public-stats HTTP/1.1" 200 215 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:10 +0000] "GET /awsodclearning/courses/read HTTP/1.1" 200 1489 "https://smart-odc.com/" "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36" "41.73.99.218"
172.31.72.116 - - [24/Jan/2026:11:52:16 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"
172.31.33.212 - - [24/Jan/2026:11:52:19 +0000] "GET / HTTP/1.1" 404 431 "-" "ELB-HealthChecker/2.0" "-"


----------------------------------------
/var/log/nginx/error.log
----------------------------------------
2026/01/21 13:38:41 [error] 1413#1413: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 13:38:45 [error] 1413#1413: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 13:59:08 [error] 13392#13392: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET /.env HTTP/1.1", upstream: "http://127.0.0.1:5000/.env", host: "23.23.144.57"
2026/01/21 13:59:09 [error] 13392#13392: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "POST / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "23.23.144.57"
2026/01/21 13:59:12 [error] 13392#13392: *4 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 13:59:16 [error] 13392#13392: *6 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 13:59:27 [error] 13392#13392: *8 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 14:44:00 [error] 6391#6391: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 14:44:04 [error] 6391#6391: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 14:44:10 [error] 6391#6391: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "POST /awsodclearning/auth/signin HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/auth/signin", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/dashboard/student HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/dashboard/student", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *8 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/profile/me HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/profile/me", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *10 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/profile/me/certificates HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/profile/me/certificates", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *12 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/learn/recent-activity?limit=3 HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/learn/recent-activity?limit=3", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *14 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/details-course/my-completed-courses HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/details-course/my-completed-courses", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *16 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/learn/learning-progress?period=week HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/learn/learning-progress?period=week", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *18 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/learn/next-steps HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/learn/next-steps", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *20 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/api/learn/upcoming-deadlines HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/api/learn/upcoming-deadlines", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:11 [error] 6391#6391: *16 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/cohorte/read HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/cohorte/read", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 14:44:15 [error] 6391#6391: *24 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 14:44:19 [error] 6391#6391: *26 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 14:44:19 [error] 6391#6391: *16 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/auth/signin HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/auth/signin", host: "api.smart-odc.com", referrer: "https://smart-odc.com/"
2026/01/21 17:47:51 [warn] 6391#6391: *4814 an upstream response is buffered to a temporary file /var/lib/nginx/tmp/proxy/1/00/0000000001 while reading upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/swagger-ui/swagger-ui-bundle.js HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/swagger-ui/swagger-ui-bundle.js", host: "odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com", referrer: "http://odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com/awsodclearning/swagger-ui/index.html"
2026/01/21 17:59:32 [warn] 6391#6391: *5249 an upstream response is buffered to a temporary file /var/lib/nginx/tmp/proxy/2/00/0000000002 while reading upstream, client: 172.31.33.212, server: , request: "GET /awsodclearning/swagger-ui/swagger-ui-bundle.js HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/swagger-ui/swagger-ui-bundle.js", host: "odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com"
2026/01/21 20:07:39 [error] 25069#25069: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/auth/signin HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/auth/signin", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/21 20:07:45 [error] 25069#25069: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "OPTIONS /awsodclearning/auth/signin HTTP/1.1", upstream: "http://127.0.0.1:5000/awsodclearning/auth/signin", host: "api.smart-odc.com", referrer: "https://admin.smart-odc.com/"
2026/01/21 20:07:46 [error] 25069#25069: *4 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 20:07:50 [error] 25069#25069: *6 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.33.212, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 20:08:01 [error] 25069#25069: *8 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
2026/01/21 20:21:02 [error] 509#509: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.31.72.116, server: , request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:5000/", host: "172.31.34.63"
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

