function [qval, qloc, rval, rloc] = getPeaks(x1)
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
    x6 = x2;
% 
%     b = [-1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 32 -32 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1];
%     a = [1 -1];
%     %b = [1 -1];
%     %a = [1];
% 
%     h_HP=filter(b,a,[1 zeros(1,32)]); % impulse response iof HPF
% 
%     x3 = conv (x2 ,h_HP);
%     x3 = x3/ max( abs(x3 ));
%     x6 = x3;
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
%     b=[-1 -2 0 2 1]/8;
%     a=[1];
%     h_D=filter(b,a,[1 zeros(1,4)]);
%     x4 = conv (x3 ,h_D);
%     x4 = x4 (2+[1: N]);
%     x4 = x4/ max( abs(x4 ));
% 
%     %figure(5)
%     %subplot(2,1,1)
%     %plot([0:length(x4)-1]/fs,x4)
%     %xlabel('second');ylabel('Volts');title(' ECG Signal after Derivative')
%     %xlim([0 max(t)])
%     %subplot(2,1,2)
%     %plot(t(200:2200),x4(200:2200))
%     %xlabel('second');ylabel('Volts');title(' ECG Signal 0.2-2.2 second')
%     %xlim([0.2 2.2])
% 
%     x5 = x4 .^2;
%     x5 = x5/ max( abs(x5 ));
% 
%     %figure(6)
%     %subplot(2,1,1)
%     %plot([0:length(x5)-1]/fs,x5)
%     %xlabel('second');ylabel('Volts');title(' ECG Signal after Squaring')
%     %xlim([0 max(t)])
%     %subplot(2,1,2)
%     %plot(t(200:2200),x5(200:2200))
%     %xlabel('second');ylabel('Volts');title(' ECG Signal 0.2-2.2 second')
%     %xlim([0.2 2.2])
% 
%     h = ones (1 ,31)/31;
% 
%     % Apply filter
%     x6 = conv (x5 ,h);
%     x6 = x6 (15+[1: N]);
%     x6 = x6/ max( abs(x6 ));

%     figure(7)
%     plot([0:length(x6)-1]/100,x6)
%     xlabel('second');ylabel('Volts');title(' ECG Signal after Averaging')
    %xlim([0 max(t)])
    %subplot(2,1,2)
    %plot(t(200:2200),x6(200:2200))
    %xlabel('second');ylabel('Volts');title(' ECG Signal 0.2-2.2 second')
    %xlim([0.2 2.2])

    %figure(7)
    %subplot(2,1,1)
    sorted = sort(x6);
    max_h = sorted(end-100);
    thresh = mean (x6 );
    thresVal = 0.2;
    poss_reg =(x6>thresh+thresVal*max_h);
    left = find(diff([0 poss_reg])==1);
    right = find(diff([poss_reg 0])==-1);
%     prevLeft = [];
%     prevRight = [];
%     while(1)
%         poss_reg =(x6>thresVal*max_h);
%         left = find(diff([0 poss_reg])==1);
%         right = find(diff([poss_reg 0])==-1);
%         if(length(left)/(N/100) > 1.5 || length(prevLeft) > length(left))
%             left = prevLeft;
%             right = prevRight;
%             break;
%         else
%             thresVal = thresVal - 0.05;
%             prevLeft = left;
%             prevRight = right;
%         end
%     end
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
    
    Q_value=zeros(1,length(left));R_value=zeros(1,length(left));R_loc = zeros(1,length(left));Q_loc=zeros(1,length(left));
%     Q_value=[];R_value=[];R_loc = [];Q_loc=[];
    R_loc(1) = 1;
    R_value(1) = 1;
    for i=1:length(left)
        if i>1 && left(i)<length(x1) && right(i)<length(x1)
            [R_value(i) ,R_loc(i)] = max( x1(left(i):right(i)) );
            R_loc(i) = R_loc(i)-1+left(i); % add offset
            [Q_value(i) ,Q_loc(i)] = min( x1(R_loc(i-1):left(i)) );
            Q_loc(i) = Q_loc(i)-1+R_loc(i-1); % add offset
            %[rv rl] = getPeaks2(x1(R_loc(i-1):Q_loc(i)), Q_loc(i)-R_loc(i-1)+1);%max( x1(R_loc(i-1):Q_loc(i)) );
            %S_loc(i) = max(rl);
            %S_value(i) = max(rv);
            %S_loc(i) = S_loc(i)-1+left(i); % add offset
        end

    end
    rval = R_value(:,2:length(left)-1);
    rloc = R_loc(:,2:length(left)-1);
    %sval = S_value(:,3:length(left)-3);
    %sloc = S_loc(:,3:length(left)-3);
    if(isempty(Q_value))
        Q_value = zeros(1,length(R_value));
        Q_loc = ones(1,length(R_value));
    end
    qval = Q_value(:,2:length(left)-1);
    qloc = Q_loc(:,2:length(left)-1);
    %figure
    %subplot(2,1,1)
    %title('ECG Signal with R points');
    %plot (t,x1/max(x1) , t(R_loc) ,R_value , 'r^', t(S_loc) ,S_value, '*',t(Q_loc) , Q_value, 'o');
    %legend('ECG','R','S','Q');
    %subplot(2,1,2)
    %plot (t,x1/max(x1) , t(R_loc) ,R_value , 'r^', t(S_loc) ,S_value, '*',t(Q_loc) , Q_value, 'o');
    %xlim([0.2 2.2])
end