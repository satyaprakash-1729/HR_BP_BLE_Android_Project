function [ SfeatureMatrix, DfeatureMatrix ] = getMatrix()
    xx = load('PPG94.mat');
    PPG = xx.PPG;
    xx = load('BP94.mat');
    BP = xx.BP;
    ansMatrix1 = [];
    ansMatrix2 = [];
    for fileNum=1:94
        fileNum
        SBP = BP(fileNum,1);
        DBP = BP(fileNum,2);
        temper = PPG(fileNum,~isnan(PPG(fileNum,:)));
        temper = temper(50:end);
        temper = temper - mean(temper);
        temper = temper/max(abs(temper));
        [ SfeatureVector, DfeatureVector ] = extractFeatures(temper, SBP, DBP, fileNum);
        if(size(SfeatureVector,1)==1 && isnan(SfeatureVector(1)))
            continue;
        end
        ansMatrix1 = [ansMatrix1; SfeatureVector];
        ansMatrix2 = [ansMatrix2; DfeatureVector];
    end
    SfeatureMatrix = ansMatrix1;
    DfeatureMatrix = ansMatrix2;
end