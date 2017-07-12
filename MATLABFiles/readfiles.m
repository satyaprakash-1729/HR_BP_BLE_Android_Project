function [BP, PPG] = readfiles()
    BP = NaN(94, 2);
    PPG = NaN(94, 16000);
    for fileNum = 1:94
        name = strcat(num2str(fileNum),".txt");
        fid = fopen(name, "r");
        for j=1:5
            vv = fgetl(fid);
        end
        [temp,~] = strsplit(vv, '/');
        [temp1, ~] = strsplit(temp{1}, '-');
        BP(fileNum, 2) = str2double(temp{2});
        BP(fileNum, 1) = str2double(temp1{2});
        vv = fgetl(fid);
        nums = NaN(1,16000);
        i = 1;
        while(ischar(vv))
            if(~isempty(vv))
                tempp = str2double(vv);
                if(tempp~=0)
                    nums(i) = tempp;
                end
                i = i + 1;
            end
            vv = fgetl(fid);
        end
        PPG(fileNum,:) = nums;
        fclose(fid);
    end
end