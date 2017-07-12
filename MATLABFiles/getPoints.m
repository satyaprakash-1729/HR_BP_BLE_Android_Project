function [rloc, qloc, sloc] = getPoints(temper, num)
    %N = length(temper);
    %fs = 100;
    %tm = [1:N]/fs;
    temper = temper(:);
    temper = temper';
    integ = ones(1,31)/31;
    %temper = conv(temper, integ);
    dtemper = diff(temper)*100;
    dtemper = dtemper/max(abs(dtemper));
    dtemper = conv(dtemper, integ);
    ddtemper = diff(dtemper)*100;
    ddtemper = ddtemper/max(abs(dtemper));
    ddtemper = conv(ddtemper, integ);
    [~, qloc1, ~, rloc1] = getPeaks(temper);
    zeross2 = ones(1,length(rloc1));
    for i=1:length(rloc1)-1
        tempo = ddtemper(rloc1(i):qloc1(i+1));
        step = 1;
        while zeross2(i)==1
            if(step + 1 > length(tempo)-step)
                break;
            end
            for j=step+1:length(tempo)-step
                if(tempo(j-step)*tempo(j+step)<0)
                    zeross2(i) = rloc1(i)+j-1;
                    break;
                end
            end
            step = step+1;
        end
    end
    %zeross2 = nonzeros(zeross2);
    zeross2 = zeross2(:);
    sloc1 = zeross2';
    backed = 0;
    rloc(1) = 1;
    sloc(1) = 1;
    qloc(1) = 1;
    for i=1:length(sloc1)
        if(sloc1(i)==1)
            backed=backed+1;
        else
            rloc(i-backed) = rloc1(i);
            sloc(i-backed) = sloc1(i);
            qloc(i-backed) = qloc1(i);
        end
    end
    %if(num==75)
    %    figure(1);
    %    subplot(2,1,1);
    %    plot(tm, temper, tm(rloc),temper(rloc),'r^',tm(sloc),temper(sloc),'g*',tm(qloc),temper(qloc),'ro');
    %    title('all points');
    %    subplot(2,1,2);
    %    plot(tm, temper, tm(rloc),temper(rloc),'r^',tm(sloc),temper(sloc),'g*',tm(qloc),temper(qloc),'ro'); xlim([5 10]);
    %    title('5-10 sec');
    %	end
end
