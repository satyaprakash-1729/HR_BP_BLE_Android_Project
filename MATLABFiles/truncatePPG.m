function [answer] = truncatePPG(ppg, n)
    for i=1:n
        ppg(i,1:end-199) = ppg(i,200:end);
    end
    answer = ppg;
end