function [Asys,Adn,Aval,SysArea,DNArea,totArea,peakInt,pulseHeight,pulseInt] = featureFinder(x, N, fs)
    tm = [1:N]/fs;
    x = x - mean(x);
    x = x/max(abs(x));
    [qval, qloc, rval, rloc] = getPeaks(x, N);
    sloc = slocsFinder(x, rloc, qloc);
    %plot(tm, x/max(x), tm(rloc(5)), x(rloc(5))/max(x), 'r^', tm(qloc(5)), x(qloc(5))/max(x), '*', tm(sloc(5)), x(sloc(5))/max(x), 'go'); xlim([0 10]);
    figure(2);
    subplot(2,1,1);
    title('All Data');
    plot(tm, x/max(x), tm(rloc), x(rloc)/max(x), 'r^', tm(qloc), x(qloc)/max(x), '*', tm(sloc), x(sloc)/max(x), 'go');
    subplot(2,1,2);
    title('5-25 seconds of data');
    plot(tm, x/max(x), tm(rloc), x(rloc)/max(x), 'r^', tm(qloc), x(qloc)/max(x), '*', tm(sloc), x(sloc)/max(x), 'go'); xlim([5 15]);
    Asys = x(rloc(:,1:length(rloc)-1));
    Adn = x(sloc(:,1:length(rloc)-1));
    Aval = x(qloc(:,1:length(rloc)-1));
    SysArea(1) = 0;
    for i=1:length(rloc)-1
        SysArea(i) = sum(x(rloc(i):sloc(i)));
    end
    DNArea(1) = 0;
    for i=1:length(rloc)-1
        DNArea(i) = sum(x(sloc(i):qloc(i+1)));
    end
    totArea = SysArea + DNArea;
    peakInt = diff(tm(rloc));
    pulseHeight = (x(rloc)-x(qloc));
    pulseHeight = pulseHeight(:,1:length(rloc)-1);
    pulseInt = diff(tm(qloc));
end