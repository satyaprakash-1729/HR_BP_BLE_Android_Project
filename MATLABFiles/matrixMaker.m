function featureMatrix = matrixMaker(x, N, fs)
    [Asys,Adn,Aval,SysArea,DNArea,totArea,peakInt,pulseHeight,pulseInt] = featureFinder(x, N, fs);
    featureMatrix = [Asys' Adn' Aval' SysArea' DNArea' totArea' peakInt' pulseHeight' pulseInt'];
end