function [ SfeatureVector, DfeatureVector ] = extractFeatures(x, SBP, DBP, num)
    [rloc, qloc, sloc] = getPoints(x, num);
    N = length(x);
    fs = 100;
    tm = [1:N]/fs;
    if(length(rloc)==1)
        SfeatureVector = NaN(1,11);
        DfeatureVector = NaN(1,11);
    else
        Asys = x(rloc(1:end-1));
        Adn = x(sloc(1:end-1));
        Aval = x(qloc(1:end-1));
        SysArea = zeros(1,length(rloc)-1);
        for i=1:length(rloc)-1
            SysArea(i) = sum(x(rloc(i):sloc(i)));
        end
        DNArea = zeros(1,length(rloc)-1);
        for i=1:length(rloc)-1
            DNArea(i) = sum(x(sloc(i):qloc(i+1)));
        end
        TotArea = SysArea + DNArea;
        PeakInt = diff(tm(rloc));
        PulseHt = Asys - Aval;
        PulseInt = diff(tm(qloc));
        AugIndex = (Adn-Aval)./(Asys-Aval);
        AugIndex(find(isinf(AugIndex)==1)) = 1;
        AugIndex(find(isnan(AugIndex)==1)) = 0;
        %ReflIndex = ones(1,length(AugIndex)) - AugIndex;
        for i=1:length(Aval)
            SfeatureVector(i,:) = [Asys(i),Adn(i),Aval(i),SysArea(i),DNArea(i),TotArea(i),PeakInt(i),PulseHt(i),PulseInt(i),AugIndex(i),SBP];
            DfeatureVector(i,:) = [Asys(i),Adn(i),Aval(i),SysArea(i),DNArea(i),TotArea(i),PeakInt(i),PulseHt(i),PulseInt(i),AugIndex(i),DBP];
        end
    end
end