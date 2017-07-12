function [rval, rloc] = getPeaks2(x1, N)
   %close all;
    %clc;
    %figure(1)
    %subplot(2,1,1);
    %plot(t,x1)
    %xlabel('second');ylabel('Volts');title('Input ECG Signal')
    %subplot(2,1,2)
    %plot(t(200:2200),x1(200:2200))
    %xlabel('second');ylabel('Volts');title('Input ECG Signal 0.2-2.2 second')
    %xlim([0.2 2.2])

    x1 = x1 - mean (x1 );    % cancel DC conponents
    x1 = x1/ max( abs(x1 )); % normalize to one

    %figure(2)
    %subplot(2,1,1)
    %plot(t,x1)
    %xlabel('second');ylabel('Volts');title(' ECG Signal after cancellation DC drift and normalization')
    %subplot(2,1,2)
    %plot(t(200:2200),x1(200:2200))
    %xlabel('second');ylabel('Volts');title(' ECG Signal 0.2-2.2 second')
    %xlim([0.2 2.2])

    % LPF (1-z^-6)^2/(1-z^-1)^2
    b=[1 0 0 0 0 0 -2 0 0 0 0 0 1];
    a=[1 -2 1];
    %b = [1 1];
    %a = [1];

    h_LP=filter(b,a,[1 zeros(1,12)]); % transfer function of LPF

    x2 = conv (x1 ,h_LP);
    %x2 = x2 (6+[1: N]); %cancle delay
    x2 = x2/ max( abs(x2 )); % normalize , for convenience .

    b = [-1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 32 -32 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1];
    a = [1 -1];
    %b = [1 -1];
    %a = [1];

    h_HP=filter(b,a,[1 zeros(1,32)]); % impulse response iof HPF

    x3 = conv (x2 ,h_HP);
    x3 = x3/ max( abs(x3 ));

    %figure(4)
    %subplot(2,1,1)
    %plot([0:length(x3)-1]/fs,x3)
    %xlabel('second');ylabel('Volts');title(' ECG Signal after HPF')
    %xlim([0 max(t)])
    %subplot(2,1,2)
    %plot(t(200:2200),x3(200:2200))
    %xlabel('second');ylabel('Volts');title(' ECG Signal 0.2-2.2 second')
    %xlim([0.2 2.2])

    %h = [2 1 0 -1 -2]/8;
    b=[-1 -2 0 2 1]/8;
    a=[1];
    h_D=filter(b,a,[1 zeros(1,4)]);
    x4 = conv (x3 ,h_D);
    x4 = x4 (2+[1: N]);
    x4 = x4/ max( abs(x4 ));

    %figure(5)
    %subplot(2,1,1)
    %plot([0:length(x4)-1]/fs,x4)
    %xlabel('second');ylabel('Volts');title(' ECG Signal after Derivative')
    %xlim([0 max(t)])
    %subplot(2,1,2)
    %plot(t(200:2200),x4(200:2200))
    %xlabel('second');ylabel('Volts');title(' ECG Signal 0.2-2.2 second')
    %xlim([0.2 2.2])

    x5 = x4 .^2;
    x5 = x5/ max( abs(x5 ));

    %figure(6)
    %subplot(2,1,1)
    %plot([0:length(x5)-1]/fs,x5)
    %xlabel('second');ylabel('Volts');title(' ECG Signal after Squaring')
    %xlim([0 max(t)])
    %subplot(2,1,2)
    %plot(t(200:2200),x5(200:2200))
    %xlabel('second');ylabel('Volts');title(' ECG Signal 0.2-2.2 second')
    %xlim([0.2 2.2])

    h = ones (1 ,31)/31;

    % Apply filter
    x6 = conv (x5 ,h);
    x6 = x6 (15+[1: N]);
    x6 = x6/ max( abs(x6 ));

    figure(7)
    plot([0:length(x6)-1]/100,x6)
    xlabel('second');ylabel('Volts');title(' ECG Signal after Averaging')

    %figure(7)
    %subplot(2,1,1)
    max_h = max(x6);
    thresh = mean (x6 );
    poss_reg =(x6>0.1);
    %figure (8)
    %subplot(2,1,1)
    %hold on
    %plot (t(200:2200),x1(200:2200)/max(x1))
    %box on
    %xlabel('second');ylabel('Integrated')
    %xlim([0.2 2.2])

    %subplot(2,1,2)
    %plot (t(200:2200),x6(200:2200)/max(x6))
    %xlabel('second');ylabel('Integrated')
    %xlim([0.2 2.2])

    left = find(diff([0 poss_reg])==1);
    right = find(diff([poss_reg 0])==-1);
    R_value(1) = 0;
    R_loc(1) = 1;
    for i=1:length(left)
        [R_value(i) R_loc(i)] = max( x1(left(i):right(i)) );
        R_loc(i) = R_loc(i)-1+left(i); % add offset
    end
    rval = R_value;
    rloc = R_loc;
    %figure
    %subplot(2,1,1)
    %title('ECG Signal with R points');
    %plot (t,x1/max(x1) , t(R_loc) ,R_value , 'r^', t(S_loc) ,S_value, '*',t(Q_loc) , Q_value, 'o');
    %legend('ECG','R','S','Q');
    %subplot(2,1,2)
    %plot (t,x1/max(x1) , t(R_loc) ,R_value , 'r^', t(S_loc) ,S_value, '*',t(Q_loc) , Q_value, 'o');
    %xlim([0.2 2.2])
end