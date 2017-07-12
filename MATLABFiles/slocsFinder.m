function sloc = slocsFinder(x, rloc, qloc)
    answer = ones(1, length(rloc));
    for i=1:length(rloc)-1
        temp2 = x(rloc(i):qloc(i+1));
        %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        temp1 = temp2;
        temp1 = temp1-mean(temp1);
        temp1 = temp1/max(abs(temp1));
        temp1 = (temp1(60+[1:length(temp1)-60]) - temp1(1:length(temp1)-60))*(500/120);
        
        integ = ones(1, 10)/10;
        temp1 = conv(temp1, integ);
        nn = length(temp1);
        temp1 = temp1(int64(nn/10):int64(9*nn/10));
        [~,rl] = max(temp1);
        %if(i==150)
        %    figure(1);
        %    tm1 = [1:length(temp2)]/500;
        %    tm2 = [1:length(temp1)]/500;
        %    subplot(2,1,1);
        %    plot(tm2, temp1);
        %    subplot(2,1,2);
        %    plot(tm1, temp2, tm1(int64(nn/10)+rl), temp2(int64(nn/10)+rl), 'r^');
        %end

        %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        %[rrr] = peakdet(temp1, 0.01);
        if(not(isempty(rl)))
            %temp = rrr(:,1);
            temp = rl;
            %int16(nn/10)
            tt = int64(nn/10)+rloc(i)+temp(1);
            answer(i) = tt;
        end
        %rloc(i)
        %answer(i)
    end
    sloc = answer;
end